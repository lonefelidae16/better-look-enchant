package me.lonefelidae16.betterlookenchant.client.gui.screen;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantClient;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.widget.TextFormatListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class BetterLookEnchantConfigScreen extends Screen {
    private static final Text TITLE_TEXT = Text.translatable("text.betterlookenchant.config.title");

    private final Screen parent;
    public final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private TextFormatListWidget list;

    public BetterLookEnchantConfigScreen(Screen parent) {
        super(TITLE_TEXT);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addHeader(TITLE_TEXT, this.textRenderer);

        this.list = new TextFormatListWidget(this.client, this.width, this);
        this.layout.addBody(this.list);

        DirectionalLayoutWidget footer = DirectionalLayoutWidget.horizontal().spacing(8);
        footer.add(
                ButtonWidget.builder(ScreenTexts.CANCEL, (button) -> {
                            BetterLookEnchantConfig.reload();
                            this.close();
                        })
                        .build()
        );
        footer.add(
                ButtonWidget.builder(Text.translatable("text.betterlookenchant.config.save"), (button) -> {
                            BetterLookEnchantConfig.writeFile(BetterLookEnchantClient.STATE_FILE);
                            this.close();
                        })
                        .build()
        );
        this.layout.addFooter(footer);

        this.layout.refreshPositions();
        this.layout.forEachChild(this::addDrawableChild);
    }

    @Override
    protected void refreshWidgetPositions() {
        this.layout.refreshPositions();
        this.list.position(this.width, this.layout);
    }

    public void refresh() {
        this.list.visible = false;
        this.clearChildren();
        this.init();
    }
}
