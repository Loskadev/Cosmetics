package com.kosmo;

import java.util.ArrayList;
import java.util.List;

/**
 * Все предметы косметики. Координаты:
 * голова, спина, плечи: x вправо, y вверх от пяток (голова 24..32), z вперёд (лицо при z=12) для головы и плеч,
 * для спины z растёт назад от тела (спина при z=10).
 * меч: начало в середине рукояти, клинок вдоль +y.
 */
public final class Cosmetics {
	public static final List<Cosmetic> ALL = new ArrayList<>();

	private Cosmetics() {
	}

	private static Box b(double x0, double y0, double z0, double x1, double y1, double z1, int rgb) {
		return new Box((float) x0, (float) y0, (float) z0, (float) x1, (float) y1, (float) z1, rgb);
	}

	private static void add(String id, String name, Category c, List<Box> boxes, boolean glow) {
		ALL.add(new Cosmetic(id, name, c, boxes, glow));
	}

	public static List<Cosmetic> of(Category c) {
		List<Cosmetic> out = new ArrayList<>();
		for (Cosmetic x : ALL) {
			if (x.category() == c) {
				out.add(x);
			}
		}
		return out;
	}

	public static Cosmetic byId(String id) {
		for (Cosmetic x : ALL) {
			if (x.id().equals(id)) {
				return x;
			}
		}
		return null;
	}

