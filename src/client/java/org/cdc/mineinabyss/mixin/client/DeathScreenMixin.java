package org.cdc.mineinabyss.mixin.client;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.StringUtils;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/19
 */
@Mixin(DeathScreen.class)
public class DeathScreenMixin {

  @Mutable
  @Shadow
  @Final
  private @Nullable Component causeOfDeath;

  @Inject(method = "visitText", at = @At("HEAD"))
  private void modifyCause(ActiveTextCollector activeTextCollector, CallbackInfo ci) {
    if (causeOfDeath != null) {
      causeOfDeath = ComponentUtils.visitSingleComponent(causeOfDeath,
          a -> StringUtils.generateKey(a, false), null, null);
    }
  }
}
