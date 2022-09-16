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
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 6, Color.WHITE.argb());
        super.render(matrices, mouseX, mouseY, delta);
    }

    public static class EnchantButtonListWidget extends EntryListWidget<EnchantButtonEntry> {
        public EnchantButtonListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, BetterLookEnchantConfigScreen parent, String beforeEdit) {
            super(client, width, height, top, bottom, itemHeight);

            List<Enchantment> allEnchantList = Registry.ENCHANTMENT.stream().filter(Objects::nonNull).toList();
            for (int i = 0; i < allEnchantList.size(); i += 2) {
                Pair<String, String> pair = new Pair<>(null, null);
                for (int j = 0; j < 2; ++j) {
                    if ((i + j) % 2 == 0) {
                        pair.setLeft(allEnchantList.get(i + j).getTranslationKey());
                    } else if ((i + j) < allEnchantList.size()) {
                        pair.setRight(allEnchantList.get(i + j).getTranslationKey());
                    }
                }
                this.addEntry(new EnchantButtonEntry(pair, beforeEdit, parent));
            }
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {

        }
    }

    public static class EnchantButtonEntry extends ListWidgetEntryBase<EnchantButtonEntry> {
        public EnchantButtonEntry(Pair<String, String> pairOfEnchant, String beforeEdit, BetterLookEnchantConfigScreen parent) {
            super();

            for (int i = 0; i < 2; ++i) {
                String key = (i % 2 == 0) ? pairOfEnchant.getLeft() : pairOfEnchant.getRight();
                if (key == null) {
                    continue;
                }

                int x = i * 114;
                final OffsetButtonWidget buttonWidget = new OffsetButtonWidget(x, 0, 110, 20, Text.translatable(key), button -> {
                    // get the index before edit
                    int idx = CONFIG.enabledEnchants.indexOf(beforeEdit);
                    if (idx == -1) {
                        idx = CONFIG.enabledEnchants.size();
                    }

                    // replace
                    CONFIG.enabledEnchants.remove(beforeEdit);
                    CONFIG.enabledEnchants.add(idx, key);

                    // register
                    CONFIG.customFormats.put(key, CONFIG.customFormats.getOrDefault(key, TextFormat.EMPTY));

                    // come back to parent screen
                    parent.refresh();
                    MinecraftClient.getInstance().setScreen(parent);
                });
                if (key.equals(beforeEdit)) {
                    buttonWidget.setBackgroundTint(Color.MC_YELLOW.argb());
                } else if (CONFIG.enabledEnchants.contains(key)) {
                    buttonWidget.active = false;
                }
                this.addDrawableChild(buttonWidget);
            }
        }

        public EnchantButtonEntry(String enchantmentKey, String beforeEdit, BetterLookEnchantConfigScreen parent) {
            this(new Pair<>(enchantmentKey, null), beforeEdit, parent);
        }
    }
}
