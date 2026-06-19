package org.cdc.mineinabyss.utils;

import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * &#064;Developer  user &#064;CreatedIn  2026/6/15
 */
public class L10N {

  public static ConcurrentHashMap<Component, MutableComponent> caches = new ConcurrentHashMap<>();

  public static ConcurrentHashMap<Component, MutableComponent> getCaches() {
    return caches;
  }

  public static Language getLanguage() {
    return Language.getInstance();
  }

  public static boolean contains(String key) {
    return getLanguage().has(key);
  }

  public static String textKey(String textKey) {
    return "text.mia." + textKey;
  }

  public static String itemKey(String item, String itemContent) {
    return "item.mia." + item + "." + itemContent;
  }

  public static String npcName(String npcName) {
    return "npc.mia." + npcName + ".display_name";
  }

  public static String overLayerKey(String suffix) {
    return "overlaytext.mia." + suffix;
  }

  public static String hoverShowTextKey(String suffix) {
    return "hoverevent.showtext." + suffix;
  }
}
