package me.lonefelidae16.betterlookenchant;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import me.lonefelidae16.betterlookenchant.client.gui.Color;
import me.lonefelidae16.betterlookenchant.client.gui.TextFormat;
import net.minecraft.enchantment.Enchantments;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetterLookEnchantConfig {
    public final Map<String, TextFormat> customFormats;
    public final List<String> enabledEnchants;

    public static final String ENTRY_KEY_DEFAULT_FORMAT = "text.betterlookenchant.config.key.default_format";
    public static final String ENTRY_KEY_LV_MAX_FORMAT = "text.betterlookenchant.config.key.lv_max_format";

    private static final BetterLookEnchantConfig CONFIG;

    static {
        CONFIG = BetterLookEnchantConfig.readFile(BetterLookEnchantClient.STATE_FILE);
    }

    private BetterLookEnchantConfig() {
        // set up defaults
        enabledEnchants = new ArrayList<>();
        enabledEnchants.add(ENTRY_KEY_LV_MAX_FORMAT);
        enabledEnchants.add(Enchantments.MENDING.getValue().toString());
        enabledEnchants.add(Enchantments.INFINITY.getValue().toString());
        customFormats = new HashMap<>();
        customFormats.put(ENTRY_KEY_LV_MAX_FORMAT, new TextFormat(Color.MC_GREEN.argb()));
        customFormats.put(Enchantments.MENDING.getValue().toString(), new TextFormat(Color.MC_GOLD.argb()));
        customFormats.put(Enchantments.INFINITY.getValue().toString(), new TextFormat(Color.MC_LIGHT_PURPLE.argb()));
    }

    public static void reload() {
        CONFIG.from(BetterLookEnchantConfig.readFile(BetterLookEnchantClient.STATE_FILE));
    }

    public static void restoreDefault() {
        CONFIG.from(new BetterLookEnchantConfig());
    }

    private void from(BetterLookEnchantConfig other) {
        if (other == null) {
            return;
        }
        this.customFormats.clear();
        if (other.customFormats != null) {
            this.customFormats.putAll(other.customFormats);
        }
        this.enabledEnchants.clear();
        if (other.enabledEnchants != null) {
            this.enabledEnchants.addAll(other.enabledEnchants);
        }
    }

    public static BetterLookEnchantConfig getConfig() {
        return CONFIG;
    }

    public static void writeFile(File destination) {
        if (destination == null) {
            return;
        }

        try (FileWriter writer = new FileWriter(destination)) {
            writer.write(new Gson().toJson(CONFIG));
            writer.flush();
        } catch (IOException ex) {
            BetterLookEnchantClient.LOGGER.error("Could not write to file", ex);
        }
    }

    public static BetterLookEnchantConfig readFile(File destination) {
        if (destination == null || !destination.exists()) {
            return new BetterLookEnchantConfig();
        }

        try (FileReader reader = new FileReader(destination)) {
            BetterLookEnchantConfig loaded = new Gson().fromJson(reader, BetterLookEnchantConfig.class);
            if (loaded == null) {
                BetterLookEnchantClient.LOGGER.error("Gson#fromJson returns null", new JsonParseException("null"));
                return new BetterLookEnchantConfig();
            }
            return loaded;
        } catch (IOException ex) {
            BetterLookEnchantClient.LOGGER.error("Failed to read from file", ex);
        } catch (JsonParseException ex) {
            BetterLookEnchantClient.LOGGER.error("Failed to parse Json format", ex);
        }
        return new BetterLookEnchantConfig();
    }
}
