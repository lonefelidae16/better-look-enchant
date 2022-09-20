package me.lonefelidae16.betterlookenchant.gui;

import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TextFormat {
    private final Integer color;
    private final boolean isBold;
    private final boolean isItalic;
    private final boolean isUnderline;
    private final boolean isStrike;

    public static TextFormat EMPTY = new TextFormat(null, false, false, false, false);

    public TextFormat(int argb) {
        this(argb, false, false, false, false);
    }

    public TextFormat(@Nullable Integer color, boolean isBold, boolean isItalic, boolean isUnderline, boolean isStrike) {
        this.color = color;
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isUnderline = isUnderline;
        this.isStrike = isStrike;
    }

    @Nullable
    public Integer getColor() {
        return this.color;
    }

    public boolean isBold() {
        return this.isBold;
    }

    public boolean isItalic() {
        return this.isItalic;
    }

    public boolean isUnderline() {
        return this.isUnderline;
    }

    public boolean isStrike() {
        return this.isStrike;
    }

    public Style asStyle() {
        Style result = Style.EMPTY;
        if (this.color != null) {
            result = result.withColor(this.color);
        }
        return result
                .withBold(this.isBold)
                .withItalic(this.isItalic)
                .withUnderline(this.isUnderline)
                .withStrikethrough(this.isStrike);
    }

    public boolean isEmpty() {
        return this.equals(TextFormat.EMPTY);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TextFormat target)) {
            return false;
        }
        return Objects.equals(this.color, target.color) &&
                this.isBold == target.isBold &&
                this.isItalic == target.isItalic &&
                this.isUnderline == target.isUnderline &&
                this.isStrike == target.isStrike;
    }

    public TextFormat withColor(int color) {
        return new TextFormat(color, this.isBold, this.isItalic, this.isUnderline, this.isStrike);
    }

    public TextFormat withBold(boolean isBold) {
        return new TextFormat(this.color, isBold, this.isItalic, this.isUnderline, this.isStrike);
    }

    public TextFormat withItalic(boolean isItalic) {
        return new TextFormat(this.color, this.isBold, isItalic, this.isUnderline, this.isStrike);
    }

    public TextFormat withUnderline(boolean isUnderline) {
        return new TextFormat(this.color, this.isBold, this.isItalic, isUnderline, this.isStrike);
    }

    public TextFormat withStrike(boolean isStrike) {
        return new TextFormat(this.color, this.isBold, this.isItalic, this.isUnderline, isStrike);
    }
}
