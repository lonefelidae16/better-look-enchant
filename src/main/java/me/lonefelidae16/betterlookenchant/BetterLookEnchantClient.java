package me.lonefelidae16.betterlookenchant;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Environment(EnvType.CLIENT)
public class BetterLookEnchantClient implements ClientModInitializer {
    public static final String MOD_ID = "betterlookenchant";
    public static final Logger LOGGER = LoggerFactory.getLogger(BetterLookEnchantClient.class);
    public static final File STATE_FILE = FabricLoader.getInstance().getConfigDir().resolve(BetterLookEnchantClient.MOD_ID + ".json").toFile();

    @Override
    public void onInitializeClient() {
    }
}
