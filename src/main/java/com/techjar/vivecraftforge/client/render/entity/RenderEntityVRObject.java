package com.techjar.vivecraftforge.client.render.entity;

import java.nio.FloatBuffer;
import java.util.UUID;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.mojang.authlib.GameProfile;
import com.techjar.vivecraftforge.client.render.model.ModelVRHead;
import com.techjar.vivecraftforge.entity.EntityVRArm;
import com.techjar.vivecraftforge.entity.EntityVRHead;
import com.techjar.vivecraftforge.entity.EntityVROffHandArm;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.entity.EntityVRMainArm;
import com.techjar.vivecraftforge.util.Axis;
import com.techjar.vivecraftforge.util.Quaternion;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.Vector3;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ForgeHooksClient;

public abstract class RenderEntityVRObject extends Render {
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	protected ModelBase model;
	protected ModelBiped modelArmor;
    protected ModelBiped modelArmorChestplate;
	protected int armorSlot;
	
	public RenderEntityVRObject() {
		modelArmorChestplate = new ModelBiped(1.0F);
		modelArmor = new ModelBiped(0.5F);
	}
	
	/**
	 * lol coords are not used here so don't even try
	 */
	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		EntityVRObject entityVR = (EntityVRObject)entity;
		if (entityVR.getEntityPlayer() == null || entityVR.getEntityPlayer() == Minecraft.getMinecraft().thePlayer) return;
		Vector3 position = Vector3.lerp(Util.convertVector(entityVR.positionLast), Util.convertVector(entityVR.position), Minecraft.getMinecraft().timer.renderPartialTicks).subtract(new Vector3((float)RenderManager.renderPosX, (float)RenderManager.renderPosY, (float)RenderManager.renderPosZ));
		Quaternion quat = Util.quatLerp(entityVR.getRotationLast(), entityVR.getRotation(), Minecraft.getMinecraft().timer.renderPartialTicks).normalized();
		Matrix4f quatMatrix = quat.getMatrix();
		Matrix4f rotation = new Matrix4f();
		
