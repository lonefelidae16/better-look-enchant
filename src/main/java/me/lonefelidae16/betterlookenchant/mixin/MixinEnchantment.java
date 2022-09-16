package me.lonefelidae16.betterlookenchant.mixin;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.gui.TextFormat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {
    @Inject(method = "getName(I)Lnet/minecraft/text/Text;", at = @At(value = "RETURN"), cancellable = true)
    private void betterLookEnchant$formatted(int level, CallbackInfoReturnable<MutableText> cir) {
        final Enchantment $this = Enchantment.class.cast(this);
        final MutableText text = cir.getReturnValue();
        final BetterLookEnchantConfig config = BetterLookEnchantConfig.getInstance();
        final TextFormat defaultFontFormat = (config.enabledEnchants.contains(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT)) ?
                config.customFormats.getOrDefault(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT, TextFormat.EMPTY) :
                TextFormat.EMPTY;
        final TextFormat defaultLvMaxFormat = (config.enabledEnchants.contains(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT)) ?
                config.customFormats.getOrDefault(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT, TextFormat.EMPTY) :
                TextFormat.EMPTY;

        // set custom style
        if (!defaultFontFormat.equals(TextFormat.EMPTY)) {
            text.setStyle(defaultFontFormat.asStyle());
        }

        if ($this.getMaxLevel() == level) {
            // set custom Lv Max style
            if (!$this.isCursed() && !defaultLvMaxFormat.equals(TextFormat.EMPTY)) {
                // exclude Cursed enchant
                text.setStyle(defaultLvMaxFormat.asStyle());
            }

            // search and apply specified style that matches this enchant
            if (config.enabledEnchants.contains($this.getTranslationKey())) {
                TextFormat format = config.customFormats.getOrDefault($this.getTranslationKey(), TextFormat.EMPTY);
                if (!format.equals(TextFormat.EMPTY)) {
                    text.setStyle(format.asStyle());
                }
            }
        } else if ($this.getMaxLevel() < level) {
            // was it generated from command?
            text.formatted(Formatting.DARK_RED).formatted(Formatting.ITALIC);
        }

        cir.setReturnValue(text);
    }
}
