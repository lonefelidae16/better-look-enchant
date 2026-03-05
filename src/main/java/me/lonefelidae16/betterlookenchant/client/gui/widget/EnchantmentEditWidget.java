package me.lonefelidae16.betterlookenchant.client.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class EnchantmentEditWidget extends TextFieldWidget {
    private SubmitListener listener;

    EnchantmentEditWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
        this.visible = false;
    }

    static class Builder {
        private final TextRenderer renderer;
        private int x;
        private int y;
        private int width = 110;
        private int height = 20;
        private SubmitListener listener;

        Builder(TextRenderer renderer, int x, int y) {
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
            var result = new EnchantmentEditWidget(this.renderer, this.x, this.y, this.width, this.height, Text.empty());
            if (this.listener != null) {
                result.setSubmitListener(this.listener);
            }
            return result;
        }
    }

    public static Builder builder(TextRenderer renderer, int x, int y) {
        return new Builder(renderer, x, y);
    }

    public void setSubmitListener(SubmitListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.getKeycode() == GLFW.GLFW_KEY_ENTER) {
            if (this.listener != null) {
                this.listener.onSubmit(this);
                return true;
            }
        } else if (input.getKeycode() == GLFW.GLFW_KEY_ESCAPE) {
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
