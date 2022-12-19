package com.tzaranthony.spellbook.core.items.items;

import com.mojang.logging.LogUtils;
import com.tzaranthony.spellbook.core.items.SBItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CoordinationTome extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String LINKED = "Linked";
    private static final String POS = "BlockPos";
    private static final String DIM = "BlockDimension";

    public CoordinationTome() {
        super(SBItemProperties.Standard(Rarity.RARE, 1));
    }

    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack bag = context.getItemInHand();
        CompoundTag tag = bag.getOrCreateTag();
        if (player.isCrouching()) {
            this.setTags(level, blockpos.above(), tag);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.FAIL;
        }
    }

    protected void setTags(Level level, BlockPos pos, CompoundTag tag) {
        if (tag.getBoolean(LINKED) && NbtUtils.readBlockPos(tag.getCompound(POS)) == pos) {
            level.playSound((Player) null, pos, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundSource.PLAYERS, 1.0F, 1.0F);
            tag.remove(POS);
            tag.remove(DIM);
            tag.putBoolean(LINKED, false);
        } else {
            level.playSound((Player) null, pos, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundSource.PLAYERS, 1.0F, 1.0F);
            ResourceKey<Level> key = level.dimension();
            tag.put(POS, NbtUtils.writeBlockPos(pos));
            Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, key).resultOrPartial(LOGGER::error).ifPresent((dimId) -> {
                tag.put(DIM, dimId);
            });
            tag.putBoolean(LINKED, true);
        }
    }

    public static boolean isLinked(CompoundTag tag) {
        return tag.contains(LINKED) && tag.getBoolean(LINKED);
    }

    public static Optional<ResourceKey<Level>> getTPDim(CompoundTag tag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get(DIM)).result();
    }

    public static BlockPos getTPPos(CompoundTag tag) {
        return NbtUtils.readBlockPos(tag.getCompound(POS));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getOrCreateTag();
        if (CoordinationTome.isLinked(tag)) {
            BlockPos pos = CoordinationTome.getTPPos(tag);
            Optional<ResourceKey<Level>> dim = CoordinationTome.getTPDim(tag);
            tooltip.add((new TranslatableComponent("tooltip.spellbook.coord_tome")).append(dim.get().location().getPath()));
            tooltip.add((new TranslatableComponent("tooltip.spellbook.coord_tome1")).append(pos.toShortString()));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}