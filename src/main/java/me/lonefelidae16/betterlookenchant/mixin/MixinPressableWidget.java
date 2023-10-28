package me.lonefelidae16.betterlookenchant.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lonefelidae16.betterlookenchant.client.gui.IColorTint;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PressableWidget.class)
public abstract class MixinPressableWidget extends ClickableWidget {
    public MixinPressableWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    /**
     * enable background color tint which implements {@link IColorTint}
     */
    @Inject(
            method = "renderButton(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0)
    )
    private void betterLookEnchant$changeShaderColor(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        PressableWidget $this = PressableWidget.class.cast(this);
        if ($this instanceof IColorTint) {
            IColorTint iColorTint = (IColorTint) $this;
            int color = iColorTint.getBackgroundColor();
            float a = (color >> 24 & 0xff) / 255.0F;
            float r = (color >> 16 & 0xff) / 255.0F;
            float g = (color >> 8 & 0xff) / 255.0F;
            float b = (color & 0xff) / 255.0F;
            RenderSystem.setShaderColor(r, g, b, a);
        }
    }
}
