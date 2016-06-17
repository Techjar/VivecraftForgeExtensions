package com.techjar.vivecraftforge.handler;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class HandlerRenderEvent {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPlayer(RenderPlayerEvent.Pre event) {
		ModelBiped model = event.renderer.modelBipedMain;
		model.bipedHead.showModel = false;
		model.bipedLeftArm.showModel = false;
		model.bipedRightArm.showModel = false;
		model.bipedHeadwear.showModel = false;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPlayerSpecials(RenderPlayerEvent.Specials.Pre event) {
		event.renderHelmet = false;
		event.renderItem = false;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPlayerSetArmorModel(RenderPlayerEvent.SetArmorModel event) {
		if (event.stack != null) {
			Item item = event.stack.getItem();

			if (item instanceof ItemArmor) {
				ItemArmor itemarmor = (ItemArmor)item;
				((RenderPlayer)event.renderer).bindTexture(RenderBiped.getArmorResource(event.entityPlayer, event.stack, 3 - event.slot, null));
				ModelBiped modelbiped = event.slot == 1 ? ((RenderPlayer)event.renderer).modelArmor : ((RenderPlayer)event.renderer).modelArmorChestplate;
				modelbiped.bipedHead.showModel = /*event.slot == 3*/ false;
				modelbiped.bipedHeadwear.showModel = /*event.slot == 3*/ false;
				modelbiped.bipedBody.showModel = event.slot == 2 || event.slot == 1;
				modelbiped.bipedRightArm.showModel = /*event.slot == 2*/ false;
				modelbiped.bipedLeftArm.showModel = /*event.slot == 2*/ false;
				modelbiped.bipedRightLeg.showModel = event.slot == 1 || event.slot == 0;
				modelbiped.bipedLeftLeg.showModel = event.slot == 1 || event.slot == 0;
				modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(event.entityPlayer, event.stack, 3 - event.slot, modelbiped);
				event.renderer.setRenderPassModel(modelbiped);
				modelbiped.onGround = ((RenderPlayer)event.renderer).mainModel.onGround;
				modelbiped.isRiding = ((RenderPlayer)event.renderer).mainModel.isRiding;
				modelbiped.isChild = ((RenderPlayer)event.renderer).mainModel.isChild;

				// Move outside if to allow for more then just CLOTH
				int j = itemarmor.getColor(event.stack);
				if (j != -1) {
					float f1 = (float) (j >> 16 & 255) / 255.0F;
					float f2 = (float) (j >> 8 & 255) / 255.0F;
					float f3 = (float) (j & 255) / 255.0F;
					GL11.glColor3f(f1, f2, f3);

					if (event.stack.isItemEnchanted()) {
						event.result = 31;
					}

					event.result = 16;
				}

				GL11.glColor3f(1.0F, 1.0F, 1.0F);

				if (event.stack.isItemEnchanted()) {
					event.result = 15;
				}

				event.result = 1;
			}
		}
	}
}
