package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IOffsetElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class of EntryListWidget.Entry
 *
 * @param <E> its own class that extended this class
 */
public abstract class ListWidgetEntryBase<E extends EntryListWidget.Entry<E>> extends EntryListWidget.Entry<E> {
    protected final List<Drawable> drawableChildren;

    public ListWidgetEntryBase() {
        this.drawableChildren = new ArrayList<>();
    }

    protected <T extends Element & Drawable & Selectable> T addDrawableChild(T element) {
        this.drawableChildren.add(element);
        return element;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawableChildren.forEach(element -> {
            if (element instanceof IOffsetElement iOffsetElement) {
                iOffsetElement.render(x, y, matrices, mouseX, mouseY, tickDelta);
            } else {
                element.render(matrices, mouseX, mouseY, tickDelta);
            }
        });
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> element.isMouseOver(mouseX, mouseY));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> element.keyPressed(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> element.keyReleased(keyCode, scanCode, modifiers));
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> element.charTyped(chr, modifiers));
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> changeFocus(lookForwards));
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .forEach(element -> element.mouseMoved(mouseX, mouseY));
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> element.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> element.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.drawableChildren.stream()
                .filter(drawable -> (drawable instanceof Element))
                .map(Element.class::cast)
                .anyMatch(element -> element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
    }
}
