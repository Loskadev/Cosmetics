package com.kosmo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

/** Рисует выбранную косметику на локальном игроке. */
public class CosmeticsFeature extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {
	private static final Identifier WHITE = Identifier.of("kosmo", "textures/misc/white.png");

	public CosmeticsFeature(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
			PlayerEntityRenderState state, float limbAngle, float limbDistance) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.player == null || state.id != mc.player.getId() || state.invisible || state.spectator) {
			return;
		}
		PlayerEntityModel model = getContextModel();
		VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(WHITE));

		part(matrices, vc, light, Selection.get(Category.HEAD), model.head);
		part(matrices, vc, light, Selection.get(Category.BACK), model.body);
		part(matrices, vc, light, Selection.get(Category.SHOULDERS), model.body);

		Cosmetic sword = Selection.get(Category.SWORD);
		if (sword != null && SwordHold.IDS.contains(state.id)) {
			boolean left = state.mainArm == Arm.LEFT;
			ModelPart arm = left ? model.leftArm : model.rightArm;
			matrices.push();
			arm.rotate(matrices);
			// кисть: низ руки, по центру
			matrices.translate(left ? 1f / 16f : -1f / 16f, 10f / 16f, 0f);
			// клинок вперёд и немного вверх, плоскость клинка вертикально
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-125f));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90f));
			// рука держит середину рукояти
			matrices.translate(0f, 1.5f / 16f, 0f);
			CosmeticRenderer.draw(matrices, vc, sword, light);
			matrices.pop();
		}
	}

	private void part(MatrixStack matrices, VertexConsumer vc, int light, Cosmetic c, ModelPart part) {
		if (c == null) {
			return;
		}
		matrices.push();
		part.rotate(matrices);
		CosmeticRenderer.draw(matrices, vc, c, light);
		matrices.pop();
	}
}
