package com.tzaranthony.spellbook.core.items.otherEquipment;

import com.mojang.logging.LogUtils;
import com.tzaranthony.spellbook.core.containers.menus.providers.BagMenuProvider;
import com.tzaranthony.spellbook.core.items.SBItemProperties;
import com.tzaranthony.spellbook.registries.SBVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BuilderBag extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    private static final int size = 3;
    private static final String ITEMS = "Items";
    private static final String RAND = "seed";
    private static final String P1 = "Probability1";
    private static final String P2 = "Probability2";
    private static final String LINKED = "Linked";
    private static final String CHESTPOS = "ContainerPos";
    private static final String CHESTDIM = "ContainerDimension";

    public BuilderBag() {
        super(SBItemProperties.Standard(Rarity.RARE, 1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack bag = player.getItemInHand(hand);
        if (!level.isClientSide) {
            openBag((ServerPlayer) player, bag);
            return InteractionResultHolder.success(bag);
        } else {
            playOpenSound(player);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    public void openBag(ServerPlayer player, ItemStack builderBag) {
        if (player.containerMenu != player.inventoryMenu) {
            player.closeContainer();
        }
        player.openMenu(new BagMenuProvider(player, builderBag, this.size));
    }

    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack bag = context.getItemInHand();
        CompoundTag tag = bag.getOrCreateTag();
        if (level.getBlockState(blockpos).is(Blocks.CHEST) && player.isCrouching()) {
            level.playSound((Player) null, blockpos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            this.setChestTags(level.dimension(), blockpos, tag);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            long seed = tag.contains(RAND) ? tag.getLong(RAND) : 0;
            Random random = new Random(seed);
            tag.putLong(RAND, random.nextLong());
            int selector = random.nextInt(100);
            if (tag.contains(ITEMS)) {
                NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(tag, items);
                int slot = selectSlot(tag, selector, items);
                ItemStack pStack = items.get(slot);
                BlockPlaceContext uoc = new BagUseOnContext(context, pStack);
                InteractionResult result = pStack.getItem().useOn(uoc);
                if (result.consumesAction() && !level.isClientSide()) {
                    pStack.shrink(1);
                    tryChestRefresh(tag, items, (ServerLevel) level);
                    ContainerHelper.saveAllItems(tag, items);
                }
                return result;
            }
            return InteractionResult.FAIL;
        }
    }

    public int selectSlot(CompoundTag tag, int selector, NonNullList<ItemStack> items) {
        boolean t2 = tag.contains(P1);
        boolean t3 = tag.contains(P2);
        float p1 = 15;
        float p2 = 15;

        if (t2) {
            p1 = tag.getInt(P1);
        } else if (t3) {
            p2 += tag.getInt(P2);
        }

        if (!items.get(2).isEmpty() && p2 >= selector) {
            return 2;
        } else if (!items.get(1).isEmpty() && (p2 + p1) >= selector) {
            return 1;
        }
        return 0;
    }

    public void tryChestRefresh(CompoundTag tag, NonNullList<ItemStack> items, ServerLevel level) {
        if (tag.getBoolean(LINKED) && getChestDim(tag).get() == level.dimension()) {
            BlockPos pos = NbtUtils.readBlockPos(tag.getCompound(CHESTPOS));
            BlockState state = level.getBlockState(pos);
            if (state.hasBlockEntity() && state.is(Blocks.CHEST)) {
                ChestBlockEntity ce = ((ChestBlockEntity) level.getBlockEntity(pos));
                getSlots(ce).anyMatch((slot) -> {
                    return tryTakeInItemFromSlot(items, ce, slot);
                });
            }
        }
    }

    protected IntStream getSlots(Container container) {
        return container instanceof WorldlyContainer ? IntStream.of(((WorldlyContainer)container).getSlotsForFace(Direction.DOWN)) : IntStream.range(0, container.getContainerSize());
    }

    private static boolean tryTakeInItemFromSlot(NonNullList<ItemStack> items, Container chest, int slot) {
        ItemStack itemstack = chest.getItem(slot);
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof BlockItem) {
            return addItem(chest, items, itemstack.copy(), slot);
        }
        return false;
    }

    public static boolean addItem(Container chest, NonNullList<ItemStack> items, ItemStack cStack, int slot) {
        int cAmt = cStack.getCount();
        int moveAmt = 0;
        for(int i = 0; i < items.size(); ++i) {
            ItemStack bStack = items.get(i);
            int bAmt = bStack.getCount();
            if (bStack.sameItem(cStack) && bAmt < 64) {
                moveAmt = Math.min(64 - bAmt, cAmt);

                bStack.setCount(bAmt + moveAmt);
                items.set(i, bStack);

                cStack.setCount(cAmt - moveAmt);
                chest.setItem(slot, cStack);
                chest.setChanged();
                return true;
            }
        }
        return false;
    }

    protected void setChestTags(ResourceKey<Level> key, BlockPos pos, CompoundTag tag) {
        if (tag.getBoolean(LINKED) && NbtUtils.readBlockPos(tag.getCompound(CHESTPOS)) == pos) {
            tag.remove(CHESTPOS);
            tag.remove(CHESTDIM);
            tag.putBoolean(LINKED, false);
        } else {
            tag.put(CHESTPOS, NbtUtils.writeBlockPos(pos));
            Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, key).resultOrPartial(LOGGER::error).ifPresent((dimId) -> {
                tag.put(CHESTDIM, dimId);
            });
            tag.putBoolean(LINKED, true);
        }
    }

    public void inventoryTick(ItemStack stack, Level level, Entity holder, int tick, boolean bool) {
        if (!level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains(LINKED) && tag.getBoolean(LINKED)) {
                BlockPos blockpos = NbtUtils.readBlockPos(tag.getCompound(CHESTPOS));
                if (tag.contains(CHESTPOS) && tag.contains(CHESTDIM) && !((ServerLevel) level).getPoiManager().existsAtPosition(SBVillagers.BAG_POI.get(), blockpos)) {
                    tag.remove(CHESTPOS);
                    tag.remove(CHESTDIM);
                    tag.putBoolean(LINKED, false);
                }
            }
        }
    }

    public static Optional<ResourceKey<Level>> getChestDim(CompoundTag tag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get(CHESTDIM)).result();
    }

    public static float getFullnessDisplay(ItemStack p_150767_) {
        return (float) getFilledAmt(p_150767_) / 64.0F;
    }

    private static int getFilledAmt(ItemStack stack) {
        return getContents(stack).mapToInt((itemStack) -> {
            return itemStack.getCount() / itemStack.getMaxStackSize();
        }).sum();
    }

    private static Stream<ItemStack> getContents(ItemStack stack) {
        CompoundTag compoundtag = stack.getTag();
        if (compoundtag == null) {
            return Stream.empty();
        } else {
            ListTag listtag = compoundtag.getList("Items", 10);
            return listtag.stream().map(CompoundTag.class::cast).map(ItemStack::of);
        }
    }

    public static Optional<ResourceKey<Level>> getLevel(CompoundTag tag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get(CHESTDIM)).result();
    }

    private void playOpenSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    public void playCloseSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getLevel().getRandom().nextFloat() * 0.4F);
    }

    class BagUseOnContext extends BlockPlaceContext {
        public BagUseOnContext(UseOnContext context, ItemStack stack) {
            super(context.getLevel(), context.getPlayer(), context.getHand(), stack, new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside()));
        }
    }
}