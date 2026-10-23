package com.kosmo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

/** Выбранная косметика по категориям. Хранится в config/kosmo-cosmetics.json. */
public final class Selection {
	private static final Map<Category, String> CHOSEN = new EnumMap<>(Category.class);
	private static final Gson GSON = new Gson();

	private Selection() {
	}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("kosmo-cosmetics.json");
	}

	public static Cosmetic get(Category c) {
		String id = CHOSEN.get(c);
		return id == null ? null : Cosmetics.byId(id);
	}

	public static void set(Category c, String idOrNull) {
		if (idOrNull == null) {
			CHOSEN.remove(c);
		} else {
			CHOSEN.put(c, idOrNull);
		}
		save();
	}

	public static void load() {
		try {
			Path f = file();
			if (!Files.exists(f)) {
				return;
			}
			JsonObject o = GSON.fromJson(Files.readString(f), JsonObject.class);
			for (Category c : Category.values()) {
				if (o != null && o.has(c.name())) {
					CHOSEN.put(c, o.get(c.name()).getAsString());
				}
			}
		} catch (Exception e) {
			KosmoClient.LOGGER.warn("Не удалось прочитать выбор косметики", e);
		}
	}

	private static void save() {
		try {
			JsonObject o = new JsonObject();
			for (Map.Entry<Category, String> e : CHOSEN.entrySet()) {
				o.addProperty(e.getKey().name(), e.getValue());
			}
			Files.writeString(file(), GSON.toJson(o));
		} catch (Exception e) {
			KosmoClient.LOGGER.warn("Не удалось сохранить выбор косметики", e);
		}
	}
}
