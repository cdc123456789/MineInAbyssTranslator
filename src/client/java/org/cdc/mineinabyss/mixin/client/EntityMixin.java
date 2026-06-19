package org.cdc.mineinabyss.mixin.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.L10N;
import org.cdc.mineinabyss.utils.StringUtils;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Developer  user &#064;CreatedIn  2026/6/16
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

  @Shadow
  public abstract EntityType<?> getType();

  @Shadow
  public abstract boolean hasCustomName();

  @Shadow
  public abstract @Nullable Component getCustomName();

  @Inject(method = "getDisplayName()Lnet/minecraft/network/chat/Component;", at = @At(value = "RETURN"), cancellable = true)
  private void modifierDisplayName(CallbackInfoReturnable<Component> cir) {
    if (getType() == EntityType.PLAYER || !hasCustomName()) {
      return;
    }
    var component = cir.getReturnValue();
    if (L10N.getCaches().containsKey(component)) {
      cir.setReturnValue(L10N.getCaches().get(component));
    } else {
      var comp = ComponentUtils.visitSingleComponent(component, StringUtils::generateNPCNameKey,
          null, null, false);
      cir.setReturnValue(comp);
      L10N.getCaches().put(component, comp);
    }
  }

  @Inject(method = "getCustomName()Lnet/minecraft/network/chat/Component;", at = @At(value = "RETURN"), cancellable = true)
  private void modifierCustomName(CallbackInfoReturnable<Component> cir) {
    if (getType() == EntityType.PLAYER || !hasCustomName()) {
      return;
    }
    var component = cir.getReturnValue();
    if (component == null) {
      return;
    }
    if (L10N.getCaches().containsKey(component)) {
      cir.setReturnValue(L10N.getCaches().get(component));
    } else {
      var comp = ComponentUtils.visitSingleComponent(component, StringUtils::generateNPCNameKey,
          null, null, false);
      cir.setReturnValue(comp);
      L10N.getCaches().put(component, comp);
    }
  }

//  @Inject(method = "shouldRender",at = @At("HEAD"),cancellable = true)
//  private void shouldRenderModify(double d, double e, double f, CallbackInfoReturnable<Boolean> cir){
//    if (getCustomName().getString())
//  }
}
