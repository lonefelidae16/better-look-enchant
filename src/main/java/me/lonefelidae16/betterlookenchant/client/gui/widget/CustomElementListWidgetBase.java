package me.lonefelidae16.betterlookenchant.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class of ElementListWidget
 *
 * @param <E> its own class that extended CustomElementListWidgetBase.EntryBase
 */
public abstract class CustomElementListWidgetBase<E extends CustomElementListWidgetBase.EntryBase<E>> extends ElementListWidget<E> {
    public CustomElementListWidgetBase(MinecraftClient client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
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
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            for (ClickableWidget widget : this.elements) {
                widget.setY(this.getY());
                widget.render(context, mouseX, mouseY, deltaTicks);
            }
        }
    }
}
