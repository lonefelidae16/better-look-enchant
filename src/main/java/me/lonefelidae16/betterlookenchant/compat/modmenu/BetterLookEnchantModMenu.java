package me.lonefelidae16.betterlookenchant.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.lonefelidae16.betterlookenchant.compat.modmenu.client.gui.screen.BetterLookEnchantConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class BetterLookEnchantModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BetterLookEnchantConfigScreen::new;
    }

}
