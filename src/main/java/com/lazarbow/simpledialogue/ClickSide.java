package com.lazarbow.simpledialogue;

public enum ClickSide {
    LEFT,
    RIGHT;

    public static ClickSide parse(String input) {
        if (input == null) {
            return null;
        }

        return switch (input.toLowerCase()) {
            case "l", "left", "left_click" -> LEFT;
            case "r", "right", "right_click" -> RIGHT;
            default -> null;
        };
    }
}
