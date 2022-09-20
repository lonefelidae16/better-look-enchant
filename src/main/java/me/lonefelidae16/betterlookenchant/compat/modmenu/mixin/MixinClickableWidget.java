package me.lonefelidae16.betterlookenchant.compat.modmenu.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.IColorTint;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClickableWidget.class)
public abstract class MixinClickableWidget extends DrawableHelper implements Drawable, Element, Selectable {
    /**
     * enable background color tint which implements IColorTint
     *
     * @param red   original red
     * @param green original green
     * @param blue  original blue
     * @param alpha original alpha
     */
    @Redirect(
            method = "renderButton(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V")
    )
    private void betterLookEnchant$setShaderColor(float red, float green, float blue, float alpha) {
        var $this = ClickableWidget.class.cast(this);
        if ($this instanceof IColorTint iColorTint) {
            int color = iColorTint.getBackgroundColor();
            float r = (color >> 16 & 0xff) / 255.0F;
            float g = (color >> 8 & 0xff) / 255.0F;
            float b = (color & 0xff) / 255.0F;
            RenderSystem.setShaderColor(r, g, b, alpha);
        } else {
            RenderSystem.setShaderColor(red, green, blue, alpha);
        }
    }
}
