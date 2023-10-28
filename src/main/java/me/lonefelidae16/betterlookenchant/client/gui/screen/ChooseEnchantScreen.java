package me.lonefelidae16.betterlookenchant.client.gui.screen;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
import me.lonefelidae16.betterlookenchant.client.gui.TextFormat;
import me.lonefelidae16.betterlookenchant.client.gui.widget.CustomElementListWidgetBase;
import me.lonefelidae16.betterlookenchant.client.gui.widget.OffsetButtonWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class ChooseEnchantScreen extends Screen {
    private final BetterLookEnchantConfigScreen parent;
    private final String beforeEdit;
    private EnchantButtonListWidget listWidget;

    private static final BetterLookEnchantConfig CONFIG = BetterLookEnchantConfig.getInstance();
    private static final List<Enchantment> ALL_ENCHANT_LIST = Arrays.asList(Registries.ENCHANTMENT.stream().filter(Objects::nonNull).toArray(Enchantment[]::new));

    public ChooseEnchantScreen(BetterLookEnchantConfigScreen parent, String beforeEdit) {
        super(Text.translatable("text.betterlookenchant.config.choose.title"));
        this.parent = parent;
        this.beforeEdit = beforeEdit;
    }

    @Override
    protected void init() {
        this.listWidget = new EnchantButtonListWidget(this.client, this.width, this.height, 24, this.height - 32, 24, this.parent, this.beforeEdit);
        this.addSelectableChild(this.listWidget);
        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.client.setScreen(this.parent))
                        .dimensions(this.width / 2 - 55, this.height - 28, 110, 20)
                        .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.listWidget.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, Color.WHITE.argb());
    }

    public static class EnchantButtonListWidget extends EntryListWidget<EnchantButtonEntry> {
        public EnchantButtonListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, BetterLookEnchantConfigScreen parent, String beforeEdit) {
            super(client, width, height, top, bottom, itemHeight);

            // generate a pair of enchant
            final int listSize = ChooseEnchantScreen.ALL_ENCHANT_LIST.size();
            for (int i = 0; i < listSize; i += 2) {
                Pair<String, String> pair = new Pair<>(null, null);
                pair.setLeft(ChooseEnchantScreen.ALL_ENCHANT_LIST.get(i).getTranslationKey());
                pair.setRight(i < listSize - 1 ? ChooseEnchantScreen.ALL_ENCHANT_LIST.get(i + 1).getTranslationKey() : null);
                // add entry
                this.addEntry(new EnchantButtonEntry(pair, beforeEdit, parent));
            }
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {

        }
    }

    public static class EnchantButtonEntry extends CustomElementListWidgetBase.EntryBase<EnchantButtonEntry> {
        public EnchantButtonEntry(Pair<String, String> aPairOfEnchant, String beforeEdit, BetterLookEnchantConfigScreen parent) {
            super();

            for (int i = 0; i < 2; ++i) {
                String key = (i % 2 == 0) ? aPairOfEnchant.getLeft() : aPairOfEnchant.getRight();
                if (key == null) {
                    continue;
                }

                int x = i * 114;
                final OffsetButtonWidget buttonWidget = new OffsetButtonWidget.Builder(Text.translatable(key), button -> {
                    // get the index before edit
                    int idx = ChooseEnchantScreen.CONFIG.enabledEnchants.indexOf(beforeEdit);
                    if (idx == -1) {
                        // the “New” enchant
                        idx = ChooseEnchantScreen.CONFIG.enabledEnchants.size();
                    }

                    // replace
                    ChooseEnchantScreen.CONFIG.enabledEnchants.remove(beforeEdit);
                    ChooseEnchantScreen.CONFIG.enabledEnchants.add(idx, key);

                    // register
                    ChooseEnchantScreen.CONFIG.customFormats.put(
                            key,
                            ChooseEnchantScreen.CONFIG.customFormats.getOrDefault(beforeEdit, TextFormat.EMPTY)
                    );
                    if (!key.equals(beforeEdit)) {
                        ChooseEnchantScreen.CONFIG.customFormats.remove(beforeEdit);
                    }

                    // come back to parent screen
                    parent.refresh();
                    MinecraftClient.getInstance().setScreen(parent);
                })
                        .dimensions(x, 0, 110, 20)
                        .build();
                if (key.equals(beforeEdit)) {
                    // yellow tint if matches selected enchant
                    buttonWidget.setBackgroundTint(Color.MC_YELLOW.argb());
                } else if (ChooseEnchantScreen.CONFIG.enabledEnchants.contains(key)) {
                    // enchant already exists
                    buttonWidget.active = false;
                }
                this.addElement(buttonWidget);
            }
        }
    }
}
