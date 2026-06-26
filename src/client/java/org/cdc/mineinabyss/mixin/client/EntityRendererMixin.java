package org.cdc.mineinabyss.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.cdc.mineinabyss.utils.ComponentUtils;
import org.cdc.mineinabyss.utils.L10N;
import org.cdc.mineinabyss.utils.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/26
 */
@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

  @Inject(method = "getNameTag", at = @At(value = "RETURN"), cancellable = true)
  private void modifyNameTag(T entity, CallbackInfoReturnable<Component> cir) {
    if (entity.getType() == EntityType.PLAYER || !entity.hasCustomName()) {
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
}
