package me.lonefelidae16.betterlookenchant.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CheckboxWidget.class)
public abstract class MixinCheckboxWidget extends PressableWidget {
    public MixinCheckboxWidget(int x, int y, int width, int height, Text text) {
        super(x, y, width, height, text);
    }

    /**
     * gray out if this Checkbox is not active
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
        if (!this.active) {
            Color color = Color.MC_DARK_GRAY;
            RenderSystem.setShaderColor(color.red() / 255.0F, color.green() / 255.0F, color.blue() / 255.0F, alpha);
        } else {
            RenderSystem.setShaderColor(red, green, blue, alpha);
        }
    }
}
