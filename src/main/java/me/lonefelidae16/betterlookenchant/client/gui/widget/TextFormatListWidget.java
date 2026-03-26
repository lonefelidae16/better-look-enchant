package me.lonefelidae16.betterlookenchant.client.gui.widget;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
import me.lonefelidae16.betterlookenchant.client.gui.TextFormat;
import me.lonefelidae16.betterlookenchant.client.gui.screen.BetterLookEnchantConfigScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

public class TextFormatListWidget extends CustomElementListWidgetBase<TextFormatListWidget.EntryBase> {
    protected static final String ENTRY_ADD_NEW = "text.betterlookenchant.config.key.add_new";

    private static final BetterLookEnchantConfig CONFIG = BetterLookEnchantConfig.getConfig();

    public TextFormatListWidget(Minecraft client, int width, BetterLookEnchantConfigScreen parent) {
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

    public static class EnchantmentEditButtonCallback implements Button.OnPress {
        private final EnchantmentEditWidget enchantEditWidget;
        private final String text;

        EnchantmentEditButtonCallback(EnchantmentEditWidget enchantmentEditWidget, String text) {
            this.enchantEditWidget = enchantmentEditWidget;
            this.text = text;
        }

        @Override
        public void onPress(Button button) {
            button.visible = false;
            this.enchantEditWidget.visible = true;
            this.enchantEditWidget.setValue(this.text);
        }
    }

    public static class EnchantmentSubmitCallback implements EnchantmentEditWidget.SubmitListener {
        private final AbstractWidget enchantPreviewWidget;
        private final String beforeEdit;

        EnchantmentSubmitCallback(AbstractWidget enchantPreviewWidget) {
            this.enchantPreviewWidget = enchantPreviewWidget;
            this.beforeEdit = enchantPreviewWidget.getMessage().getString();
        }

        @Override
        public void onSubmit(EnchantmentEditWidget widget) {
            final String edited = widget.getValue();
            if (edited.isEmpty()) {
                return;
            }

            final int idx;
            if (TextFormatListWidget.CONFIG.enabledEnchants.contains(this.beforeEdit)) {
                idx = TextFormatListWidget.CONFIG.enabledEnchants.indexOf(this.beforeEdit);
            } else {
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

            this.enchantPreviewWidget.setMessage(Component.nullToEmpty(edited));

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

    public static abstract class EntryBase extends CustomElementListWidgetBase.EntryBase<me.lonefelidae16.betterlookenchant.client.gui.widget.TextFormatListWidget.EntryBase> {
        static final int CHECKBOX_MAX_X = 334;
        static final int CHECKBOX_MARGIN = 20;
        static final int DEFAULT_ELEMENT_MARGIN = 4;
        static final int DEFAULT_ELEMENT_WIDTH = 20;
        static final int ENCHANT_PREVIEW_WIDGET_X = 24;
    }

    public static class TextFormatEntry extends me.lonefelidae16.betterlookenchant.client.gui.widget.TextFormatListWidget.EntryBase {
        private final BetterLookEnchantConfigScreen parent;
        private final String key;
        private final AbstractWidget enchantPreviewWidget;
        private final EditBox colorEditor;
        private final EnchantmentEditWidget enchantEditWidget;

        private boolean isEnabled;

        TextFormatEntry(@NotNull String key, TextFormat format, BetterLookEnchantConfigScreen parent) {
            this.key = key;
            this.parent = parent;

            this.isEnabled = TextFormatListWidget.CONFIG.enabledEnchants.contains(this.key);
            this.colorEditor = new EditBox(parent.getFont(), CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 3 - 60 - DEFAULT_ELEMENT_MARGIN, 0, 60, 20, Component.empty());

            this.enchantEditWidget = EnchantmentEditWidget.builder(parent.getFont(), ENCHANT_PREVIEW_WIDGET_X, 0).build();
            this.addElement(this.enchantEditWidget);

            boolean customEnchant = !this.key.equals(BetterLookEnchantConfig.ENTRY_KEY_DEFAULT_FORMAT) && !this.key.equals(BetterLookEnchantConfig.ENTRY_KEY_LV_MAX_FORMAT);
            if (customEnchant) {
                // change or add enchant: Button
                this.enchantPreviewWidget = Button.builder(Component.translatable(this.key).setStyle(TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY).asStyle()),
                                new EnchantmentEditButtonCallback(this.enchantEditWidget, this.key) {
                                    @Override
                                    public void onPress(Button button) {
                                        super.onPress(button);
                                        TextFormatEntry.this.setFocused(TextFormatEntry.this.enchantEditWidget);
                                    }
                                })
                        .pos(ENCHANT_PREVIEW_WIDGET_X, 0)
                        .width(110)
                        .build();
                this.addElement(this.enchantPreviewWidget);

                // remove enchant entry: Button
                Button removeButton = Button.builder(Component.translatable("text.betterlookenchant.config.remove").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_RED)), button -> {
                            TextFormatListWidget.CONFIG.customFormats.remove(this.key);
                            TextFormatListWidget.CONFIG.enabledEnchants.remove(this.key);
                            parent.refresh();
                        })
                        .pos(this.parent.width - DEFAULT_ELEMENT_WIDTH - DEFAULT_ELEMENT_MARGIN, 0)
                        .width(DEFAULT_ELEMENT_WIDTH)
                        .tooltip(Tooltip.create(Component.translatable("text.betterlookenchant.config.remove_tooltip")))
                        .build();
                this.addElement(removeButton);
            } else {
                MutableComponent text = Component.translatable(this.key).setStyle(TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY).asStyle());
                this.enchantPreviewWidget = new StringWidget(text, this.parent.getFont());
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
            Checkbox boldCheckbox = Checkbox.builder(Component.translatable("text.betterlookenchant.config.bold"), parent.getFont())
                    .pos(CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 3, 0)
                    .selected(format.isBold())
                    .onValueChange((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withBold(bool));
                        updateEnchantButton();
                    })
                    .build();
            boldCheckbox.active = this.isEnabled;
            this.addElement(boldCheckbox);

            // isItalic: Checkbox
            Checkbox italicCheckbox = Checkbox.builder(Component.translatable("text.betterlookenchant.config.italic"), parent.getFont())
                    .pos(CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH) * 2, 0)
                    .selected(format.isItalic())
                    .onValueChange((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withItalic(bool));
                        updateEnchantButton();
                    })
                    .build();
            italicCheckbox.active = this.isEnabled;
            this.addElement(italicCheckbox);

