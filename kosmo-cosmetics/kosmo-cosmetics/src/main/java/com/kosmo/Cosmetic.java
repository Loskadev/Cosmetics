package com.kosmo;

import java.util.List;

public record Cosmetic(String id, String name, Category category, List<Box> boxes, boolean glow) {
}
