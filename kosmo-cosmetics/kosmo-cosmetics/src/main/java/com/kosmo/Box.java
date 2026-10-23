package com.kosmo;

/** Параллелепипед в пикселях (1 блок = 16 пикселей) и его цвет 0xRRGGBB. */
public record Box(float x0, float y0, float z0, float x1, float y1, float z1, int rgb) {
}
