package me.lonefelidae16.betterlookenchant.client.gui.widget;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
import me.lonefelidae16.betterlookenchant.client.gui.TextFormat;
import me.lonefelidae16.betterlookenchant.client.gui.screen.BetterLookEnchantConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class TextFormatListWidget extends CustomElementListWidgetBase<TextFormatListWidget.EntryBase> {
    protected static final String ENTRY_ADD_NEW = "text.betterlookenchant.config.key.add_new";

    private static final BetterLookEnchantConfig CONFIG = BetterLookEnchantConfig.getConfig();

    public TextFormatListWidget(MinecraftClient client, int width, BetterLookEnchantConfigScreen parent) {
        super(client, width, parent.layout.getContentHeight(), parent.layout.getHeaderHeight(), 25);

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
        this.addEntry(new AddEnchantEntry(parent));
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    public boolean isEditingEnchantment() {
        for (var entry : this.children()) {
            if (entry.getFocused() instanceof EnchantmentEditWidget) {
                return true;
            }
        }
        return false;
    }

    public static class EnchantmentEditButtonCallback implements ButtonWidget.PressAction {
        private final EnchantmentEditWidget enchantEditWidget;
        private final String text;

        EnchantmentEditButtonCallback(EnchantmentEditWidget enchantmentEditWidget, String text) {
            this.enchantEditWidget = enchantmentEditWidget;
            this.text = text;
        }

        @Override
        public void onPress(ButtonWidget button) {
            button.visible = false;
            this.enchantEditWidget.visible = true;
            this.enchantEditWidget.setText(this.text);
        }
    }

    public static class EnchantmentSubmitCallback implements EnchantmentEditWidget.SubmitListener {
        private final ClickableWidget enchantPreviewWidget;
        private final String beforeEdit;

        EnchantmentSubmitCallback(ClickableWidget enchantPreviewWidget) {
            this.enchantPreviewWidget = enchantPreviewWidget;
            this.beforeEdit = enchantPreviewWidget.getMessage().getString();
        }

        @Override
        public void onSubmit(EnchantmentEditWidget widget) {
            final String edited = widget.getText();
            if (edited.isEmpty()) {
                return;
            }

            int idx = TextFormatListWidget.CONFIG.enabledEnchants.indexOf(this.beforeEdit);
            if (idx == -1) {
                idx = TextFormatListWidget.CONFIG.enabledEnchants.size();
            }
            // replace
            TextFormatListWidget.CONFIG.enabledEnchants.remove(this.beforeEdit);
            TextFormatListWidget.CONFIG.enabledEnchants.add(idx, edited);
            // register
            TextFormatListWidget.CONFIG.customFormats.put(
                    edited,
                    TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.beforeEdit, TextFormat.EMPTY)
            );
            if (!edited.equals(this.beforeEdit)) {
                TextFormatListWidget.CONFIG.customFormats.remove(this.beforeEdit);
            }

            this.enchantPreviewWidget.setMessage(Text.of(edited));

            changeVisibility(widget);
        }

        @Override
        public void onCancel(EnchantmentEditWidget widget) {
            changeVisibility(widget);
        }

        private void changeVisibility(EnchantmentEditWidget widget) {
            widget.visible = false;
            this.enchantPreviewWidget.visible = true;
        }
    }

    public static abstract class EntryBase extends CustomElementListWidgetBase.EntryBase<EntryBase> {
        static final int CHECKBOX_MAX_X = 334;
        static final int CHECKBOX_MARGIN = 20;
        static final int DEFAULT_ELEMENT_MARGIN = 4;
        static final int DEFAULT_ELEMENT_WIDTH = 20;
        static final int ENCHANT_PREVIEW_WIDGET_X = 24;
    }

    public static class TextFormatEntry extends EntryBase {
        private final BetterLookEnchantConfigScreen parent;
        private final String key;
        private final ClickableWidget enchantPreviewWidget;
        private final TextFieldWidget colorEditor;
        private final EnchantmentEditWidget enchantEditWidget;

        private boolean isEnabled;

        TextFormatEntry(@NotNull String key, TextFormat format, BetterLookEnchantConfigScreen parent) {
            this.key = key;
            this.parent = parent;

            this.isEnabled = TextFormatListWidget.CONFIG.enabledEnchants.contains(this.key);
            this.colorEditor = new TextFieldWidget(parent.getTextRenderer(), CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 3 - 60 - DEFAULT_ELEMENT_MARGIN, 0, 60, 20, Text.empty());

            this.enchantEditWidget = EnchantmentEditWidget.builder(parent.getTextRenderer(), ENCHANT_PREVIEW_WIDGET_X, 0).build();
            this.addElement(this.enchantEditWidget);

            boolean customEnchant = !this.key.equals(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT) && !this.key.equals(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT);
            if (customEnchant) {
                // change or add enchant: Button
                this.enchantPreviewWidget = ButtonWidget.builder(Text.of(this.key).copy().setStyle(TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY).asStyle()),
                                new EnchantmentEditButtonCallback(this.enchantEditWidget, this.key) {
                                    @Override
                                    public void onPress(ButtonWidget button) {
                                        super.onPress(button);
                                        TextFormatEntry.this.setFocused(TextFormatEntry.this.enchantEditWidget);
                                    }
                                })
                        .position(ENCHANT_PREVIEW_WIDGET_X, 0)
                        .width(110)
                        .build();
                this.addElement(this.enchantPreviewWidget);

                // remove enchant entry: Button
                ButtonWidget removeButton = ButtonWidget.builder(Text.translatable("text.betterlookenchant.config.remove").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)), button -> {
                            TextFormatListWidget.CONFIG.customFormats.remove(this.key);
                            TextFormatListWidget.CONFIG.enabledEnchants.remove(this.key);
                            parent.refresh();
                        })
                        .position(this.parent.width - DEFAULT_ELEMENT_WIDTH - DEFAULT_ELEMENT_MARGIN, 0)
                        .width(DEFAULT_ELEMENT_WIDTH)
                        .tooltip(Tooltip.of(Text.translatable("text.betterlookenchant.config.remove_tooltip")))
                        .build();
                this.addElement(removeButton);
            } else {
                MutableText text = Text.of(this.key).copy().setStyle(TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY).asStyle());
                this.enchantPreviewWidget = new TextWidget(text, this.parent.getTextRenderer());
                this.enchantPreviewWidget.setX(ENCHANT_PREVIEW_WIDGET_X);
                this.addElement(this.enchantPreviewWidget);
            }

            this.enchantEditWidget.setSubmitListener(new EnchantmentSubmitCallback(this.enchantPreviewWidget) {
                @Override
                public void onSubmit(EnchantmentEditWidget widget) {
                    super.onSubmit(widget);
                    parent.refresh();
                }
            });

            this.isEnabled = TextFormatListWidget.CONFIG.enabledEnchants.contains(this.key);

            // isBold: Checkbox
            CheckboxWidget boldCheckbox = CheckboxWidget.builder(Text.translatable("text.betterlookenchant.config.bold"), parent.getTextRenderer())
                    .pos(CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 3, 0)
                    .checked(format.isBold())
                    .callback((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withBold(bool));
                        updateEnchantButton();
                    })
                    .build();
            boldCheckbox.active = this.isEnabled;
            this.addElement(boldCheckbox);

            // isItalic: Checkbox
            CheckboxWidget italicCheckbox = CheckboxWidget.builder(Text.translatable("text.betterlookenchant.config.italic"), parent.getTextRenderer())
                    .pos(CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 2, 0)
                    .checked(format.isItalic())
                    .callback((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withItalic(bool));
                        updateEnchantButton();
                    })
                    .build();
            italicCheckbox.active = this.isEnabled;
            this.addElement(italicCheckbox);

            // isUnderline: Checkbox
            CheckboxWidget underlineCheckbox = CheckboxWidget.builder(Text.translatable("text.betterlookenchant.config.underline"), parent.getTextRenderer())
                    .pos(CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH), 0)
                    .checked(format.isUnderline())
                    .callback((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withUnderline(bool));
                        updateEnchantButton();
                    })
                    .build();
            underlineCheckbox.active = this.isEnabled;
            this.addElement(underlineCheckbox);

            // isStrike: Checkbox
            CheckboxWidget strikeCheckbox = CheckboxWidget.builder(Text.translatable("text.betterlookenchant.config.strike"), parent.getTextRenderer())
                    .pos(CHECKBOX_MAX_X, 0)
                    .checked(format.isStrike())
                    .callback((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withStrike(bool));
                        updateEnchantButton();
                    })
                    .build();
            strikeCheckbox.active = this.isEnabled;
            this.addElement(strikeCheckbox);

            // enabled or disabled: Checkbox
            CheckboxWidget enabledCheckbox = CheckboxWidget.builder(Text.empty(), parent.getTextRenderer())
                    .pos(4, 0)
                    .checked(this.isEnabled)
                    .callback((widget, bool) -> {
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
                    })
                    .build();
            this.addElement(enabledCheckbox);

            // colorEditor: TextField
            String colorString = "";
            if (format.getColor() != null) {
                colorString = Color.fromARGB(format.getColor()).asHexString();
            }
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

        private void updateEnchantButton() {
            if (this.enchantPreviewWidget == null) {
                return;
            }
            Style style = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY).asStyle();
            this.enchantPreviewWidget.setMessage(this.enchantPreviewWidget.getMessage().copy().setStyle(style));
        }
    }

    public static class AddEnchantEntry extends EntryBase {
        AddEnchantEntry(BetterLookEnchantConfigScreen parent) {
            EnchantmentEditWidget enchantmentEditWidget = new EnchantmentEditWidget(parent.getTextRenderer(), ENCHANT_PREVIEW_WIDGET_X, 0, 110, 20, Text.empty());
            this.addElement(enchantmentEditWidget);

            var widget = ButtonWidget.builder(Text.translatable(ENTRY_ADD_NEW),
                            new EnchantmentEditButtonCallback(enchantmentEditWidget, "") {
                                @Override
                                public void onPress(ButtonWidget button) {
                                    super.onPress(button);
                                    AddEnchantEntry.this.setFocused(enchantmentEditWidget);
                                }
                            })
                    .position(ENCHANT_PREVIEW_WIDGET_X, 0)
                    .width(110)
                    .build();
            this.addElement(widget);

            enchantmentEditWidget.setSubmitListener(new EnchantmentSubmitCallback(widget) {
                @Override
                public void onSubmit(EnchantmentEditWidget widget) {
                    super.onSubmit(widget);
                    parent.refresh();
                }
            });
        }
    }
}
