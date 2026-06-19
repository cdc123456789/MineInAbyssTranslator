package org.cdc.mineinabyss.mixin.client;

import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.BossEvent;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.L10N;
import org.cdc.mineinabyss.utils.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/16
 */
@Mixin(BossEvent.class)
public class BossEventMixin {

  @Shadow
  protected Component name;

  @Inject(method = "getName()Lnet/minecraft/network/chat/Component;", at = @At("HEAD"), cancellable = true)
  private void modifyStaminaName(CallbackInfoReturnable<Component> cir) {
    var component = name;
    if (component == null) {
      return;
    }
    if (L10N.getCaches().containsKey(component)) {
      cir.setReturnValue(L10N.getCaches().get(component));
    } else {
      var comp = new AtomicReference<MutableComponent>();
      ComponentUtils.visitSingleComponent(component, comp, StringUtils::generateNPCNameKey);
      cir.setReturnValue(comp.get());
      L10N.getCaches().put(component, comp.get());
    }
  }

}
