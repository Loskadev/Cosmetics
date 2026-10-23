package com.kosmo;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** Какие игроки в этом кадре держат меч с включённой косметикой (id сущности). */
public final class SwordHold {
	public static final Set<Integer> IDS = ConcurrentHashMap.newKeySet();

	private SwordHold() {
	}
}
