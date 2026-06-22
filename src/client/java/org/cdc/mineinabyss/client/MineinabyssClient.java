package org.cdc.mineinabyss.client;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent.ShowText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.mineinabyss.Mineinabyss;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.L10N;
import org.cdc.mineinabyss.utils.StringUtils;

public class MineinabyssClient implements ClientModInitializer {

  public static final Logger LOG = LogManager.getLogger(MineinabyssClient.class);

  KeyMapping.Category CONVENIENCE = KeyMapping.Category.register(
      Identifier.fromNamespaceAndPath(Mineinabyss.MOD_ID, "convenience")
  );

  KeyMapping suicide = KeyBindingHelper.registerKeyBinding(
      new KeyMapping(
          "key.mineinabysstranslator.killme", // The translation key for the key mapping.
          InputConstants.Type.KEYSYM,
          // The type of the keybinding; KEYSYM for keyboard, MOUSE for mouse.
          -1, // The GLFW keycode of the key.
          this.CONVENIENCE // The category of the mapping.
      ));
  KeyMapping toggleClimb = KeyBindingHelper.registerKeyBinding(
      new KeyMapping(
          "key.mineinabysstranslator.toggleclimb", // The translation key for the key mapping.
          InputConstants.Type.KEYSYM,
          // The type of the keybinding; KEYSYM for keyboard, MOUSE for mouse.
          -1, // The GLFW keycode of the key.
          this.CONVENIENCE // The category of the mapping.
      ));

  @Override
  public void onInitializeClient() {

    ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
      if (isNotConnectedToAbyss()) {
        return;
      }
      while (suicide.consumeClick()) {
        minecraft.getConnection().sendCommand("suicide");
      }
      while (toggleClimb.consumeClick()) {
        minecraft.getConnection().sendCommand("climb");
      }
    });

    AttackEntityCallback.EVENT.register(
        (player, level, interactionHand, entity, entityHitResult) -> {
          // TODO 玩家攻击生物，告知相关数据
          LOG.info(entity);
          return InteractionResult.PASS;
        });
    UseEntityCallback.EVENT.register((player, level, interactionHand, entity, entityHitResult) -> {
      LOG.info(entity);
      return InteractionResult.PASS;
    });

    ClientReceiveMessageEvents.ALLOW_CHAT.register(
        ((message, signedMessage, sender, params, receptionTimestamp) -> {
          if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(
                Component.literal("!").withStyle(style -> {
                  if (signedMessage != null) {
                    return style.withHoverEvent(new ShowText(signedMessage.decoratedContent()));
                  }
                  return style;
                }).append(message), false);
          }
          return false;
        }));

    ClientReceiveMessageEvents.MODIFY_GAME.register((component, b) -> {
      LOG.info("[GameMessage {}] {}", b ? "overlayer" : "", component);
      if (isNotConnectedToAbyss()) {
        return component;
      }
      AtomicBoolean translated = new AtomicBoolean(false);
      MutableComponent comp =
          ComponentUtils.visitSingleComponent(component, a -> StringUtils.generateKey(a, b),
              a -> translated.set(true), null);

      if (translated.get()) {
        comp.append(
            Component.literal(" [!]")
                .withStyle(a -> a.withHoverEvent(new ShowText(component)).withColor(
                    ChatFormatting.GOLD)));
        LOG.info("[Game message modified] {}", comp);
      }
      return comp;
    });

    ItemTooltipCallback.EVENT.register(((itemStack, tooltipContext, tooltipFlag, list) -> {
      if (Minecraft.getInstance().hasShiftDown() || isNotConnectedToAbyss()) {
        return;
      }
      if (itemStack.has(DataComponents.ITEM_MODEL)) {
        var itemPath = BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getPath();
        var translated = new AtomicBoolean(false);
        for (int index = 0; index < list.size(); index++) {
          var component = list.get(index);
          var plain = component.getString();
          if (plain.startsWith("minecraft:")) {
            continue;
          }
          MutableComponent comp;
          if (L10N.getCaches().containsKey(component)) {
            comp = L10N.getCaches().get(component);
            translated.set(true);
          } else {
            comp = ComponentUtils.visitSingleComponent(component,
                a -> StringUtils.generateItemKey(itemPath, a), a -> translated.set(true), null);
            if (translated.get()) {
              L10N.getCaches().put(component, comp);
            }
          }
          list.set(index, comp);
        }
        if (translated.get()) {
          list.add(Component.translatable("item.mia.tip_me_shift"));
        }
      }
    }));

  }

  public static boolean isNotConnectedToAbyss() {
    return Minecraft.getInstance().getConnection() == null
        || Minecraft.getInstance().getConnection().getServerData() == null
        || !Minecraft.getInstance()
        .getConnection().getServerData().ip.contains("mineinabyss.com");
  }
}
