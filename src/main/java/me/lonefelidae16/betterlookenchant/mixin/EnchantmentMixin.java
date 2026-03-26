package me.lonefelidae16.betterlookenchant.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantClient;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.TextFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @WrapMethod(method = "getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;")
    private static Component betterLookEnchant$formatted(Holder<Enchantment> registryEntry, int level, Operation<Component> original) {
        final Enchantment enchantment = registryEntry.value();
        final boolean bCursed = registryEntry.is(EnchantmentTags.CURSE);
        final MutableComponent text = (MutableComponent) original.call(registryEntry, level);
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

            final var optionalKey = registryEntry.unwrapKey();
            optionalKey.ifPresentOrElse(key -> {
                final String k = key.identifier().toString();
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
            text.withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.ITALIC);
        }

        return text;
    }
}
