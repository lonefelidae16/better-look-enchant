package me.lonefelidae16.betterlookenchant.client.gui.widget;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

/**
 * Base class of ElementListWidget
 *
 * @param <E> its own class that extended CustomElementListWidgetBase.EntryBase
 */
public abstract class CustomElementListWidgetBase<E extends CustomElementListWidgetBase.EntryBase<E>> extends ContainerObjectSelectionList<E> {
    public CustomElementListWidgetBase(Minecraft client, int width, int height, int y, int itemHeight) {
        super(client, width, height, y, itemHeight);
    }

    public static abstract class EntryBase<E extends ContainerObjectSelectionList.Entry<E>> extends ContainerObjectSelectionList.Entry<E> {
        protected final List<AbstractWidget> elements;

        protected EntryBase() {
            super();
            this.elements = new ArrayList<>();
        }

        protected <T extends AbstractWidget> T addElement(T element) {
            this.elements.add(element);
            return element;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return this.elements;
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return this.elements;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            for (AbstractWidget widget : this.elements) {
                widget.setY(this.getY());
                widget.extractRenderState(context, mouseX, mouseY, deltaTicks);
            }
        }
    }
}
