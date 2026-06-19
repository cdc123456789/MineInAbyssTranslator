package org.cdc.mineinabyss.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.StringUtils;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/16
 */
@Mixin(Gui.class)
public class GUIMixin {

  @Shadow
  private @Nullable Component title;

  @Shadow
  private int titleTime;

  @Shadow
  private @Nullable Component overlayMessageString;

  @Shadow
  private int overlayMessageTime;

  @Shadow
  private ItemStack lastToolHighlight;

  @Inject(method = "renderTitle", at = @At("HEAD"))
  private void modifyTitle(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    var comp = new AtomicReference<MutableComponent>();
    if (this.title != null && titleTime > 0) {
      ComponentUtils.visitSingleComponent(title, comp, a -> StringUtils.generateKey(a, false));
      title = comp.get();
    }

  }

  @Inject(method = "renderOverlayMessage", at = @At("HEAD"))
  private void modifyOverlayerMessage(GuiGraphics guiGraphics, DeltaTracker deltaTracker,
      CallbackInfo ci) {
    if (overlayMessageString != null && overlayMessageTime > 0) {
      overlayMessageString = ComponentUtils.visitSingleComponent(overlayMessageString,
          a -> StringUtils.generateKey(a, false), null, null);
    }
  }

  @ModifyExpressionValue(method = "renderSelectedItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getHoverName()Lnet/minecraft/network/chat/Component;"))
  private Component modifySelectedItemName(Component original) {
    return ComponentUtils.visitSingleComponent(original,
        a -> StringUtils.generateItemKey(
            BuiltInRegistries.ITEM.getKey(lastToolHighlight.getItem()).getPath(), a),
        null, null);
  }
}
