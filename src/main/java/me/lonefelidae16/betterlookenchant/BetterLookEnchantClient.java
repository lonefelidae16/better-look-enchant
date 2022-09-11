package me.lonefelidae16.betterlookenchant;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Environment(EnvType.CLIENT)
public class BetterLookEnchantClient implements ClientModInitializer {
    public static final String MOD_ID = "betterlookenchant";
    public static final Logger LOGGER = LoggerFactory.getLogger(BetterLookEnchantClient.MOD_ID);
    public static final File STATE_FILE = new File("config/" + BetterLookEnchantClient.MOD_ID + ".json");

    @Override
    public void onInitializeClient() {
    }
}
