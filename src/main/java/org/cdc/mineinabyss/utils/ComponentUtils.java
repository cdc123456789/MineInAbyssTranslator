package org.cdc.mineinabyss.utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.chat.ClickEvent.SuggestCommand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.HoverEvent.ShowText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.mineinabyss.AbyssEventDetector;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/16
 */
public class ComponentUtils {

  private static final Logger LOG = LogManager.getLogger(ComponentUtils.class);

  public static void visitSingleComponent(Component component,
      AtomicReference<MutableComponent> comp,
      Function<String, String> keyFunction) {
    comp.set(visitSingleComponent(component, keyFunction, null, null));
  }

  /**
   *
   * @param component 原始组件，等待翻译
   * @param keyFunction 提供键值的方法
   * @param translated 当翻译成功，就会调用
   * @param postModifier 会对每一个翻译还是没翻译的组件进行
   * @return 翻译完成的组件
   */
  public static MutableComponent visitSingleComponent(Component component
      , Function<String, String> keyFunction, Consumer<String> translated,
      Consumer<Component> postModifier) {
    return visitSingleComponent(component, keyFunction, translated, postModifier, true);
  }

  public static MutableComponent visitSingleComponent(Component component
      , Function<String, String> keyFunction, Consumer<String> translated,
      Consumer<Component> postModifier, boolean checkWholeEnglish) {
    if (AbyssEventDetector.isDynamicText(component)) {
      var d = AbyssEventDetector.getDynamicText(component);
      LOG.info("Dynamic type: {}", d.name());
      return d.getMutableComponent(component);
    }
    return visitSingleComponentOrigin(component, keyFunction, translated, postModifier,
        checkWholeEnglish);
  }

  public static MutableComponent visitSingleComponentOrigin(Component component
      , Function<String, String> keyFunction, Consumer<String> translated,
      Consumer<Component> postModifier, boolean checkWholeEnglish) {
    if (component == null) {
      return null;
    }
    MutableComponent comp;
    if (component.getContents() instanceof LiteralContents(String text)) {
      if ((StringUtils.isAllEnglish(text) || !checkWholeEnglish) && text.length() != 1) {
        var key = keyFunction.apply(text);
        var generalKey = StringUtils.generateKey(text, false);
        if (component.getStyle().getClickEvent() instanceof SuggestCommand(String command)
            && command.startsWith("/tell ")) {
          comp = component.plainCopy()
              .setStyle(component.getStyle().withFont(FontDescription.DEFAULT));
        } else if (L10N.contains(key)) {
          comp = Component.literal(L10N.getLanguage().getOrDefault(key))
              .setStyle(component.getStyle().withFont(FontDescription.DEFAULT));
          if (translated != null) {
            translated.accept(key);
          }
        } else if (L10N.contains(generalKey)) {
          comp = Component.literal(L10N.getLanguage().getOrDefault(generalKey))
              .setStyle(component.getStyle().withFont(FontDescription.DEFAULT));
          if (translated != null) {
            translated.accept(generalKey);
          }
        } else {
          comp = component.plainCopy().setStyle(component.getStyle());
          LOG.info("Missing key: \"{}\":\"{}\"", key, text);
        }
      } else {
        comp = Component.literal(text).setStyle(component.getStyle());
      }

      if (component.getStyle().getHoverEvent() instanceof ShowText(Component text1)) {
        comp.withStyle(
            style -> style.withHoverEvent(new ShowText(visitSingleComponent(text1,
                StringUtils::generateHoverKey, translated, postModifier))));
      }
    } else if (component.getContents() instanceof TranslatableContents translatableContents) {
      var args = translatableContents.getArgs();
      var modified = new Object[args.length];
      for (int index = 0; index < args.length; index++) {
        var arg = args[index];
        if (arg instanceof Component arss) {
          modified[index] = visitSingleComponent(arss, keyFunction, translated, postModifier);
        } else {
          modified[index] = arg;
        }
      }
      comp = Component.translatable(translatableContents.getKey(), modified);
    } else {
      comp = component.plainCopy().setStyle(component.getStyle());
    }

    if (postModifier != null) {
      postModifier.accept(comp);
    }
    visitComponentSiblings(component, comp, keyFunction, translated, postModifier,
        checkWholeEnglish);
    return comp;
  }

  private static void visitComponentSiblings(Component component, MutableComponent comp,
      Function<String, String> keyFunction, Consumer<String> translated,
      Consumer<Component> postModifier, boolean checkWholeEnglish) {
    for (Component sibling : component.getSiblings()) {
      LOG.debug(sibling.getClass().getName());
      MutableComponent mutableComponent;
      if (sibling.getContents() instanceof LiteralContents(String text)) {
        if ((StringUtils.isAllEnglish(text) || !checkWholeEnglish) && text.length() != 1) {
          var key = keyFunction.apply(text);
          var generalKey = StringUtils.generateKey(text, false);
          if (component.getStyle().getClickEvent() instanceof SuggestCommand(String command)
              && command.startsWith("/tell ")) {
            mutableComponent = sibling.plainCopy()
                .setStyle(component.getStyle().withFont(FontDescription.DEFAULT));
          } else if (L10N.contains(key)) {
            mutableComponent = Component.literal(L10N.getLanguage().getOrDefault(key))
                .setStyle(sibling.getStyle().withFont(FontDescription.DEFAULT));
            if (translated != null) {
              translated.accept(key);
            }
          } else if (L10N.contains(generalKey)) {
            mutableComponent = Component.literal(L10N.getLanguage().getOrDefault(generalKey))
                .setStyle(sibling.getStyle().withFont(FontDescription.DEFAULT));
            if (translated != null) {
              translated.accept(generalKey);
            }
          } else {
            mutableComponent = sibling.plainCopy().setStyle(sibling.getStyle());
            LOG.info("Missing sibling key: \"{}\":\"{}\"", key, text);
          }
        } else {
          mutableComponent = sibling.plainCopy().withStyle(sibling.getStyle());
        }

        if (sibling.getStyle().getHoverEvent() instanceof ShowText(Component text1)) {
          mutableComponent.withStyle(
              style -> style.withHoverEvent(new ShowText(visitSingleComponent(text1,
                  StringUtils::generateHoverKey, translated, postModifier))));
        }
      } else if (sibling.getContents() instanceof TranslatableContents translatableContents) {
        var args = translatableContents.getArgs();
        var modified = new Object[args.length];
        for (int index = 0; index < args.length; index++) {
          var arg = args[index];
          if (arg instanceof Component ass) {
            modified[index] = visitSingleComponent(ass, keyFunction, translated,
                postModifier);
          } else {
            modified[index] = arg;
          }
        }
        mutableComponent = Component.translatable(translatableContents.getKey(), modified);
      } else {
        mutableComponent = sibling.plainCopy().withStyle(sibling.getStyle());
      }

      comp.append(mutableComponent);
      if (postModifier != null) {
        postModifier.accept(mutableComponent);
      }
      visitComponentSiblings(sibling, mutableComponent, keyFunction, translated, postModifier,
          checkWholeEnglish);
    }
  }
}
