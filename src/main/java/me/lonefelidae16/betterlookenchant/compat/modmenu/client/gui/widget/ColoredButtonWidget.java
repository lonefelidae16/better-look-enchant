package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IColorTint;
import me.lonefelidae16.betterlookenchant.gui.Color;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ColoredButtonWidget extends ButtonWidget implements IColorTint {
    private Color colorBg;
    private Color colorText;

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        this(x, y, width, height, message, onPress, EMPTY);
    }

    public ColoredButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message, onPress, tooltipSupplier);
        this.colorBg = Color.WHITE;
        this.colorText = Color.WHITE;
    }

    public void setBackgroundTint(int rgb) {
        this.colorBg = Color.fromARGB(rgb);
    }

    public void setTextColor(int rgb) {
        this.colorText = Color.fromARGB(rgb);
    }

    @Override
    public Color getForegroundColor() {
        return this.colorText;
    }

    @Override
    public Color getBackgroundColor() {
        return this.colorBg;
    }

//    @Override
//    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        MinecraftClient minecraftClient = MinecraftClient.getInstance();
//        TextRenderer textRenderer = minecraftClient.textRenderer;
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);
//        RenderSystem.setShaderColor(this.colorBg.red() / 255.0F, this.colorBg.green() / 255.0F, this.colorBg.blue() / 255.0F, this.alpha);
//        int i = this.getYImage(this.isHovered());
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.enableDepthTest();
//        this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
//        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
//        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
//        int textColor = this.active ? this.colorText.argb() : 0xA0A0A0;
//        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor | MathHelper.ceil(this.alpha * 255.0F) << 24);
//    }
}
