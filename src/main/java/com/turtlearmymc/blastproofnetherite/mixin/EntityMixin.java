package com.turtlearmymc.blastproofnetherite.mixin;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

@Mixin(Entity.class)
public abstract class EntityMixin {

    // Makes fireproof items invulnerable to damage
    @Inject(method = "isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z", at = @At("RETURN"), cancellable = true)
    protected void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> ci) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof ItemEntity item) {
            ci.setReturnValue(ci.getReturnValue().booleanValue()
                    || (!item.getStack().isEmpty() && item.isFireImmune() && !source.isOutOfWorld()));
        }
    }

    // Covers the void in a collidable VoxelShape for fireproof items
    @ModifyVariable(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("STORE"))
    List<VoxelShape> adjustMovementForCollisions(List<VoxelShape> list) {
        if ((Object) this instanceof ItemEntity item) {
            if (!item.getStack().isEmpty() && item.isFireImmune()) {
                ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(list.size() + 1);
                builder.addAll(list);
                builder.add(VoxelShapes.cuboid(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                        Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, item.world.getBottomY(),
                        Double.POSITIVE_INFINITY));
                return builder.build();
            }
        }
        return list;
    }
}