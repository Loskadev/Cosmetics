package com.kosmo;

public enum Category {
	HEAD("Голова"),
	BACK("Спина"),
	SHOULDERS("Плечи"),
	SWORD("Меч");

	public final String title;

	Category(String title) {
		this.title = title;
	}
}
