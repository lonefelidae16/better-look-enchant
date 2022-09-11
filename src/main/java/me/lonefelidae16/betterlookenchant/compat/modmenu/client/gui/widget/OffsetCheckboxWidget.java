package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IOffsetElement;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class OffsetCheckboxWidget extends CheckboxWidget implements IOffsetElement {
    private final int offsetX;
    private final int offsetY;
    private Consumer<Boolean> changedListener;

    public OffsetCheckboxWidget(int offsetX, int offsetY, int width, int height, Text message, boolean checked) {
        super(offsetX, offsetY, width, height, message, checked);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void render(int x, int y, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.x = x + this.offsetX;
        this.y = y + this.offsetY;
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.changedListener.accept(this.isChecked());
    }

    public void setCheckedChangedListener(Consumer<Boolean> changedListener) {
        this.changedListener = changedListener;
    }
}
