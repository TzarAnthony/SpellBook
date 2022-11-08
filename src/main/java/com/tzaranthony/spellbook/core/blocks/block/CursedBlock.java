package com.tzaranthony.spellbook.core.blocks.block;

import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import com.tzaranthony.spellbook.core.util.damage.SBDamageSource;
import com.tzaranthony.spellbook.registries.SBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CursedBlock extends Block {
    public CursedBlock(float resistance) {
        super(SBBlockProperties.NetheriteBasedBlock(resistance));
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entityIn) {
        if (entityIn instanceof LivingEntity
                && !ItemStack.isSame(((LivingEntity) entityIn).getItemBySlot(EquipmentSlot.FEET), new ItemStack(SBItems.NECROMANCER_BOOTS.get()))) {
            entityIn.hurt(SBDamageSource.CURSE, 10.0F);
            if (entityIn instanceof Player
                    && ((Player) entityIn).isCrouching()) {
                entityIn.hurt(SBDamageSource.CURSE, 5.0F);
            }
        }
        super.stepOn(level, pos, state, entityIn);
    }
}