	static {
		final int DRED = 0x8C141E, RED = 0xD2232D, GOLD = 0xF5CD37, GOLD2 = 0xCD9B23, GEM = 0x468CEB, HAIR = 0x30283A, PINK = 0xEB96AA;

		// ---------- ГОЛОВА ----------
		List<Box> horns = new ArrayList<>();
		double[][] hs = {{4.2, -1}, {10.2, 1}};
		for (double[] h : hs) {
			double sx = h[0], dx = h[1];
			horns.add(b(sx, 32.3, 6, sx + 1.6, 33.7, 7.8, DRED));
			horns.add(b(sx + dx * 0.6, 33.7, 6.1, sx + dx * 0.6 + 1.4, 35.5, 7.7, RED));
			horns.add(b(sx + dx * 1.2, 35.5, 6.3, sx + dx * 1.2 + 1.2, 37.1, 7.5, RED));
		}
		add("horns", "Рога", Category.HEAD, horns, false);

		add("cat_ears", "Кошачьи уши", Category.HEAD, List.of(
				b(4, 32.3, 6.5, 7.5, 35.6, 9.5, HAIR), b(8.5, 32.3, 6.5, 12, 35.6, 9.5, HAIR),
				b(4.7, 32.4, 7.5, 6.8, 34.4, 8.6, PINK), b(9.2, 32.4, 7.5, 11.3, 34.4, 8.6, PINK)), false);

		List<Box> crown = new ArrayList<>();
		crown.add(b(3.6, 32.3, 3.6, 12.4, 33.5, 12.4, GOLD));
		double[][] sp = {{3.6, 3.6}, {11.2, 3.6}, {3.6, 11.2}, {11.2, 11.2}, {7.4, 3.6}, {7.4, 11.2}, {3.6, 7.4}, {11.2, 7.4}};
		for (double[] p : sp) {
			crown.add(b(p[0], 33.5, p[1], p[0] + 1.2, 35.7, p[1] + 1.2, GOLD2));
		}
		crown.add(b(7.2, 33.5, 12.2, 8.8, 34.9, 12.8, GEM));
		add("crown", "Корона", Category.HEAD, crown, false);

		List<Box> halo = new ArrayList<>();
		double[][] hp = {{5, 5}, {7, 4}, {9, 4}, {11, 5}, {12, 7}, {12, 9}, {11, 11}, {9, 12}, {7, 12}, {5, 11}, {4, 9}, {4, 7}};
		for (double[] p : hp) {
			halo.add(b(p[0] - 0.8, 36, p[1] - 0.8, p[0] + 0.8, 36.8, p[1] + 0.8, 0xFFEB78));
		}
		add("halo", "Нимб", Category.HEAD, halo, true);

		add("tophat", "Цилиндр", Category.HEAD, List.of(
				b(2.5, 32.3, 2.5, 13.5, 33.1, 13.5, 0x1E1E24), b(4.5, 33.1, 4.5, 11.5, 39, 11.5, 0x1E1E24),
				b(4.4, 33.1, 4.4, 11.6, 34.6, 11.6, 0xAA1E32)), false);

		// ---------- СПИНА ----------
		add("cape", "Красный плащ", Category.BACK, List.of(
				b(4, 6, 10, 12, 24.4, 11, 0x961929), b(4, 24.4, 10, 12, 25.4, 11.3, 0xD74659), b(4, 6, 10, 12, 7, 11, 0x640F1E)), false);
		add("wings_angel", "Крылья ангела", Category.BACK, wings(0xFAFAFF, 0xC8D7F0), true);
		add("wings_demon", "Крылья демона", Category.BACK, wings(0x5A0F1E, 0x280A14), false);
		add("backpack", "Рюкзак", Category.BACK, List.of(
				b(4.5, 12, 10, 11.5, 23, 14, 0x785028), b(5, 18, 14, 11, 22, 14.6, 0x966937),
				b(4.5, 23, 10, 11.5, 24.4, 14, 0x5A3C1E), b(6.5, 14, 14.6, 9.5, 17, 15, 0xCDA03C)), false);
		List<Box> tail = new ArrayList<>();
		double[][] tp = {{7, 11, 10}, {7, 10, 12}, {7, 9.5, 14}, {7, 10.5, 16}, {7, 12.5, 17.5}, {7, 15, 18}};
		for (int i = 0; i < tp.length; i++) {
			tail.add(b(tp[i][0], tp[i][1], tp[i][2], tp[i][0] + 2, tp[i][1] + 2, tp[i][2] + 2, i < 5 ? HAIR : PINK));
		}
		add("tail", "Кошачий хвост", Category.BACK, tail, false);

		// ---------- ПЛЕЧИ ----------
		add("pads", "Наплечники", Category.SHOULDERS, List.of(
				b(-1, 24, 5.5, 4, 26.2, 10.5, 0x96A0AF), b(12, 24, 5.5, 17, 26.2, 10.5, 0x96A0AF),
				b(-1, 26.2, 6.5, 3, 27.4, 9.5, 0x6E788A), b(13, 26.2, 6.5, 17, 27.4, 9.5, 0x6E788A)), false);
		add("parrot", "Попугай", Category.SHOULDERS, List.of(
				b(12.5, 24, 7, 15, 27.5, 9.5, 0xDC2832), b(12.8, 27.5, 7.2, 15, 30, 9.3, 0xDC2832),
				b(15, 28.5, 7.8, 16.6, 29.5, 8.6, 0xF0BE28), b(12.8, 24, 9.5, 14.6, 24.8, 11.5, 0x3C82DC),
				b(13, 29, 9.3, 13.8, 29.8, 9.5, 0x141414)), false);
		add("slime", "Слайм", Category.SHOULDERS, List.of(
				b(12, 24, 6.5, 16, 28, 10.5, 0x5AC85A), b(12.6, 24.6, 10.5, 13.6, 25.6, 10.7, 0x1E5A1E),
				b(14.4, 24.6, 10.5, 15.4, 25.6, 10.7, 0x1E5A1E), b(13.2, 24.2, 10.5, 14.8, 24.6, 10.7, 0x286E28)), false);
		add("dragon", "Дракончик", Category.SHOULDERS, List.of(
				b(12.5, 24, 7, 15.5, 27, 10, 0x28965A), b(12.8, 27, 7.5, 15.3, 29.4, 10, 0x28965A),
				b(15.3, 27.8, 8.2, 17, 28.8, 9.3, 0x28965A), b(10.5, 25.5, 8, 12.5, 28, 8.4, 0xDCC846),
				b(14.5, 28.8, 9.9, 15, 29.2, 10.2, 0xDC2828), b(12.5, 24, 6, 15, 25, 7, 0x1E6E41)), false);
		add("epaulets", "Эполеты", Category.SHOULDERS, List.of(
				b(-0.8, 24, 5.8, 4, 25, 10.2, GOLD), b(12, 24, 5.8, 16.8, 25, 10.2, GOLD),
				b(-0.8, 25, 5.8, 0.6, 25.8, 10.2, GOLD2), b(15.4, 25, 5.8, 16.8, 25.8, 10.2, GOLD2),
				b(-0.8, 23, 9.4, 0.4, 24, 10.2, GOLD2), b(15.6, 23, 9.4, 16.8, 24, 10.2, GOLD2)), false);

		// ---------- МЕЧ ----------
		final int STEEL = 0xC3CDD7, STEEL2 = 0x78828F, DARK = 0x2D2D37, ORANGE = 0xD65A1E;
		add("knife", "Тактический нож", Category.SWORD, List.of(
				b(-0.9, -4, -0.9, 0.9, 0, 0.9, DARK), b(-1.1, -4.6, -1.1, 1.1, -4, 1.1, ORANGE), b(-2.4, 0, -1, 2.4, 1, 1, ORANGE),
				b(-1.1, 1, -0.5, 1.1, 8, 0.5, STEEL), b(0.7, 1, -0.55, 1.1, 8, 0.55, STEEL2),
				b(-0.9, 8, -0.5, 0.9, 10, 0.5, STEEL2), b(-0.5, 10, -0.4, 0.5, 11.2, 0.4, STEEL)), false);

		List<Box> curved = new ArrayList<>();
		curved.add(b(-0.9, -4, -0.9, 0.9, 0, 0.9, DARK));
		curved.add(b(-1.5, -4.8, -0.6, 1.5, -4, 0.6, ORANGE));
		curved.add(b(-2.4, 0, -1, 2.4, 1, 1, ORANGE));
		for (int i = 0; i < 8; i++) {
			double off = 0.1 * i * i, w = 1.0 - 0.05 * i, y = 1 + i * 1.2;
			curved.add(b(off - w, y, -0.45, off + w, y + 1.3, 0.45, i % 2 == 0 ? STEEL : STEEL2));
		}
		add("curved", "Изогнутый нож", Category.SWORD, curved, false);

		List<Box> katana = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			katana.add(b(-0.8, -5 + i, -0.8, 0.8, -4 + i, 0.8, i % 2 == 0 ? DARK : 0xAA1928));
		}
		katana.add(b(-1.6, 0, -1.6, 1.6, 0.8, 1.6, GOLD));
		katana.add(b(-0.9, -5.8, -0.9, 0.9, -5, 0.9, GOLD));
		for (int i = 0; i < 7; i++) {
			katana.add(b(-0.55, 0.8 + i * 2.2, -0.35, 0.55, 3.0 + i * 2.2, 0.35, i % 2 == 0 ? 0xE1E6F0 : 0x96A0AF));
		}
		katana.add(b(-0.35, 16.2, -0.3, 0.35, 17.7, 0.3, 0xE1E6F0));
		add("katana", "Катана", Category.SWORD, katana, false);

