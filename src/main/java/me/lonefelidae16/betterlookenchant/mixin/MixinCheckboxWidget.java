package me.lonefelidae16.betterlookenchant.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CheckboxWidget.class)
public abstract class MixinCheckboxWidget extends PressableWidget {
    public MixinCheckboxWidget(int x, int y, int width, int height, Text text) {
        super(x, y, width, height, text);
    }

    /**
     * gray out if this Checkbox is not active
     */
    @Inject(
            method = "renderButton(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V", ordinal = 0)
    )
    private void betterLookEnchant$changeShaderColor(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!this.active) {
            Color color = Color.MC_DARK_GRAY;
            RenderSystem.setShaderColor(color.red() / 255.0F, color.green() / 255.0F, color.blue() / 255.0F, this.alpha);
        }
    }
}
