package com.techjar.vivecraftforge.handler;

import org.lwjgl.opengl.GL11;

import com.techjar.vivecraftforge.proxy.ProxyClient;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;

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
	public void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
		if (Util.isVRPlayer(event.entityPlayer)) {
			VRPlayerData data = ProxyClient.vrPlayerIds.get(event.entityPlayer.getEntityId());
			ModelBiped model = event.renderer.modelBipedMain;
			model.bipedHead.showModel = false;
			model.bipedLeftArm.showModel = data.seated;
			model.bipedRightArm.showModel = data.seated;
			model.bipedHeadwear.showModel = false;
			event.entityPlayer.ignoreFrustumCheck = true;
		}
	}
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		if (Util.isVRPlayer(event.entityPlayer)) {
			ModelBiped model = event.renderer.modelBipedMain;
			model.bipedHead.showModel = true;
			model.bipedLeftArm.showModel = true;
			model.bipedRightArm.showModel = true;
			model.bipedHeadwear.showModel = true;
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderPlayerSpecials(RenderPlayerEvent.Specials.Pre event) {
		if (Util.isVRPlayer(event.entityPlayer)) {
			VRPlayerData data = ProxyClient.vrPlayerIds.get(event.entityPlayer.getEntityId());
			event.renderHelmet = false;
			event.renderItem = data.seated;
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderPlayerSetArmorModel(RenderPlayerEvent.SetArmorModel event) {
		if (!Util.isVRPlayer(event.entityPlayer)) return;
		if (event.stack != null) {
			Item item = event.stack.getItem();

			if (item instanceof ItemArmor) {
				VRPlayerData data = ProxyClient.vrPlayerIds.get(event.entityPlayer.getEntityId());
				ItemArmor itemarmor = (ItemArmor)item;
				((RenderPlayer)event.renderer).bindTexture(RenderBiped.getArmorResource(event.entityPlayer, event.stack, 3 - event.slot, null));
				ModelBiped modelbiped = event.slot == 1 ? ((RenderPlayer)event.renderer).modelArmor : ((RenderPlayer)event.renderer).modelArmorChestplate;
				modelbiped.bipedHead.showModel = event.slot == 3;
				modelbiped.bipedHeadwear.showModel = event.slot == 3;
				modelbiped.bipedBody.showModel = event.slot == 2 || event.slot == 1;
				modelbiped.bipedRightArm.showModel = event.slot == 2;
				modelbiped.bipedLeftArm.showModel = event.slot == 2;
				modelbiped.bipedRightLeg.showModel = event.slot == 1 || event.slot == 0;
				modelbiped.bipedLeftLeg.showModel = event.slot == 1 || event.slot == 0;
				modelbiped = net.minecraftforge.client.ForgeHooksClient.getArmorModel(event.entityPlayer, event.stack, 3 - event.slot, modelbiped);
				modelbiped.bipedHead.showModel = false;
				modelbiped.bipedHeadwear.showModel = false;
				modelbiped.bipedRightArm.showModel &= data.seated;
				modelbiped.bipedLeftArm.showModel &= data.seated;
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
						return;
					}

					event.result = 16;
					return;
				}

				GL11.glColor3f(1.0F, 1.0F, 1.0F);

				if (event.stack.isItemEnchanted()) {
					event.result = 15;
					return;
				}

				event.result = 1;
				return;
			}
		}
		event.result = -2;
	}
}
