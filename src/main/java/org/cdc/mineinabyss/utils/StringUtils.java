package org.cdc.mineinabyss.utils;

import java.util.Locale;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * &#064;Developer  user &#064;CreatedIn  2026/6/15
 */
public class StringUtils {

  private static final Logger LOG = LogManager.getLogger(StringUtils.class);


  public static String generateKey(String content, boolean overlay) {
    var keySuffix = toFKey(content);
    if (overlay) {
      return L10N.overLayerKey(keySuffix);
    }
    return L10N.textKey(keySuffix);
  }

  public static String generateItemKey(String item, String content) {
    var keySuffix = toFKey(content);
    return L10N.itemKey(item, keySuffix);
  }

  public static String generateNPCNameKey(String name) {
    var keySuffix = toFKey(name);
    return L10N.npcName(keySuffix);
  }

  public static String generateHoverKey(String text) {
    var keySuffix = toFKey(text);
    return L10N.hoverShowTextKey(keySuffix);
  }

  private static String toFKey(String text) {
    var split = text.split("(\n)");
    return split[split.length - 1].trim().toLowerCase(Locale.ROOT).replaceAll("\\s", "_");
  }

  /**
   * 检查字符串是否包含汉字（CJK统一表意文字）。
   * <p>只要包含任意一个汉字字符，即认为该字符串是中文内容（已翻译），返回 {@code true}。</p>
   * <p>若字符串为 {@code null} 或空，或没有任何汉字，则返回 {@code false}。</p>
   * <p>注意：此方法不再检查英文字母、标点等，仅判断汉字存在性。</p>
   *
   * @param str 待检查的字符串，可能为 {@code null}
   * @return 包含汉字则返回 {@code true}，否则 {@code false}
   */
  public static boolean isAllEnglish(String str) {
    if (str == null || str.isEmpty()) {
      return false;
    }

    boolean hasChinese = str.codePoints()
        .anyMatch(cp -> Character.UnicodeScript.of(cp) == Character.UnicodeScript.HAN);

    if (str.matches("\\d+")) {
      return false;
    }

    LOG.debug("[check Chinese presence] {} -> {}", str, hasChinese);
    return !hasChinese;   // 有汉字即返回 false
  }

  public static boolean isJoinOrLeaveMessage(String content) {
    return (content.startsWith("+") || content.startsWith("-")) && content.contains(" ┃ ");
  }

  public static boolean isChatMessage(Component component) {
    if (component.getString().startsWith("!") && component.getString().contains(": ")) {
      var str = component.toString();
      if (str.contains("literal{Time since last death: }[style={color=light_purple}")
          && str.contains("command=/mia profile ")) {
        return true;
      }
    }
    return false;
  }

  public static int parsePingLength(String message) {
    return switch (message) {
      case String s when s.startsWith("du du du du dum") -> 5;
      case String s when s.startsWith("du du du dum") -> 4;
      case String s when s.startsWith("du du dum") -> 3;
      case String s when s.startsWith("du dum") -> 2;
      case String s when s.startsWith("dum") -> 1;
      default -> 0;
    };
  }
}
