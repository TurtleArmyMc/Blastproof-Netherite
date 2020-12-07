package com.turtlearmymc.blastproofnetherite.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
    protected void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> ci) {
        ItemEntity stack = (ItemEntity) (Object) this;
        if (!stack.getStack().isEmpty() && stack.getStack().getItem().isFireproof() && source.isExplosive()) {
            ci.setReturnValue(false);
            ci.cancel();
        }
    }
}
