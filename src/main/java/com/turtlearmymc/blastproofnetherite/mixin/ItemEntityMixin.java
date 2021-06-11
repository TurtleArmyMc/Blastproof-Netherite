package com.turtlearmymc.blastproofnetherite.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Direction;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    // Teleports fireproof items out of the void to prevent them from being
    // deleted at the start of their tick.
    @Inject(method = "tick()V", at = @At("HEAD"))
    protected void tick(CallbackInfo ci) {
        ItemEntity item = (ItemEntity) (Object) this;
        if (item.isFireImmune() && item.getY() < item.world.getBottomY()) {
            item.setBoundingBox(item.getBoundingBox().offset(0, item.world.getBottomY() - item.getY(), 0));
            item.setPos(item.getX(), item.getBoundingBox().getMin(Direction.Axis.Y), item.getZ());
            item.setVelocity(item.getVelocity().multiply(1, 0, 1));
        }
    }
}
