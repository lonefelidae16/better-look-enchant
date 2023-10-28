package me.lonefelidae16.betterlookenchant.client.gui.widget;

import me.lonefelidae16.betterlookenchant.client.gui.IOffsetElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base class of ElementListWidget
 *
 * @param <E> its own class that extended CustomElementListWidgetBase.EntryBase
 */
public abstract class CustomElementListWidgetBase<E extends CustomElementListWidgetBase.EntryBase<E>> extends ElementListWidget<E> {
    public CustomElementListWidgetBase(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    public static abstract class EntryBase<E extends ElementListWidget.Entry<E>> extends ElementListWidget.Entry<E> {
        protected final List<ClickableWidget> elements;

        protected EntryBase() {
            super();
            this.elements = new ArrayList<>();
        }

        protected <T extends ClickableWidget> T addElement(T element) {
            this.elements.add(element);
            return element;
        }

        @Override
        public List<? extends Element> children() {
            return this.elements;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.elements;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.elements.forEach(element -> {
                if (element instanceof IOffsetElement) {
                    IOffsetElement iOffsetElement = (IOffsetElement) element;
                    iOffsetElement.render(x, y, context, mouseX, mouseY, tickDelta);
                } else {
                    element.render(context, mouseX, mouseY, tickDelta);
                }
            });
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return this.elements.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(element -> element.isMouseOver(mouseX, mouseY));
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return this.elements.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(element -> element.keyPressed(keyCode, scanCode, modifiers));
        }

        @Override
        public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
            return this.elements.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(element -> element.keyReleased(keyCode, scanCode, modifiers));
        }

        @Override
        public boolean charTyped(char chr, int modifiers) {
            return this.elements.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(element -> element.charTyped(chr, modifiers));
        }

        @Override
        public void mouseMoved(double mouseX, double mouseY) {
            this.elements.stream()
                    .filter(Objects::nonNull)
                    .forEach(element -> element.mouseMoved(mouseX, mouseY));
            super.mouseMoved(mouseX, mouseY);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return this.elements.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(element -> element.mouseClicked(mouseX, mouseY, button));
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return this.elements.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(element -> element.mouseReleased(mouseX, mouseY, button));
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return this.elements.stream()
                    .filter(Objects::nonNull)
                    .anyMatch(element -> element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
        }

        @Override
        public void setFocused(@Nullable Element focused) {
            this.elements.stream()
                    .filter(Objects::nonNull)
                    .forEach(element -> element.setFocused(false));
            super.setFocused(focused);
        }
    }
}
