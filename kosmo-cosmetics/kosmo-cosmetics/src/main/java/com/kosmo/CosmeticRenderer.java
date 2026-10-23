package com.kosmo;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/** Рисует кубики косметики в текущей системе координат MatrixStack. */
public final class CosmeticRenderer {
	private static final int FULL_BRIGHT = 0xF000F0;

	private CosmeticRenderer() {
	}

	public static void draw(MatrixStack matrices, VertexConsumer vc, Cosmetic c, int light) {
		int lt = c.glow() ? FULL_BRIGHT : light;
		MatrixStack.Entry e = matrices.peek();
		for (Box b : c.boxes()) {
			drawBox(e, vc, b, lt, c.category());
		}
	}

	/** Из пикселей модели в координаты части тела (блоки, y вниз, лицо к -z). */
	private static float mx(Category c, float px) {
		return c == Category.SWORD ? px / 16f : (px - 8f) / 16f;
	}

	private static float my(Category c, float py) {
		return c == Category.SWORD ? py / 16f : (24f - py) / 16f;
	}

	private static float mz(Category c, float pz) {
		if (c == Category.SWORD) {
			return pz / 16f;
		}
		return c == Category.BACK ? (pz - 8f) / 16f : -(pz - 8f) / 16f;
	}

	private static void drawBox(MatrixStack.Entry e, VertexConsumer vc, Box b, int light, Category cat) {
		float x0 = b.x0(), y0 = b.y0(), z0 = b.z0(), x1 = b.x1(), y1 = b.y1(), z1 = b.z1();
		// вершины по 4 на грань, нормаль, яркость грани
		float[][][] faces = {
				{{x0, y1, z0}, {x1, y1, z0}, {x1, y1, z1}, {x0, y1, z1}}, // верх
				{{x0, y0, z1}, {x1, y0, z1}, {x1, y0, z0}, {x0, y0, z0}}, // низ
				{{x1, y0, z0}, {x1, y1, z0}, {x1, y1, z1}, {x1, y0, z1}}, // +x
				{{x0, y0, z1}, {x0, y1, z1}, {x0, y1, z0}, {x0, y0, z0}}, // -x
				{{x0, y0, z1}, {x0, y1, z1}, {x1, y1, z1}, {x1, y0, z1}}, // +z
				{{x1, y0, z0}, {x1, y1, z0}, {x0, y1, z0}, {x0, y0, z0}}  // -z
		};
		float[][] normals = {{0, 1, 0}, {0, -1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
		float[] shade = {1.0f, 0.55f, 0.78f, 0.78f, 0.88f, 0.88f};
		int baseR = (b.rgb() >> 16) & 0xFF, baseG = (b.rgb() >> 8) & 0xFF, baseB = b.rgb() & 0xFF;
		for (int f = 0; f < 6; f++) {
			float[] n = normals[f];
			float nx = n[0];
			float ny = cat == Category.SWORD ? n[1] : -n[1];
			float nz = cat == Category.SWORD ? n[2] : (cat == Category.BACK ? n[2] : -n[2]);
			int r = Math.min(255, Math.round(baseR * shade[f]));
			int g = Math.min(255, Math.round(baseG * shade[f]));
			int bl = Math.min(255, Math.round(baseB * shade[f]));
			for (float[] v : faces[f]) {
				vc.vertex(e, mx(cat, v[0]), my(cat, v[1]), mz(cat, v[2]))
						.color(r, g, bl, 255)
						.texture(0.5f, 0.5f)
						.overlay(OverlayTexture.DEFAULT_UV)
						.light(light)
						.normal(e, nx, ny, nz);
			}
		}
	}
}
