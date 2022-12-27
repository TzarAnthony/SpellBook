package com.tzaranthony.spellbook.core.items.items;

import com.tzaranthony.spellbook.core.entities.other.CursedPainting;
import com.tzaranthony.spellbook.core.items.SBItemProperties;
import com.tzaranthony.spellbook.registries.SBEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class CursedPaintingItem extends Item {
    public CursedPaintingItem() {
        super(SBItemProperties.Standard());
    }

    public InteractionResult useOn(UseOnContext p_41331_) {
        EntityType<? extends CursedPainting> type = SBEntities.CURSED_PAINTING.get();
        BlockPos blockpos = p_41331_.getClickedPos();
        Direction direction = p_41331_.getClickedFace();
        BlockPos blockpos1 = blockpos.relative(direction);
        Player player = p_41331_.getPlayer();
        ItemStack itemstack = p_41331_.getItemInHand();
        if (player != null && !this.mayPlace(player, direction, itemstack, blockpos1)) {
            return InteractionResult.FAIL;
        } else {
            Level level = p_41331_.getLevel();
            CursedPainting painting = new CursedPainting(type, level);
            painting.initPainting(level, blockpos1, direction);

            CompoundTag compoundtag = itemstack.getTag();
            if (compoundtag != null) {
                EntityType.updateCustomEntityTag(level, player, painting, compoundtag);
            }

            if (painting.survives()) {
                if (!level.isClientSide) {
                    painting.playPlacementSound();
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, blockpos);
                    level.addFreshEntity(painting);
                }

                itemstack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    protected boolean mayPlace(Player p_41326_, Direction p_41327_, ItemStack p_41328_, BlockPos p_41329_) {
        return !p_41327_.getAxis().isVertical() && p_41326_.mayUseItemAt(p_41329_, p_41327_, p_41328_);
    }
}