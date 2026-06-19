package org.cdc.mineinabyss.mixin.client;

import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import org.cdc.mineinabyss.utils.L10N;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/19
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

  @Inject(method = "reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"))
  private void doBeforeResourcesPacksLoad(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
    L10N.getCaches().clear();
  }

}
