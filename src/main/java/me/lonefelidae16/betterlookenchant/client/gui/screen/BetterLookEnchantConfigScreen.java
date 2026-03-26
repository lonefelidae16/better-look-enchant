package me.lonefelidae16.betterlookenchant.client.gui.screen;

import me.lonefelidae16.betterlookenchant.BetterLookEnchantClient;
import me.lonefelidae16.betterlookenchant.BetterLookEnchantConfig;
import me.lonefelidae16.betterlookenchant.client.gui.widget.TextFormatListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class BetterLookEnchantConfigScreen extends Screen {
    private static final Component TITLE_TEXT = Component.translatable("text.betterlookenchant.config.title");

    private final Screen parent;
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private TextFormatListWidget list;

    public BetterLookEnchantConfigScreen(Screen parent) {
        super(TITLE_TEXT);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE_TEXT, this.font);

        this.list = new TextFormatListWidget(this.minecraft, this.width, this);
        this.layout.addToContents(this.list);

        LinearLayout footer = LinearLayout.horizontal().spacing(8);
        footer.addChild(
                Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
                            BetterLookEnchantConfig.reload();
                            this.onClose();
                        })
                        .build()
        );
        footer.addChild(
                Button.builder(Component.translatable("text.betterlookenchant.config.save"), (button) -> {
                            BetterLookEnchantConfig.writeFile(BetterLookEnchantClient.STATE_FILE);
                            this.onClose();
                        })
                        .build()
        );
        this.layout.addToFooter(footer);

        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.list.updateSize(this.width, this.layout);
    }

    public void refresh() {
        this.list.visible = false;
        this.clearWidgets();
        this.init();
    }
}
