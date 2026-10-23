package com.kosmo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/** Меню косметики. Открывается правым Shift. */
public class CosmeticsScreen extends Screen {
	private Category tab;
	private final boolean holdsSword;

	public CosmeticsScreen(Category tab) {
		super(Text.literal("Косметика"));
		MinecraftClient mc = MinecraftClient.getInstance();
		ItemStack held = mc.player == null ? ItemStack.EMPTY : mc.player.getMainHandStack();
		this.holdsSword = held.isIn(ItemTags.SWORDS);
		this.tab = (tab == Category.SWORD && !holdsSword) || tab == null ? Category.HEAD : tab;
	}

	private List<Category> tabs() {
		List<Category> t = new ArrayList<>(List.of(Category.HEAD, Category.BACK, Category.SHOULDERS));
		if (holdsSword) {
			t.add(Category.SWORD);
		}
		return t;
	}

	@Override
	protected void init() {
		int x = 20;
		int tabW = 78;
		int i = 0;
		for (Category c : tabs()) {
			ButtonWidget b = ButtonWidget.builder(Text.literal(c.title), btn -> client.setScreen(new CosmeticsScreen(c)))
					.dimensions(x + i * (tabW + 4), 36, tabW, 20).build();
			b.active = c != tab;
			addDrawableChild(b);
			i++;
		}

		int y = 66;
		Cosmetic current = Selection.get(tab);
		addDrawableChild(ButtonWidget.builder(Text.literal((current == null ? "> " : "") + "Нет"), btn -> {
			Selection.set(tab, null);
			client.setScreen(new CosmeticsScreen(tab));
		}).dimensions(x, y, 170, 20).build());
		y += 24;
		for (Cosmetic c : Cosmetics.of(tab)) {
			boolean on = current != null && current.id().equals(c.id());
			addDrawableChild(ButtonWidget.builder(Text.literal((on ? "> " : "") + c.name()), btn -> {
				Selection.set(tab, c.id());
				client.setScreen(new CosmeticsScreen(tab));
			}).dimensions(x, y, 170, 20).build());
			y += 24;
		}

		addDrawableChild(ButtonWidget.builder(Text.literal("Закрыть"), btn -> close())
				.dimensions(x, height - 30, 170, 20).build());
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Косметика"), width / 2, 12, 0xFFFFFF);
		context.drawTextWithShadow(textRenderer, Text.literal(tab.title), 20, 22, 0xAAAAAA);

		int px1 = Math.max(210, width - 240);
		int px2 = width - 20;
		int py1 = 40;
		int py2 = height - 40;
		if (px2 - px1 > 60 && client.player != null) {
			context.fill(px1, py1, px2, py2, 0x66000000);
			InventoryScreen.drawEntity(context, px1, py1, px2, py2, 70, 0.0625F, mouseX, mouseY, client.player);
		}
		if (!holdsSword) {
			context.drawTextWithShadow(textRenderer, Text.literal("Возьмите меч в руку, чтобы открыть вкладку «Меч»"), 20, height - 46, 0x888888);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
