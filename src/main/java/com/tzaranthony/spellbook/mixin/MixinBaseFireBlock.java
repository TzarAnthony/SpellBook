package com.tzaranthony.spellbook.mixin;

import com.tzaranthony.spellbook.core.blocks.fire.EndFire;
import com.tzaranthony.spellbook.registries.SBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public abstract class MixinBaseFireBlock {
    @Inject(at = @At(value = "HEAD"), method = "getState", cancellable = true)
    private static void addEndFire(BlockGetter reader, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        if (EndFire.canSurviveOnBlock(blockstate.getBlock())) {
            cir.cancel();
            cir.setReturnValue(SBBlocks.END_FIRE.get().defaultBlockState());
        }
    }
}