package org.cdc.mineinabyss;

import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/17
 */
public abstract class AbyssEventDetector {

  private static final Logger LOG = LogManager.getLogger(AbyssEventDetector.class);

  public enum DynamicText {
    FOUND_VEIN(startsWithFunction("You found a hidden vein!"), findVein()), WELCOME(
        containsFunction("Welcome"), null), JOIN_OR_LEAVE(
        a -> StringUtils.isJoinOrLeaveMessage(a.getString()), null), CHAT_MESSAGE(
        StringUtils::isChatMessage, tryToTranslateChat());

    private final Function<Component, Boolean> matchFunction;
    private final Function<Component, MutableComponent> mutableComponentFunction;

    DynamicText(@NotNull Function<Component, Boolean> matchFunction,
        Function<Component, MutableComponent> mutableComponentFunction) {
      this.matchFunction = matchFunction;
      this.mutableComponentFunction = mutableComponentFunction;
    }

    public MutableComponent getMutableComponent(Component component) {
      if (mutableComponentFunction == null) {
        return (MutableComponent) component;
      }
      var result = mutableComponentFunction.apply(component);
      if (!result.equals(component)) {
        LOG.info("[Modified Chat] {}", result);
      }
      return result;
    }
  }

  public static boolean isDynamicText(Component component) {
    return getDynamicText(component) != null;
  }

  public static DynamicText getDynamicText(Component component) {
    for (DynamicText value : DynamicText.values()) {
      if (value.matchFunction.apply(component)) {
        return value;
      }
    }
    return null;
  }

  // origin:
  public static Function<Component, MutableComponent> findVein() {
    return a -> {
      String plain = a.getString().replace("You found a hidden vein! ", "");
      return Component.literal("你找到了一条隐藏矿脉！").append(
          Component.literal(plain).withStyle(style -> style.withColor(ChatFormatting.BLUE)));
    };
  }

  public static Function<Component, MutableComponent> tryToTranslateChat() {
    return a -> {
      var com = ComponentUtils.visitSingleComponent(a, b -> StringUtils.generateKey(b, false), null,
          null);
      return com;
    };
  }


  public static Function<Component, Boolean> startsWithFunction(String string) {
    return component -> component.getString().startsWith(string);
  }

  public static Function<Component, Boolean> containsFunction(String string) {
    return component -> component.getString().contains(string);
  }
}
