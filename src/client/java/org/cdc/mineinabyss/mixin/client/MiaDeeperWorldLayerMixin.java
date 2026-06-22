package org.cdc.mineinabyss.mixin.client;

import dev.hintsystem.miacompat.utils.MiaDeeperWorld.LayerInfo;
import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/6/21
 */
@Mixin(LayerInfo.class)
@Pseudo
public class MiaDeeperWorldLayerMixin {

  @Mutable
  @Shadow
  @Final
  public String title;

  @Inject(method = "<init>(Ljava/lang/String;ILjava/lang/String;ILjava/lang/Integer;ILjava/util/List;)V", at = @At("TAIL"))
  private void modifyTitle(String title, int centerX, String startY, int titleColor,
      Integer ascensionEffects, int par6, List par7, CallbackInfo ci) {
    switch (title) {
      case "Orth" -> {
        this.title = "奥斯";
      }
      case "L1" -> {
        this.title = "阿比斯之渊边界";
      }
      case "L2" -> {
        this.title = "诱惑之森";
      }
      case "L3" -> {
        this.title = "大断层";
      }
      case "L4" -> {
        this.title = "巨人之杯";
      }
      case "L5" -> {
        this.title = "亡骸之海";
      }
    }
  }

}
