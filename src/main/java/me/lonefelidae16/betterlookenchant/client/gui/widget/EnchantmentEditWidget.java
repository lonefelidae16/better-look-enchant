package me.lonefelidae16.betterlookenchant.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class EnchantmentEditWidget extends EditBox {
    private SubmitListener listener;

    EnchantmentEditWidget(Font textRenderer, int x, int y, int width, int height, Component text) {
        super(textRenderer, x, y, width, height, text);
        this.visible = false;
    }

    static class Builder {
        private final Font renderer;
        private int x;
        private int y;
        private int width = 110;
        private int height = 20;
        private SubmitListener listener;

        Builder(Font renderer, int x, int y) {
            this.renderer = renderer;
            this.x = x;
            this.y = y;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder submitListener(SubmitListener listener) {
            this.listener = listener;
            return this;
        }

        public EnchantmentEditWidget build() {
            var result = new EnchantmentEditWidget(this.renderer, this.x, this.y, this.width, this.height, Component.empty());
            if (this.listener != null) {
                result.setSubmitListener(this.listener);
            }
            return result;
        }
    }

    public static Builder builder(Font renderer, int x, int y) {
        return new Builder(renderer, x, y);
    }

    public void setSubmitListener(SubmitListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (input.input() == GLFW.GLFW_KEY_ENTER) {
            if (this.listener != null) {
                this.listener.onSubmit(this);
                return true;
            }
        } else if (input.input() == GLFW.GLFW_KEY_ESCAPE) {
            if (this.listener != null) {
                this.listener.onCancel(this);
                return true;
            }
        }
        return super.keyPressed(input);
    }

    public interface SubmitListener {
        void onSubmit(EnchantmentEditWidget widget);

        void onCancel(EnchantmentEditWidget widget);
    }
}
