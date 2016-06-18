package com.techjar.vivecraftforge.client.render.entity;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.techjar.vivecraftforge.client.render.model.ModelVRHead;
import com.techjar.vivecraftforge.entity.EntityVRHead;
import com.techjar.vivecraftforge.entity.EntityVRLeftArm;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.entity.EntityVRRightArm;
import com.techjar.vivecraftforge.util.Axis;
import com.techjar.vivecraftforge.util.Quaternion;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.Vector3;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
	public void doRender(Entity entity, double x, double y, double z, float pitch, float yaw) {
		EntityVRObject entityVR = (EntityVRObject)entity;
		Vector3 position = Vector3.lerp(Util.convertVector(entityVR.positionLast), Util.convertVector(entityVR.position), Minecraft.getMinecraft().timer.renderPartialTicks).subtract(new Vector3((float)RenderManager.renderPosX, (float)RenderManager.renderPosY, (float)RenderManager.renderPosZ));
		Quaternion quat = Util.quatLerp(entityVR.getRotationLast(), entityVR.getRotation(), Minecraft.getMinecraft().timer.renderPartialTicks).normalized();
		quat = quat.rotate(Axis.YAW, 90);
		Matrix4f rotation = quat.getMatrix().scale(new Vector3f(1, -1, 1));
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		Matrix4f matrix = new Matrix4f();
		matrix.translate(Util.convertVector(position));
		Matrix4f.mul(matrix, rotation, matrix);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.store(buffer);
		buffer.rewind();
		GL11.glMultMatrix(buffer);
		bindEntityTexture(entity);
		model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
		GL11.glPopMatrix();
		//ItemStack armorStack = entityVR.getEntityPlayer() != null ? ((AbstractClientPlayer)entityVR.getEntityPlayer()).inventory.armorItemInSlot(armorSlot) : null;
		ItemStack armorStack = Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(armorSlot);
		if (armorStack != null && armorStack.getItem() instanceof ItemArmor) {
			bindTexture(RenderBiped.getArmorResource(entity, armorStack, 0, null));
			ModelBiped model = armorSlot == 1 ? modelArmor : modelArmorChestplate;
			//model = ForgeHooksClient.getArmorModel((AbstractClientPlayer)entityVR.getEntityPlayer(), armorStack, 3 - armorSlot, model);
			model.bipedHead.showModel = armorSlot == 3;
			model.bipedHeadwear.showModel = armorSlot == 3;
            model.bipedBody.showModel = /*armorSlot == 2 ||*/ armorSlot == 1;
            model.bipedRightArm.showModel = armorSlot == 2 && entity instanceof EntityVRRightArm;
            model.bipedLeftArm.showModel = armorSlot == 2 && entity instanceof EntityVRLeftArm;
            model.bipedRightLeg.showModel = armorSlot == 1 || armorSlot == 0;
            model.bipedLeftLeg.showModel = armorSlot == 1 || armorSlot == 0;
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
			matrix.translate(Util.convertVector(position));
			Matrix4f.mul(matrix, rotation, matrix);
			switch (armorSlot) {
				case 2:
					break;
				case 3:
					matrix.translate(Util.convertVector(new Vector3(0, 0.25F, 0)));
					break;
			}
			buffer.rewind();
			matrix.store(buffer);
			buffer.rewind();
			GL11.glPushMatrix();
			GL11.glMultMatrix(buffer);
			model.bipedHead.render(0.0625F);
			model.bipedHeadwear.render(0.0625F);
			if ((bitFlags & 240) == 16) {
				bindTexture(RenderBiped.getArmorResource(entity, armorStack, 3 - armorSlot, "overlay"));
				GL11.glColor3f(1, 1, 1);
				model.bipedHead.render(0.0625F);
				model.bipedHeadwear.render(0.0625F);
			}
			if ((bitFlags & 15) == 15) { // Woo black box code copied from a class
				float f8 = entity.ticksExisted + 0.0625F;
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
					model.bipedHead.render(0.0625F);
					model.bipedHeadwear.render(0.0625F);
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
		}
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		EntityPlayer entityPlayer = ((EntityVRHead)entity).getEntityPlayer();
		return entityPlayer == null ? AbstractClientPlayer.locationStevePng : ((AbstractClientPlayer)entityPlayer).getLocationSkin();
	}
}
