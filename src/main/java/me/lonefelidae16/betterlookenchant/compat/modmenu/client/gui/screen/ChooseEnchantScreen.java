package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.screen;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget.ListWidgetEntryBase;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget.OffsetButtonWidget;
import me.lonefelidae16.betterlookenchant.gui.Color;
import me.lonefelidae16.betterlookenchant.gui.TextFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Objects;

public class ChooseEnchantScreen extends Screen {
    private final BetterLookEnchantConfigScreen parent;
    private final String beforeEdit;
    private EnchantButtonListWidget listWidget;

    private static final BetterLookEnchantConfig CONFIG = BetterLookEnchantConfig.getInstance();
    private static final List<Enchantment> ALL_ENCHANT_LIST = Registry.ENCHANTMENT.stream().filter(Objects::nonNull).toList();

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
                new ButtonWidget(this.width / 2 - 55, this.height - 28, 110, 20, ScreenTexts.CANCEL,
                        button -> this.client.setScreen(this.parent)
                )
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.listWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 8, Color.WHITE.argb());
        super.render(matrices, mouseX, mouseY, delta);
    }

    public static class EnchantButtonListWidget extends EntryListWidget<EnchantButtonEntry> {
        public EnchantButtonListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, BetterLookEnchantConfigScreen parent, String beforeEdit) {
            super(client, width, height, top, bottom, itemHeight);

            // generate a pair of enchant
            for (int i = 0; i < ChooseEnchantScreen.ALL_ENCHANT_LIST.size(); i += 2) {
                Pair<String, String> pair = new Pair<>(null, null);
                for (int j = 0; j < 2; ++j) {
                    if ((i + j) % 2 == 0) {
                        pair.setLeft(ChooseEnchantScreen.ALL_ENCHANT_LIST.get(i + j).getTranslationKey());
                    } else if ((i + j) < ChooseEnchantScreen.ALL_ENCHANT_LIST.size()) {
                        pair.setRight(ChooseEnchantScreen.ALL_ENCHANT_LIST.get(i + j).getTranslationKey());
                    }
                }
                // add entry
                this.addEntry(new EnchantButtonEntry(pair, beforeEdit, parent));
            }
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {

        }
    }

    public static class EnchantButtonEntry extends ListWidgetEntryBase<EnchantButtonEntry> {
        public EnchantButtonEntry(Pair<String, String> aPairOfEnchant, String beforeEdit, BetterLookEnchantConfigScreen parent) {
            super();

            for (int i = 0; i < 2; ++i) {
                String key = (i % 2 == 0) ? aPairOfEnchant.getLeft() : aPairOfEnchant.getRight();
                if (key == null) {
                    continue;
                }

                int x = i * 114;
                final OffsetButtonWidget buttonWidget = new OffsetButtonWidget(x, 0, 110, 20, Text.translatable(key), button -> {
                    // get the index before edit
                    int idx = ChooseEnchantScreen.CONFIG.enabledEnchants.indexOf(beforeEdit);
                    if (idx == -1) {
                        // the button is “New”
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
                    ChooseEnchantScreen.CONFIG.customFormats.remove(beforeEdit);

                    // come back to parent screen
                    parent.refresh();
                    MinecraftClient.getInstance().setScreen(parent);
                });
                if (key.equals(beforeEdit)) {
                    // yellow tint if matches selected enchant
                    buttonWidget.setBackgroundTint(Color.MC_YELLOW.argb());
                } else if (ChooseEnchantScreen.CONFIG.enabledEnchants.contains(key)) {
                    // enchant already exists
                    buttonWidget.active = false;
                }
                this.addDrawableChild(buttonWidget);
            }
        }
    }
}
