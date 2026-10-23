package com.kosmo.mixin;

import com.kosmo.Category;
import com.kosmo.Selection;
import com.kosmo.SwordHold;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Если выбран скин меча и игрок держит меч, прячет обычный меч в руке (рисуем свой). */
@Mixin(ArmedEntityRenderState.class)
public class ArmedStateMixin {
	@Inject(method = "updateRenderState", at = @At("TAIL"))
	private static void kosmo$hideSword(LivingEntity entity, ArmedEntityRenderState state, ItemModelManager itemModelManager, CallbackInfo ci) {
		if (entity != MinecraftClient.getInstance().player) {
			return;
		}
		ItemStack held = entity.getMainHandStack();
		if (Selection.get(Category.SWORD) != null && held.isIn(ItemTags.SWORDS)) {
			SwordHold.IDS.add(entity.getId());
			if (entity.getMainArm() == Arm.LEFT) {
				state.leftHandItemState.clear();
			} else {
				state.rightHandItemState.clear();
			}
		} else {
			SwordHold.IDS.remove(entity.getId());
		}
	}
}
