package me.lonefelidae16.betterlookenchant.client.gui.screen;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantClient;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.widget.ColoredButtonWidget;
import me.lonefelidae16.betterlookenchant.client.gui.widget.TextFormatListWidget;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
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
                ButtonWidget.builder(ScreenTexts.CANCEL, (button) -> {
                            BetterLookEnchantConfig.reload();
                            this.client.setScreen(this.parent);
                        })
                        .position(this.width / 2 - 154, this.height - 28)
                        .build()
        );

        // Save button
        this.addDrawableChild(
                ButtonWidget.builder(Text.translatable("text.betterlookenchant.config.save"), (button) -> {
                            BetterLookEnchantConfig.writeFile(BetterLookEnchantClient.STATE_FILE);
                            this.client.setScreen(this.parent);
                        })
                        .position(this.width / 2 + 4, this.height - 28)
                        .build()
        );

        // Restore Default button
        this.addDrawableChild(
                new ColoredButtonWidget.Builder(Text.translatable("text.betterlookenchant.config.restore_default"), (button) -> {
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
                })
                        .textColor(0xFF9090)
                        .backgroundTint(Color.MC_DARK_RED.argb())
                        .dimensions(this.width - 120 - 4, 4, 120, 20)
                        .build()
        );

        this.textFormatListWidget = new TextFormatListWidget(this.client, this.width, this.height, 32, this.height - 36, 62, this);
        this.addSelectableChild(this.textFormatListWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.textFormatListWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 12, Color.WHITE.argb());
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void refresh() {
        this.clearChildren();
        this.init();
    }
}