		List<Box> fire = new ArrayList<>();
		fire.add(b(-0.9, -4, -0.9, 0.9, 0, 0.9, 0x3C1E19));
		fire.add(b(-2.4, 0, -1, 2.4, 1, 1, 0x781E14));
		for (int i = 0; i < 9; i++) {
			fire.add(b(-1.0, 1 + i * 1.3, -0.5, 1.0, 2.4 + i * 1.3, 0.5, i % 2 == 0 ? 0xFF9620 : 0xFFDC50));
		}
		fire.add(b(1.0, 3, -0.4, 1.8, 4.2, 0.4, 0xFFC83C));
		fire.add(b(1.0, 6.5, -0.4, 1.8, 7.8, 0.4, 0xFF7820));
		fire.add(b(-1.8, 5, -0.4, -1.0, 6.4, 0.4, 0xFFDC50));
		fire.add(b(-1.8, 9, -0.4, -1.0, 10.3, 0.4, 0xFF7820));
		fire.add(b(-0.5, 13, -0.4, 0.5, 14.6, 0.4, 0xFFC83C));
		add("fire", "Огненный клинок", Category.SWORD, fire, true);

		add("energy", "Энергетический меч", Category.SWORD, List.of(
				b(-1, -5, -1, 1, 0, 1, 0x464E5C), b(-1.4, 0, -1.4, 1.4, 1, 1.4, 0x646E7D),
				b(-1, 1, -0.8, 1, 14, 0.8, 0x78E6FF), b(-0.5, 1, -0.85, 0.5, 14.2, 0.85, 0xD2FAFF),
				b(-0.7, 14, -0.6, 0.7, 14.8, 0.6, 0xD2FAFF)), true);
	}

	private static List<Box> wings(int col, int tip) {
		List<Box> w = new ArrayList<>();
		for (int side = -1; side <= 1; side += 2) {
			for (int i = 0; i < 5; i++) {
				double y0 = 24 - i * 3.2, ln = 14 - i * 1.8;
				int c = i % 2 == 0 ? col : tip;
				if (side < 0) {
					w.add(b(4 - ln, y0 - 3, 10.5, 4, y0, 11.3, c));
				} else {
					w.add(b(12, y0 - 3, 10.5, 12 + ln, y0, 11.3, c));
				}
			}
		}
		return w;
	}
}
