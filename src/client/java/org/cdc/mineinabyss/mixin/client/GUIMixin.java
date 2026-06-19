package org.cdc.mineinabyss.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/16
 */
@Mixin(SubtitleOverlay.class)
public class SubtitleMixin {

  @ModifyExpressionValue(method = "render(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/SubtitleOverlay$Subtitle;getText()Lnet/minecraft/network/chat/Component;"))
  private Component modifyText(Component original) {
    var comp = new AtomicReference<MutableComponent>();
    ComponentUtils.visitSingleComponent(original, comp, a -> StringUtils.generateKey(a, false));
    return comp.get();
  }
}
