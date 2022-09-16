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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClickableWidget.class)
public abstract class MixinClickableWidget extends DrawableHelper implements Drawable, Element, Selectable {
    /**
     * enable background color tint which implements IColorTint
     *
     * @param red original red
     * @param green original green
     * @param blue original blue
     * @param alpha original alpha
     */
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

    /**
     * enable foreground color tint which implements IColorTint
     *
     * @param c original color
     * @return tinted color
     */
    @ModifyArg(
            method = "renderButton(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ClickableWidget;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"
            ),
            index = 5
    )
    private int betterLookEnchant$drawCenteredText_arg5(int c) {
        var $this = ClickableWidget.class.cast(this);
        if ($this instanceof IColorTint iColorTint) {
            Color color = iColorTint.getForegroundColor();
            return color.argb();
        } else {
            return c;
        }
    }
}
