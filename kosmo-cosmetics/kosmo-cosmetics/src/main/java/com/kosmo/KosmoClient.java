package com.kosmo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KosmoClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("kosmo");
	public static KeyBinding openMenu;

	@Override
	public void onInitializeClient() {
		Selection.load();

		openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.kosmo.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.kosmo"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openMenu.wasPressed()) {
				if (client.currentScreen == null && client.player != null) {
					client.setScreen(new CosmeticsScreen(null));
				}
			}
		});

		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, helper, context) -> {
			if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
				helper.register(new CosmeticsFeature(playerRenderer));
			}
		});
	}
}