		// Don't ask about all this nonsense, found it by experimentation
		rotation.rotate((float)Math.PI, new Vector3f(0, -1, 0));
		Matrix4f.mul(rotation, quatMatrix, rotation);
		if (entity instanceof EntityVRArm) {
			rotation.rotate((float)Math.PI * 0.5F, new Vector3f(-1, 0, 0));
			rotation.rotate((float)Math.PI, new Vector3f(0, -1, 0));
		}
		if (entity instanceof EntityVRHead) rotation.rotate((float)Math.PI, new Vector3f(0, 0, -1));
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		float modelScale = 1F / 16F;
		float scale = Util.getVRPlayerScale(entityVR.getEntityPlayer());
		Matrix4f matrix = new Matrix4f();
		matrix.translate(Util.convertVector(position));
		if (entity instanceof EntityVRHead) {
			matrix.translate(new Vector3f(0, -0.25F, 0));
			scale /= Math.pow(scale, 0.5F);
		}
		Matrix4f.mul(matrix, rotation, matrix);
		matrix.scale(new Vector3f(scale, scale, scale));
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.store(buffer);
		buffer.rewind();
		GL11.glMultMatrix(buffer);
		bindEntityTexture(entity);
		preRenderModel(entityVR);
		model.render(entity, 0, 0, 0, 0, 0, modelScale);
		GL11.glPopMatrix();
		ItemStack armorStack = ((AbstractClientPlayer)entityVR.getEntityPlayer()).inventory.armorItemInSlot(armorSlot);
		if (armorStack != null && armorStack.getItem() instanceof ItemArmor) {
			bindTexture(RenderBiped.getArmorResource(entity, armorStack, 0, null));
			ModelBiped model = armorSlot == 1 ? modelArmor : modelArmorChestplate;
			model = ForgeHooksClient.getArmorModel((AbstractClientPlayer)entityVR.getEntityPlayer(), armorStack, 3 - armorSlot, model);
			model.bipedHead.showModel = true;
			model.bipedHeadwear.showModel = true;
            model.bipedBody.showModel = true;
            model.bipedRightArm.showModel = true;
            model.bipedLeftArm.showModel = true;
            model.bipedRightLeg.showModel = true;
            model.bipedLeftLeg.showModel = true;
			int bitFlags = 0;
			int color = ((ItemArmor)armorStack.getItem()).getColor(armorStack);
			if (color != -1) {
				float r = (color >> 16 & 0xFF) / 255F;
				float g = (color >> 8 & 0xFF) / 255F;
				float b = (color & 0xFF) / 255F;
				GL11.glColor3f(r, g, b);
				if (armorStack.isItemEnchanted()) {
					bitFlags = 31;
				} else {
					bitFlags = 16;
				}
			} else {
				GL11.glColor3f(1, 1, 1);
				if (armorStack.isItemEnchanted()) {
					bitFlags = 15;
				} else {
					bitFlags = 1;
				}
			}

			matrix.setIdentity();
			if (entity instanceof EntityVRHead) matrix.translate(new Vector3f(0, -0.25F, 0));
			matrix.translate(Util.convertVector(position));
			Matrix4f.mul(matrix, rotation, matrix);
			matrix.scale(new Vector3f(scale, scale, scale));
			matrix.translate(Util.convertVector(getArmorModelOffset(entityVR)));
			buffer.rewind();
			matrix.store(buffer);
			buffer.rewind();
			GL11.glPushMatrix();
			GL11.glMultMatrix(buffer);
			renderArmorModel(entityVR, model, modelScale);
			if ((bitFlags & 240) == 16) {
				bindTexture(RenderBiped.getArmorResource(entity, armorStack, 3 - armorSlot, "overlay"));
				GL11.glColor3f(1, 1, 1);
				renderArmorModel(entityVR, model, modelScale);
			}
			if ((bitFlags & 15) == 15) { // Woo black box code copied from a class
				float f8 = entity.ticksExisted + modelScale;
				this.bindTexture(RES_ITEM_GLINT);
				GL11.glEnable(GL11.GL_BLEND);
				float f9 = 0.5F;
				GL11.glColor4f(f9, f9, f9, 1.0F);
				GL11.glDepthFunc(GL11.GL_EQUAL);
				GL11.glDepthMask(false);

				for (int k = 0; k < 2; ++k) {
					GL11.glDisable(GL11.GL_LIGHTING);
					float f10 = 0.76F;
					GL11.glColor4f(0.5F * f10, 0.25F * f10, 0.8F * f10, 1.0F);
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
					GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glLoadIdentity();
					float f11 = f8 * (0.001F + (float) k * 0.003F) * 20.0F;
					float f12 = 0.33333334F;
					GL11.glScalef(f12, f12, f12);
					GL11.glRotatef(30.0F - (float) k * 60.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, f11, 0.0F);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					renderArmorModel(entityVR, model, modelScale);
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glDepthMask(true);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}
			GL11.glPopMatrix();
		} else if (armorSlot == 3 && armorStack.getItem() instanceof ItemBlock) {
            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(armorStack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, armorStack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

			matrix.setIdentity();
			if (entity instanceof EntityVRHead) matrix.translate(new Vector3f(0, -0.25F, 0));
			matrix.translate(Util.convertVector(position));
			Matrix4f.mul(matrix, rotation, matrix);
			matrix.scale(new Vector3f(scale, scale, scale));
			
            if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(armorStack.getItem()).getRenderType())) {
            	matrix.translate(new Vector3f(0.0F, -0.25F, 0.0F));
            	matrix.rotate((float)Math.PI * 0.5F, new Vector3f(0, 1, 0));
            	matrix.scale(new Vector3f(0.625F, -0.625F, -0.625F));
            }

			buffer.rewind();
			matrix.store(buffer);
			buffer.rewind();
			GL11.glPushMatrix();
			GL11.glMultMatrix(buffer);
            this.renderManager.itemRenderer.renderItem(entityVR.getEntityPlayer(), armorStack, 0);
            GL11.glPopMatrix();
        } else if (armorSlot == 3 && armorStack.getItem() == Items.skull) {
            GameProfile gameprofile = null;

            if (armorStack.hasTagCompound())
            {
                NBTTagCompound nbttagcompound = armorStack.getTagCompound();

                if (nbttagcompound.hasKey("SkullOwner", 10))
                {
                    gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
                }
                else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner")))
                {
                    gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                }
            }

			matrix.setIdentity();
			if (entity instanceof EntityVRHead) matrix.translate(new Vector3f(0, -0.25F, 0));
			matrix.translate(Util.convertVector(position));
			Matrix4f.mul(matrix, rotation, matrix);
			matrix.scale(new Vector3f(scale, scale, scale));
        	matrix.scale(new Vector3f(1.0625F, -1.0625F, -1.0625F));
			buffer.rewind();
			matrix.store(buffer);
			buffer.rewind();
			GL11.glPushMatrix();
			GL11.glMultMatrix(buffer);
            TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, armorStack.getItemDamage(), gameprofile);
            GL11.glPopMatrix();
        }
		ItemStack heldStack = entityVR.getEntityPlayer().inventory.getCurrentItem();
		if (heldStack != null && entity instanceof EntityVRMainArm) {
			EntityPlayer player = entityVR.getEntityPlayer();
			matrix.setIdentity();
			matrix.translate(Util.convertVector(position));
			Matrix4f.mul(matrix, rotation, matrix);
			matrix.scale(new Vector3f(scale, scale, scale));
			matrix.translate(new Vector3f(0.05F, -0.02F, -0.45F));
			matrix.rotate((float)Math.PI * -0.3F, new Vector3f(-1, 0, 0));
			buffer.rewind();
			matrix.store(buffer);
			buffer.rewind();
			GL11.glPushMatrix();
			GL11.glMultMatrix(buffer);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            if (player.fishEntity != null)
            {
            	heldStack = new ItemStack(Items.stick);
            }

            EnumAction enumaction = null;

            if (player.getItemInUseCount() > 0)
            {
                enumaction = heldStack.getItemUseAction();
            }

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(heldStack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED, heldStack, net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (is3D || heldStack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(heldStack.getItem()).getRenderType()))
            {
                float something = 0.5F * 0.75F;
                GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
                GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(-something, -something, something);
            }
            else if (heldStack.getItem() == Items.bow)
            {
            	float something = 0.625F;
                GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
                GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(something, -something, something);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else if (heldStack.getItem().isFull3D())
            {
            	float something = 0.625F;

                if (heldStack.getItem().shouldRotateAroundWhenRendering())
                {
                    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0.0F, -0.125F, 0.0F);
                }

                if (player.getItemInUseCount() > 0 && enumaction == EnumAction.block)
                {
                    GL11.glTranslatef(0.05F, 0.0F, -0.1F);
                    GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
                }

                GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
                GL11.glScalef(something, -something, something);
                GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
            	float something = 0.375F;
                GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
                GL11.glScalef(something, something, something);
                GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
            }

            float f3;
            int k;
            float f12;

            if (heldStack.getItem().requiresMultipleRenderPasses())
            {
                for (k = 0; k < heldStack.getItem().getRenderPasses(heldStack.getItemDamage()); ++k)
                {
                    int i = heldStack.getItem().getColorFromItemStack(heldStack, k);
                    f12 = (float)(i >> 16 & 255) / 255.0F;
                    f3 = (float)(i >> 8 & 255) / 255.0F;
                    float f4 = (float)(i & 255) / 255.0F;
                    GL11.glColor4f(f12, f3, f4, 1.0F);
                    this.renderManager.itemRenderer.renderItem(player, heldStack, k);
                }
            }
            else
            {
                k = heldStack.getItem().getColorFromItemStack(heldStack, 0);
                float f11 = (float)(k >> 16 & 255) / 255.0F;
                f12 = (float)(k >> 8 & 255) / 255.0F;
                f3 = (float)(k & 255) / 255.0F;
                GL11.glColor4f(f11, f12, f3, 1.0F);
                this.renderManager.itemRenderer.renderItem(player, heldStack, 0);
            }
            GL11.glPopMatrix();
		}
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	public abstract Vector3 getArmorModelOffset(EntityVRObject entity);
	public abstract void renderArmorModel(EntityVRObject entity, ModelBiped modelBiped, float scale);
	public void preRenderModel(EntityVRObject entity) {
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		EntityPlayer entityPlayer = ((EntityVRObject)entity).getEntityPlayer();
		return entityPlayer == null ? AbstractClientPlayer.locationStevePng : ((AbstractClientPlayer)entityPlayer).getLocationSkin();
	}
}
