package me.lonefelidae16.betterlookenchant.gui;

public record Color(int argb) {
    public static final Color WHITE = fromGray(0xff);
    public static final Color MC_BLACK = fromGray(0x0);
    public static final Color MC_DARK_BLUE = fromRGB(0, 0, 0xaa);
    public static final Color MC_DARK_GREEN = fromRGB(0, 0xaa, 0);
    public static final Color MC_DARK_AQUA = fromRGB(0, 0xaa, 0xaa);
    public static final Color MC_DARK_RED = fromRGB(0xaa, 0, 0);
    public static final Color MC_DARK_PURPLE = fromRGB(0xaa, 0, 0xaa);
    public static final Color MC_GOLD = fromRGB(0xff, 0xaa, 0);
    public static final Color MC_GRAY = fromGray(0xaa);
    public static final Color MC_DARK_GRAY = fromGray(0x55);
    public static final Color MC_BLUE = fromRGB(0x55, 0x55, 0xff);
    public static final Color MC_GREEN = fromRGB(0x55, 0xff, 0x55);
    public static final Color MC_AQUA = fromRGB(0x55, 0xff, 0xff);
    public static final Color MC_RED = fromRGB(0xff, 0x55, 0x55);
    public static final Color MC_LIGHT_PURPLE = fromRGB(0xff, 0x55, 0xff);
    public static final Color MC_YELLOW = fromRGB(0xff, 0xff, 0x55);

    public static Color fromARGB(int argb) {
        return new Color(argb);
    }

    public static Color fromGray(int gray) {
        return fromARGB(0, gray, gray, gray);
    }

    public static Color fromGray(int gray, int alpha) {
        return fromARGB(alpha, gray, gray, gray);
    }

    public static Color fromRGB(int red, int green, int blue) {
        return fromARGB(0, red, green, blue);
    }

    public static Color fromARGB(int alpha, int red, int green, int blue) {
        return fromARGB(alpha << 24 | red << 16 | green << 8 | blue);
    }

    public static Color fromHexString(String hex) {
        if (hex == null) {
            return new Color(0);
        }
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (hex.length() == 3) {
            hex = String.format("%c%c%c%c%c%c", hex.charAt(0), hex.charAt(0), hex.charAt(1), hex.charAt(1), hex.charAt(2), hex.charAt(2));
        }
        if (!hex.startsWith("0x")) {
            hex = "0x" + hex;
        }
        int decoded = 0xFFFFFF;
        try {
            decoded = Integer.decode(hex);
        } catch (Throwable ignore) {
        }
        return Color.fromARGB(decoded);
    }

    public int alpha() {
        return argb >> 24 & 0xFF;
    }

    public int red() {
        return argb >> 16 & 0xFF;
    }

    public int green() {
        return argb >> 8 & 0xFF;
    }

    public int blue() {
        return argb & 0xFF;
    }

    public String asHexString() {
        return String.format("%02X%02X%02X", this.red(), this.green(), this.blue());
    }
}
