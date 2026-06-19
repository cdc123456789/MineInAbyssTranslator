package org.cdc.mineinabyss.utils;

import java.util.Locale;

/**
 * &#064;Developer  user &#064;CreatedIn  2026/6/15
 */
public class StringUtils {

  public static String generateKey(String content, boolean overlay) {
    var keySuffix = content.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
    if (overlay) {
      return "overlaytext.mia." + keySuffix;
    }
    return "text.mia." + keySuffix;
  }

  /**
   * 检查字符串是否仅包含英文字母、空格和英文标点符号。
   * <p>默认允许的标点：. , ! ? ; : ' " ( ) -</p>
   * 但纯标点符号则返回false
   * <p>数字、汉字、特殊符号（如@#$%^&*）等均不允许。</p>
   *
   * @param str 待检查的字符串，可能为 null
   * @return 若字符串非空且全部由允许的字符组成则返回 true，否则 false
   */
  public static boolean isAllEnglish(String str) {
    if (str == null || str.isEmpty()) {
      return false;
    }
    // 整个字符串只允许字母、空格、指定标点，且至少含有一个字母
    return str.matches("^(?=.*[A-Za-z])[A-Za-z\\s.…,!?;:'\"()\\-]+$");
  }
}
