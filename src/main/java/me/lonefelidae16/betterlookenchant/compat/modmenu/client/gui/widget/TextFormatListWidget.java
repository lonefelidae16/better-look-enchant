package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.screen.BetterLookEnchantConfigScreen;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.screen.ChooseEnchantScreen;
import me.lonefelidae16.betterlookenchant.gui.Color;
import me.lonefelidae16.betterlookenchant.gui.TextFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextFormatListWidget extends EntryListWidget<TextFormatListWidget.TextFormatEntry> {
    protected static final String ENTRY_ADD_NEW = "text.betterlookenchant.config.key.add_new";

    private static final BetterLookEnchantConfig CONFIG = BetterLookEnchantConfig.getInstance();

    public TextFormatListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight, BetterLookEnchantConfigScreen parent) {
        super(client, width, height, top, bottom, itemHeight);

        this.addEntry(
                new TextFormatEntry(
                        BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT,
                        TextFormatListWidget.CONFIG.customFormats.getOrDefault(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT, TextFormat.EMPTY),
                        parent
                )
        );
        this.addEntry(
                new TextFormatEntry(
                        BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT,
                        TextFormatListWidget.CONFIG.customFormats.getOrDefault(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT, TextFormat.EMPTY),
                        parent
                )
        );
        TextFormatListWidget.CONFIG.customFormats.keySet().stream()
                .filter(key -> !key.equals(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT) && !key.equals(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT))
                .forEach(key -> this.addEntry(new TextFormatEntry(key, TextFormatListWidget.CONFIG.customFormats.getOrDefault(key, TextFormat.EMPTY), parent)));
        this.addEntry(new TextFormatEntry(ENTRY_ADD_NEW, null, parent));
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if (getFocused() == null) {
            return false;
        }
        return getFocused().changeFocus(lookForwards);
    }

    public static class TextFormatEntry extends ListWidgetEntryBase<TextFormatEntry> {
        private final String key;
        private final boolean customEnchant;
        private OffsetButtonWidget enchantButton;
        private OffsetTextFieldWidget colorEditor;
        private boolean isEnabled;

        private static final int CHECKBOX_MAX_X = 206;
        private static final int CHECKBOX_MARGIN = 20;
        private static final int DEFAULT_ELEMENT_MARGIN = 4;
        private static final int DEFAULT_ELEMENT_HEIGHT = 20;
        private static final int DEFAULT_ELEMENT_WIDTH = DEFAULT_ELEMENT_HEIGHT;

        public TextFormatEntry(@NotNull String key, @Nullable TextFormat format, BetterLookEnchantConfigScreen parent) {
            super();

            this.key = key;
            this.customEnchant = !key.equals(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT) && !key.equals(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT);
            if (this.customEnchant) {
                // change or add enchant: Button
                this.enchantButton = new OffsetButtonWidget(0, 0, 110, DEFAULT_ELEMENT_HEIGHT, Text.translatable(key), button ->
                        MinecraftClient.getInstance().setScreen(new ChooseEnchantScreen(parent, key))
                );
                this.addDrawableChild(this.enchantButton);

                if (!key.equals(ENTRY_ADD_NEW)) {
                    // remove enchant entry: Button
                    OffsetButtonWidget removeButton = new OffsetButtonWidget(120, 0, DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT, Text.translatable("text.betterlookenchant.config.key.remove"), button -> {
                        TextFormatListWidget.CONFIG.customFormats.remove(key);
                        parent.refresh();
                    }, (button, matrices, mouseX, mouseY) -> {
                        if (button.isHovered()) {
                            parent.renderTooltip(matrices, Text.translatable("text.betterlookenchant.config.key.remove_tooltip"), mouseX, mouseY);
                        }
                    });
                    removeButton.setBackgroundTint(Color.MC_DARK_RED.argb());
                    this.addDrawableChild(removeButton);
                }
            }
            if (format != null) {
                this.isEnabled = TextFormatListWidget.CONFIG.enabledEnchants.contains(key);

                // isBold: Checkbox
                OffsetCheckboxWidget boldCheckbox = new OffsetCheckboxWidget(
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 3,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH,
                        DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(), format.isBold()
                );
                boldCheckbox.setCheckedChangedListener(bool -> {
                    TextFormatListWidget.CONFIG.customFormats.put(
                            key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(key, TextFormat.EMPTY).withBold(bool)
                    );
                });
                boldCheckbox.active = this.isEnabled;
                this.addDrawableChild(boldCheckbox);

                // isItalic: Checkbox
                OffsetCheckboxWidget italicCheckbox = new OffsetCheckboxWidget(
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 2,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(), format.isItalic()
                );
                italicCheckbox.setCheckedChangedListener(bool -> {
                    TextFormatListWidget.CONFIG.customFormats.put(
                            key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(key, TextFormat.EMPTY).withItalic(bool)
                    );
                });
                italicCheckbox.active = this.isEnabled;
                this.addDrawableChild(italicCheckbox);

                // isUnderline: Checkbox
                OffsetCheckboxWidget underlineCheckbox = new OffsetCheckboxWidget(
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 1,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(), format.isUnderline()
                );
                underlineCheckbox.setCheckedChangedListener(bool -> {
                    TextFormatListWidget.CONFIG.customFormats.put(
                            key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(key, TextFormat.EMPTY).withUnderline(bool)
                    );
                });
                underlineCheckbox.active = this.isEnabled;
                this.addDrawableChild(underlineCheckbox);

                // isStrike: Checkbox
                OffsetCheckboxWidget strikeCheckbox = new OffsetCheckboxWidget(
                        CHECKBOX_MAX_X,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(), format.isStrike()
                );
                strikeCheckbox.setCheckedChangedListener(bool -> {
                    TextFormatListWidget.CONFIG.customFormats.put(
                            key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(key, TextFormat.EMPTY).withStrike(bool)
                    );
                });
                strikeCheckbox.active = this.isEnabled;
                this.addDrawableChild(strikeCheckbox);

                // enabled or disabled: Checkbox
                OffsetCheckboxWidget enabledCheckbox = new OffsetCheckboxWidget(
                        0,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(),
                        this.isEnabled
                );
                enabledCheckbox.setCheckedChangedListener(bool -> {
                    this.isEnabled = bool;
                    if (bool) {
                        TextFormatListWidget.CONFIG.enabledEnchants.add(key);
                    } else {
                        TextFormatListWidget.CONFIG.enabledEnchants.remove(key);
                    }
                    boldCheckbox.active = bool;
                    italicCheckbox.active = bool;
                    underlineCheckbox.active = bool;
                    strikeCheckbox.active = bool;
                    this.colorEditor.setEditable(bool);
                });
                this.addDrawableChild(enabledCheckbox);

                // colorEditor: TextField
                String colorString = "";
                if (format.getColor() != null) {
                    colorString = Color.fromARGB(format.getColor()).asHexString();
                }
                this.colorEditor = new OffsetTextFieldWidget(MinecraftClient.getInstance().textRenderer, 176, 0, 50, DEFAULT_ELEMENT_HEIGHT, Text.empty());
                this.colorEditor.setEditable(this.isEnabled);
                this.colorEditor.setChangedListener(str -> {
                    if (this.enchantButton != null) {
                        this.enchantButton.setTextColor(Color.fromHexString(this.colorEditor.getText()).argb());
                    }
                    if (!str.isEmpty()) {
                        TextFormatListWidget.CONFIG.customFormats.put(
                                key,
                                TextFormatListWidget.CONFIG.customFormats.getOrDefault(key, TextFormat.EMPTY)
                                        .withColor(Color.fromHexString(str).argb())
                        );
                    }
                });
                this.colorEditor.setText(colorString);
                this.addDrawableChild(this.colorEditor);
            }
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            TextRenderer font = MinecraftClient.getInstance().textRenderer;
            if (!this.customEnchant) {
                font.drawWithShadow(matrices, Text.translatable(this.key), x, 4 + y, Color.fromHexString(this.colorEditor.getText()).argb());
            }
            if (!this.key.equals(ENTRY_ADD_NEW)) {
                int color = (this.isEnabled) ? Color.WHITE.argb() : Color.MC_GRAY.argb();
                drawCenteredText(
                        matrices, font, Text.translatable("text.betterlookenchant.config.key.bold"),
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 3 + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
                drawCenteredText(
                        matrices, font, Text.translatable("text.betterlookenchant.config.key.italic"),
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 2 + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
                drawCenteredText(
                        matrices, font, Text.translatable("text.betterlookenchant.config.key.underline"),
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 1 + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
                drawCenteredText(
                        matrices, font, Text.translatable("text.betterlookenchant.config.key.strike"),
                        CHECKBOX_MAX_X + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
            }
            super.render(matrices, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        }
    }
}
