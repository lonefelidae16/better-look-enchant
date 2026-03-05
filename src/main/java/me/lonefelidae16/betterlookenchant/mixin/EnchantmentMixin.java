package me.lonefelidae16.betterlookenchant.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantClient;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.TextFormat;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @WrapMethod(method = "getName(Lnet/minecraft/registry/entry/RegistryEntry;I)Lnet/minecraft/text/Text;")
    private static Text betterLookEnchant$formatted(RegistryEntry<Enchantment> registryEntry, int level, Operation<Text> original) {
        final Enchantment enchantment = registryEntry.value();
        final boolean bCursed = registryEntry.isIn(EnchantmentTags.CURSE);
        final MutableText text = (MutableText) original.call(registryEntry, level);
        final BetterLookEnchantConfig config = BetterLookEnchantConfig.getConfig();
        final TextFormat defaultFontFormat = config.customFormats.getOrDefault(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT, TextFormat.EMPTY);
        final TextFormat defaultLvMaxFormat = config.customFormats.getOrDefault(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT, TextFormat.EMPTY);

        // set custom style
        if (!bCursed && !defaultFontFormat.isEmpty()) {
            text.setStyle(defaultFontFormat.asStyle());
        }

        if (enchantment.getMaxLevel() == level) {
            // set custom Lv Max style
            if (!bCursed && !defaultLvMaxFormat.isEmpty()) {
                // exclude Cursed enchant
                text.setStyle(defaultLvMaxFormat.asStyle());
            }

            final var optionalKey = registryEntry.getKey();
            optionalKey.ifPresentOrElse(key -> {
                final String k = key.getValue().toString();
                // search and apply specified style that matches this enchant
                if (config.enabledEnchants.contains(k)) {
                    TextFormat format = config.customFormats.getOrDefault(k, TextFormat.EMPTY);
                    if (!format.isEmpty()) {
                        text.setStyle(format.asStyle());
                    }
                }
            }, () -> BetterLookEnchantClient.LOGGER.error("RegistryKey is empty"));
        } else if (enchantment.getMaxLevel() < level) {
            // was it generated from command?
            text.formatted(Formatting.DARK_RED).formatted(Formatting.ITALIC);
        }

        return text;
    }
}
