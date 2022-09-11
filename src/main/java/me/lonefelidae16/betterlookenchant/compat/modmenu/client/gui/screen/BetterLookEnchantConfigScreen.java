package me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.screen;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantClient;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget.ColoredButtonWidget;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.widget.TextFormatListWidget;
import me.lonefelidae16.betterlookenchant.gui.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class BetterLookEnchantConfigScreen extends Screen {
    private final Screen parent;
    private TextFormatListWidget textFormatListWidget;

    public BetterLookEnchantConfigScreen(Screen parent) {
        super(Text.translatable("text.betterlookenchant.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Cancel button
        this.addDrawableChild(
                new ButtonWidget(this.width / 2 - 154, this.height - 28, 150, 20, ScreenTexts.CANCEL,
                        (button) -> {
                            BetterLookEnchantConfig.reload();
                            this.client.setScreen(this.parent);
                        }
                )
        );

        // Save button
        this.addDrawableChild(
                new ButtonWidget(this.width / 2 + 4, this.height - 28, 150, 20,
                        Text.translatable("text.betterlookenchant.config.key.save"),
                        (button) -> {
                            BetterLookEnchantConfig.writeFile(BetterLookEnchantClient.STATE_FILE);
                            this.client.setScreen(this.parent);
                        }
                )
        );

        // Restore Default button
        ColoredButtonWidget restoreDefaultButton = new ColoredButtonWidget(this.width - 120 - 4, 4, 120, 20,
                Text.translatable("text.betterlookenchant.config.key.restore_default"),
                (button) -> {
                    this.client.setScreen(
                            new ConfirmScreen(
                                    bool -> {
                                        if (bool) {
                                            BetterLookEnchantConfig.restoreDefault();
                                        }
                                        this.client.setScreen(this);
                                    },
                                    Text.translatable("text.betterlookenchant.config.restore.title"),
                                    Text.translatable("text.betterlookenchant.config.restore.confirmation")
                            )
                    );
                }
        );
        restoreDefaultButton.setBackgroundTint(Color.MC_DARK_RED.argb());
        this.addDrawableChild(restoreDefaultButton);

        this.textFormatListWidget = new TextFormatListWidget(this.client, this.width, this.height, 32, this.height - 36, 62, this);
        this.addSelectableChild(this.textFormatListWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.textFormatListWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, Color.WHITE.argb());
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void refresh() {
        this.clearChildren();
        this.init();
    }
}
