package me.lonefelidae16.betterlookenchant.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lonefelidae16.betterlookenchant.client.gui.IColorTint;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PressableWidget.class)
public abstract class MixinPressableWidget extends ClickableWidget {
    public MixinPressableWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

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
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0)
    )
    private void betterLookEnchant$setShaderColor(float red, float green, float blue, float alpha) {
        ClickableWidget $this = ClickableWidget.class.cast(this);
        if ($this instanceof IColorTint) {
            IColorTint iColorTint = (IColorTint) $this;
            int color = iColorTint.getBackgroundColor();
            float a = (color >> 24 & 0xff) / 255.0F;
            float r = (color >> 16 & 0xff) / 255.0F;
            float g = (color >> 8 & 0xff) / 255.0F;
            float b = (color & 0xff) / 255.0F;
            RenderSystem.setShaderColor(r, g, b, a);
        } else {
            RenderSystem.setShaderColor(red, green, blue, alpha);
        }
    }
}
