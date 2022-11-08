package com.tzaranthony.spellbook.core.blocks.block;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EnderBlock extends Block {
    public EnderBlock(float resistance) {
        super(SBBlockProperties.NetheriteBasedBlock(resistance));
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entityIn) {
        if (!level.isClientSide) {
            double d0 = entityIn.getX();
            double d1 = entityIn.getY();
            double d2 = entityIn.getZ();

            if (entityIn instanceof LivingEntity
                    && !(entityIn instanceof EnderMan)
                    && !(entityIn instanceof Endermite)
                    && !ItemStack.isSame(((LivingEntity) entityIn).getItemBySlot(EquipmentSlot.FEET), new ItemStack(SBItems.WITCH_BOOTS.get()))
            ) {
                LivingEntity entityLiving = (LivingEntity) entityIn;
                for (int i = 0; i < 16; ++i) {
                    double d3 = entityLiving.getX() + (entityLiving.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double d4 = Mth.clamp(entityLiving.getY() + (double) (entityLiving.getRandom().nextInt(16) - 8), 0.0D, (double) (level.getHeight() - 1));
                    double d5 = entityLiving.getZ() + (entityLiving.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (entityLiving.isPassenger()) {
                        entityLiving.stopRiding();
                    }

                    if (entityLiving.randomTeleport(d3, d4, d5, true)) {
                        SoundEvent soundevent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                        level.playSound((Player) null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entityLiving.playSound(soundevent, 1.0F, 1.0F);
                        break;
                    }
                }
            }
            super.stepOn(level, pos, state, entityIn);
        }
    }
}