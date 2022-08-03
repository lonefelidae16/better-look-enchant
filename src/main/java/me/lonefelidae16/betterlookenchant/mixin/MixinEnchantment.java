package me.lonefelidae16.betterlookenchant.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.InfinityEnchantment;
import net.minecraft.enchantment.MendingEnchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    private static final Formatting MAX_LEVEL_COLOR = Formatting.GREEN;

    @Inject(method = "getName(I)Lnet/minecraft/text/Text;", at = @At(value = "RETURN"), cancellable = true)
    private void MixinEnchantment$formatted(int level, CallbackInfoReturnable<MutableText> cir) {
        Enchantment $this = ((Enchantment) (Object) this);
        if ($this.isCursed()) {
            return;  // early return
        }

        MutableText text = cir.getReturnValue();
        String[] enchStr = text.getString().split(" ");
        if ($this.getMaxLevel() == 1) {
            // default color
            text.formatted(MixinEnchantment.MAX_LEVEL_COLOR);

            if ($this instanceof MendingEnchantment) {
                // Mending: 修繕
                text.formatted(Formatting.GOLD);
            } else if ($this instanceof InfinityEnchantment) {
                // Infinity: 無限
                text.formatted(Formatting.LIGHT_PURPLE);
            }
        } else {
            if ($this.getMaxLevel() == level) {
                text.formatted(MixinEnchantment.MAX_LEVEL_COLOR);
            } else if ($this.getMaxLevel() < level) {
                text.formatted(Formatting.DARK_RED).formatted(Formatting.ITALIC);
            }
        }
        cir.setReturnValue(text);
    }
}
