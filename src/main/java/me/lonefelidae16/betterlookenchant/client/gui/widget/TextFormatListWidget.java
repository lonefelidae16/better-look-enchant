package me.lonefelidae16.betterlookenchant.client.gui.widget;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
import me.lonefelidae16.betterlookenchant.client.gui.TextFormat;
import me.lonefelidae16.betterlookenchant.client.gui.screen.BetterLookEnchantConfigScreen;
import me.lonefelidae16.betterlookenchant.client.gui.screen.ChooseEnchantScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextFormatListWidget extends CustomElementListWidgetBase<TextFormatListWidget.TextFormatEntry> {
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

    public static class TextFormatEntry extends CustomElementListWidgetBase.EntryBase<TextFormatEntry> {
        private final String key;
        private final BetterLookEnchantConfigScreen parent;
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
            this.parent = parent;
            this.customEnchant = !this.key.equals(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT) && !this.key.equals(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT);
            if (this.customEnchant) {
                // change or add enchant: Button
                this.enchantButton = new OffsetButtonWidget.Builder(Text.translatable(this.key), button ->
                        MinecraftClient.getInstance().setScreen(new ChooseEnchantScreen(this.parent, this.key))
                )
                        .dimensions(0, 0, 110, DEFAULT_ELEMENT_HEIGHT)
                        .build();
                if (format != null) {
                    this.enchantButton.setTextFormat(format.asStyle());
                }
                this.addElement(this.enchantButton);

                if (!this.key.equals(TextFormatListWidget.ENTRY_ADD_NEW)) {
                    // remove enchant entry: Button
                    OffsetButtonWidget removeButton = new OffsetButtonWidget.Builder(Text.translatable("text.betterlookenchant.config.remove"), button -> {
                        TextFormatListWidget.CONFIG.customFormats.remove(this.key);
                        TextFormatListWidget.CONFIG.enabledEnchants.remove(this.key);
                        this.parent.refresh();
                    })
                            .dimensions(120, 0, DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT)
                            .tooltip(Tooltip.of(Text.translatable("text.betterlookenchant.config.remove_tooltip")))
                            .build();
                    removeButton.setBackgroundTint(Color.MC_DARK_RED.argb());
                    this.addElement(removeButton);
                }
            }
            if (format != null) {
                this.isEnabled = TextFormatListWidget.CONFIG.enabledEnchants.contains(this.key);

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
                            this.key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY)
                                    .withBold(bool)
                    );
                    updateEnchantButton();
                });
                boldCheckbox.active = this.isEnabled;
                this.addElement(boldCheckbox);

                // isItalic: Checkbox
                OffsetCheckboxWidget italicCheckbox = new OffsetCheckboxWidget(
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 2,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(), format.isItalic()
                );
                italicCheckbox.setCheckedChangedListener(bool -> {
                    TextFormatListWidget.CONFIG.customFormats.put(
                            this.key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY)
                                    .withItalic(bool)
                    );
                    updateEnchantButton();
                });
                italicCheckbox.active = this.isEnabled;
                this.addElement(italicCheckbox);

                // isUnderline: Checkbox
                OffsetCheckboxWidget underlineCheckbox = new OffsetCheckboxWidget(
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 1,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(), format.isUnderline()
                );
                underlineCheckbox.setCheckedChangedListener(bool -> {
                    TextFormatListWidget.CONFIG.customFormats.put(
                            this.key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY)
                                    .withUnderline(bool)
                    );
                    updateEnchantButton();
                });
                underlineCheckbox.active = this.isEnabled;
                this.addElement(underlineCheckbox);

                // isStrike: Checkbox
                OffsetCheckboxWidget strikeCheckbox = new OffsetCheckboxWidget(
                        CHECKBOX_MAX_X,
                        DEFAULT_ELEMENT_HEIGHT + DEFAULT_ELEMENT_MARGIN,
                        DEFAULT_ELEMENT_WIDTH, DEFAULT_ELEMENT_HEIGHT,
                        Text.empty(), format.isStrike()
                );
                strikeCheckbox.setCheckedChangedListener(bool -> {
                    TextFormatListWidget.CONFIG.customFormats.put(
                            this.key,
                            TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY)
                                    .withStrike(bool)
                    );
                    updateEnchantButton();
                });
                strikeCheckbox.active = this.isEnabled;
                this.addElement(strikeCheckbox);

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
                        TextFormatListWidget.CONFIG.enabledEnchants.add(this.key);
                    } else {
                        TextFormatListWidget.CONFIG.enabledEnchants.remove(this.key);
                    }
                    boldCheckbox.active = bool;
                    italicCheckbox.active = bool;
                    underlineCheckbox.active = bool;
                    strikeCheckbox.active = bool;
                    this.colorEditor.setEditable(bool);
                });
                this.addElement(enabledCheckbox);

                // colorEditor: TextField
                String colorString = "";
                if (format.getColor() != null) {
                    colorString = Color.fromARGB(format.getColor()).asHexString();
                }
                this.colorEditor = new OffsetTextFieldWidget(MinecraftClient.getInstance().textRenderer, 166, 0, 60, DEFAULT_ELEMENT_HEIGHT, Text.empty());
                this.colorEditor.setText(colorString);
                this.colorEditor.setEditable(this.isEnabled);
                this.colorEditor.setChangedListener(str -> {
                    int color = Color.fromHexString(str).argb();
                    if (!str.isEmpty()) {
                        TextFormatListWidget.CONFIG.customFormats.put(
                                this.key,
                                TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY)
                                        .withColor(color)
                        );
                    }
                    updateEnchantButton();
                });
                this.addElement(this.colorEditor);
            }
        }

        private void updateEnchantButton() {
            if (this.enchantButton == null) {
                return;
            }
            this.enchantButton.setTextFormat(TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY).asStyle());
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            if (!this.customEnchant) {
                MutableText text = Text.translatable(this.key).setStyle(TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY).asStyle());
                context.drawTextWithShadow(textRenderer, text, x, 4 + y, Color.fromHexString(this.colorEditor.getText()).argb());
            }
            if (!this.key.equals(ENTRY_ADD_NEW)) {
                int color = (this.isEnabled) ? Color.WHITE.argb() : Color.MC_GRAY.argb();
                context.drawCenteredTextWithShadow(
                        textRenderer, Text.translatable("text.betterlookenchant.config.bold"),
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 3 + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
                context.drawCenteredTextWithShadow(
                        textRenderer, Text.translatable("text.betterlookenchant.config.italic"),
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 2 + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
                context.drawCenteredTextWithShadow(
                        textRenderer, Text.translatable("text.betterlookenchant.config.underline"),
                        CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 1 + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
                context.drawCenteredTextWithShadow(
                        textRenderer, Text.translatable("text.betterlookenchant.config.strike"),
                        CHECKBOX_MAX_X + DEFAULT_ELEMENT_WIDTH / 2 + x,
                        44 + y,
                        color
                );
            }
        }
    }
}
