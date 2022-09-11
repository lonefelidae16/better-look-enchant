package me.lonefelidae16.betterlookenchant.compat.modmenu.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IColorTint;
import me.lonefelidae16.betterlookenchant.gui.Color;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClickableWidget.class)
public abstract class MixinClickableWidget extends DrawableHelper implements Drawable, Element, Selectable {
    @Redirect(
            method = "renderButton(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V")
    )
    private void betterLookEnchant$setShaderColor(float red, float green, float blue, float alpha) {
        var $this = ClickableWidget.class.cast(this);
        if ($this instanceof IColorTint iColorTint) {
            Color color = iColorTint.getBackgroundColor();
            RenderSystem.setShaderColor(color.red() / 255.0F, color.green() / 255.0F, color.blue() / 255.0F, alpha);
        } else {
            RenderSystem.setShaderColor(red, green, blue, alpha);
        }
    }

    @Redirect(
            method = "renderButton(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ClickableWidget;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"
            )
    )
    private void betterLookEnchant$drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int x, int y, int c) {
        var $this = ClickableWidget.class.cast(this);
        if ($this instanceof IColorTint iColorTint) {
            Color color = iColorTint.getForegroundColor();
            drawCenteredText(matrices, textRenderer, text, x, y, color.argb());
        } else {
            drawCenteredText(matrices, textRenderer, text, x, y, c);
        }
    }
}
