package org.cdc.mineinabyss;

import java.util.HashSet;
import java.util.Set;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Logger;

public class Mineinabyss implements ModInitializer {

  public static final String MOD_ID = "mineinabysstranslator";

  private static final Set<String> MISSING_KEYS = new HashSet<>();

  @Override
  public void onInitialize() {

  }

  public static void LogUntranslatedKey(Logger LOG, String key, String text) {
    if (!MISSING_KEYS.contains(key)) {
      LOG.info("Missing key: \"{}\":\"{}\"", key, text);
      MISSING_KEYS.add(key);
    }
  }
}