            // isUnderline: Checkbox
            Checkbox underlineCheckbox = Checkbox.builder(Component.translatable("text.betterlookenchant.config.underline"), parent.getFont())
                    .pos(CHECKBOX_MAX_X - (CHECKBOX_MARGIN + DEFAULT_ELEMENT_WIDTH), 0)
                    .selected(format.isUnderline())
                    .onValueChange((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withUnderline(bool));
                        updateEnchantButton();
                    })
                    .build();
            underlineCheckbox.active = this.isEnabled;
            this.addElement(underlineCheckbox);

            // isStrike: Checkbox
            Checkbox strikeCheckbox = Checkbox.builder(Component.translatable("text.betterlookenchant.config.strike"), parent.getFont())
                    .pos(CHECKBOX_MAX_X, 0)
                    .selected(format.isStrike())
                    .onValueChange((widget, bool) -> {
                        final var currentFormat = TextFormatListWidget.CONFIG.customFormats.getOrDefault(this.key, TextFormat.EMPTY);
                        TextFormatListWidget.CONFIG.customFormats.put(this.key, currentFormat.withStrike(bool));
                        updateEnchantButton();
                    })
                    .build();
            strikeCheckbox.active = this.isEnabled;
            this.addElement(strikeCheckbox);

            // enabled or disabled: Checkbox
            Checkbox enabledCheckbox = Checkbox.builder(Component.empty(), parent.getFont())
                    .pos(4, 0)
                    .selected(this.isEnabled)
                    .onValueChange((widget, bool) -> {
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
            this.colorEditor.setValue(colorString);
            this.colorEditor.setEditable(this.isEnabled);
            this.colorEditor.setResponder(str -> {
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

    public static class AddEnchantEntry extends me.lonefelidae16.betterlookenchant.client.gui.widget.TextFormatListWidget.EntryBase {
        AddEnchantEntry(BetterLookEnchantConfigScreen parent) {
            EnchantmentEditWidget enchantmentEditWidget = new EnchantmentEditWidget(parent.getFont(), ENCHANT_PREVIEW_WIDGET_X, 0, 110, 20, Component.empty());
            this.addElement(enchantmentEditWidget);

            var widget = Button.builder(Component.translatable(ENTRY_ADD_NEW),
                            new EnchantmentEditButtonCallback(enchantmentEditWidget, "") {
                                @Override
                                public void onPress(Button button) {
                                    super.onPress(button);
                                    AddEnchantEntry.this.setFocused(enchantmentEditWidget);
                                }
                            })
                    .pos(ENCHANT_PREVIEW_WIDGET_X, 0)
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
