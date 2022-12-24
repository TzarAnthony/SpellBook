package com.tzaranthony.spellbook.core.blockEntities;

import com.tzaranthony.spellbook.core.crafting.RitualRecipe;
import com.tzaranthony.spellbook.core.network.ItemS2CPacket;
import com.tzaranthony.spellbook.registries.SBBlockEntities;
import com.tzaranthony.spellbook.registries.SBBlocks;
import com.tzaranthony.spellbook.registries.SBPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Collections;
import java.util.List;

public class AlterBE extends CraftingBE {
    protected String TIER = "AlterTier";
    public static int tier;
    protected String LT_ID = "ListDataID";
    protected String MOB_ID = "MobUUID";
    protected NonNullList<Mob> boundEntitites;
    protected String CRAFTING = "CraftingStatus";
    protected boolean isCrafting;
    protected String TRANSFER = "TransferCooldown";
    protected int cooldownTime = -1;
    protected int filledSlots = 0;
    protected float activeRotation;
    protected float activeSpin;

    public AlterBE(BlockPos pos, BlockState state) {
        super(SBBlockEntities.ALTER.get(), pos, state, RitualRecipe.TYPE);
        this.itemHandler = new ItemStackHandler(21) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    SBPackets.sendToClients(new ItemS2CPacket(this, worldPosition));
                }
            }
        };
        this.maxTime = 200;
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.tier = tag.getInt(TIER);
        this.isCrafting = tag.getBoolean(CRAFTING);
        this.cooldownTime = tag.getInt(TRANSFER);
        this.refreshFilledSlots();
        if (!this.level.isClientSide() && tag.contains(MOB_ID)) {
            ListTag lt = tag.getList(MOB_ID, tag.getInt(LT_ID));
            for (Tag tag1 : lt) {
                if (tag1 instanceof CompoundTag) {
                    Entity e = ((ServerLevel) this.level).getEntity(((CompoundTag) tag1).getUUID(MOB_ID));
                    if (e instanceof Mob) {
                        this.boundEntitites.add((Mob) e);
                    }
                }
            }
        }
    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TIER, this.tier);
        tag.putBoolean(CRAFTING, this.isCrafting);
        tag.putInt(TRANSFER, this.cooldownTime);
        if (!this.level.isClientSide() && this.boundEntitites != null && !this.boundEntitites.isEmpty()) {
            ListTag lt = new ListTag();
            for (Mob mob : this.boundEntitites) {
                CompoundTag tag1 = new CompoundTag();
                tag1.putUUID(MOB_ID, mob.getUUID());
                lt.add(tag1);
            }
            tag.putInt(LT_ID, lt.getElementType());
            tag.put(MOB_ID, lt);
        }
    }

    public void setTier() {
        if (this.level.getBlockState(this.getBlockPos()).is(SBBlocks.ALTER_1.get())) {
            this.tier = 0;
        } else if (this.level.getBlockState(this.getBlockPos()).is(SBBlocks.ALTER_2.get())) {
            this.tier = 1;
        } else if (this.level.getBlockState(this.getBlockPos()).is(SBBlocks.ALTER_3.get())) {
            this.tier = 2;
        } else if (this.level.getBlockState(this.getBlockPos()).is(SBBlocks.ALTER_4.get())) {
            this.tier = 3;
        }
    }

    public void startCrafting() {
        this.isCrafting = true;
    }

    public void stopCrafting() {
        this.isCrafting = false;
    }

    public boolean checkMultiBlock() {
        assert !(this.level.isClientSide());
        switch (tier) {
            case 1:
                return MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier2_fmt, AlterTiers.tier2_map, this.level, this.getBlockPos().above());
            case 2:
                return MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier3_fmt, AlterTiers.tier3_map, this.level, this.getBlockPos().above());
            case 3:
                return MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier4_fmt, AlterTiers.tier4_map, this.level, this.getBlockPos().above());
            default:
                return MultiblockHelper.validateMultiBlockPattern(AlterTiers.tier1_fmt, AlterTiers.tier1_map, this.level, this.getBlockPos().above());
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AlterBE AltBe) {
        ++AltBe.activeRotation;
        if (AltBe.activeRotation > 359) {
            AltBe.activeRotation = 0;
        }
        ++AltBe.activeSpin;
        if (AltBe.activeSpin >  5459) {
            AltBe.activeSpin = 0;
        }
    }

    // Server Tick stuff
    public static void serverTick(Level level, BlockPos pos, BlockState state, AlterBE AltBe) {
        boolean flag1 = false;
        AltBe.setTier();
        if (AltBe.isCrafting) {
            RitualRecipe recipe = level.getRecipeManager().getRecipeFor(RitualRecipe.TYPE, AltBe.createContainer(), level).orElse(null);
//            if (recipe != null && recipe.getAlterTier() <= AltBe.tier && recipe.getMobCount() == AltBe.boundEntitites.size() && AltBe.checkMultiBlock() && AltBe.canCraft(recipe)) {
            if (recipe != null && recipe.getAlterTier() <= AltBe.tier && AltBe.checkMultiBlock() && AltBe.canCraft(recipe, AltBe)) {
                ++AltBe.progress;
                if (level.getGameTime() % 80L == 0L) {
                    level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                if (AltBe.progress >= AltBe.maxTime) {
                    AltBe.progress = 0;
                    AltBe.craft(recipe, AltBe);
                    AltBe.refreshFilledSlots();
                    AltBe.stopCrafting();
                    level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    flag1 = true;
                }
            } else {
                if (AltBe.progress > 0) {
                    level.playSound((Player) null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
//                float pctDone = AltBe.progress/ AltBe.maxTime;
                AltBe.stopCrafting();
            }
        } else if (AltBe.progress > 0) {
            AltBe.progress = Mth.clamp(AltBe.progress - 2, 0, AltBe.maxTime);
        }

        if (!AltBe.hasTansferCooldown() && !AltBe.isCrafting) {
            AltBe.setTier();
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, (new AABB(pos)).inflate(0.5D));
            if (!items.isEmpty()) {
                for (ItemEntity item : items) {
                    if (!item.isRemoved() && !item.hasPickUpDelay()) {
                        for (int i = 1; i < AltBe.itemHandler.getSlots(); ++i) {
                            if (AltBe.itemHandler.insertItem(i, item.getItem(), true).isEmpty()) {
                                ItemStack remainder = AltBe.itemHandler.insertItem(i, item.getItem(), false);
                                if (!remainder.isEmpty()) {
                                    item.setItem(remainder);
                                } else {
                                    item.discard();
                                }
                                flag1 = true;
                                break;
                            }
                        }
                    }
                }
            }
            AltBe.cooldownTime = 20;
        } else {
            --AltBe.cooldownTime;
        }

        if (flag1) {
            AltBe.update();
            setChanged(level, pos, state);
        }
    }

    private boolean canCraft(RitualRecipe recipe, AlterBE AltBe) {
        boolean hasStuff = false;
        for (int i = 1; i < AltBe.itemHandler.getSlots() - 1; ++i) {
            if (!AltBe.itemHandler.getStackInSlot(i).isEmpty()) {
                hasStuff = hasStuff || true;
                break;
            }
        }

        if (recipe.getMobCount() > 0) {
            return AltBe.checkMobs(AltBe, recipe.getMobCount()) > 0 && hasStuff && checkResult(recipe.assemble(this.createContainer()), AltBe.itemHandler.getStackInSlot(0), AltBe.itemHandler.getSlotLimit(0));
        }
        return hasStuff && checkResult(recipe.assemble(this.createContainer()), AltBe.itemHandler.getStackInSlot(0), AltBe.itemHandler.getSlotLimit(0));
    }

    private void craft(RitualRecipe recipe, AlterBE AltBe) {
        NonNullList<ItemStack> ings = recipe.getInputs();
        for (int i = 1; i < AltBe.itemHandler.getSlots(); ++i) {
            ItemStack currItm = AltBe.itemHandler.getStackInSlot(i);
            for (ItemStack ingStack : ings) {
                if (currItm.is(ingStack.getItem()) && currItm.getCount() >= ingStack.getCount()) {
                    AltBe.itemHandler.extractItem(i, ingStack.getCount(), false);
                }
            }
        }
        int count = AltBe.itemHandler.getStackInSlot(0).getCount() + recipe.getResultItem().getCount();

        ItemStack result = new ItemStack(recipe.getResultItem().getItem(), count);
        if (recipe.getMobCount() > 0) {
            int needs = recipe.getMobCount();
            int j = AltBe.checkMobs(AltBe, needs);
            if (j > 0) {
                EntityType mobType = AltBe.getMob(j).getType();
                for (int k = 0; k > AltBe.boundEntitites.size(); ++k) {
                    if (mobType == AltBe.getMob(k).getType()) {
                        AltBe.boundEntitites.remove(k);
                        --needs;
                    }
                    if (needs <= 0) {
                        break;
                    }
                }
                CompoundTag tag = result.getOrCreateTag();
                tag.putString("SBEntityIDTag", mobType.toString());
            }
        }
        AltBe.itemHandler.setStackInSlot(0, result);
    }

    private int checkMobs(AlterBE AltBe, int needs) {
        for (int i = 0; i > AltBe.boundEntitites.size(); ++i) {
            int mobCount = Collections.frequency(AltBe.boundEntitites, AltBe.getMob(i));
            if (mobCount >= needs) {
                return i;
            }
        }
        return -1;
    }

    // interactions
    public boolean takeOrAddItem(Player player, ItemStack stack) {
        if (player.isShiftKeyDown()) {
            for (int i = 0; i < itemHandler.getSlots(); ++i) {
                ItemStack altStack = itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false);
                if (!altStack.isEmpty()) {
                    addOrPopItem(player, altStack, false);
                }
            }
            this.level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            this.filledSlots = 0;
            return true;
        } else if (stack.isEmpty()) {
            if (!itemHandler.getStackInSlot(0).isEmpty()) {
                ItemStack altStack = itemHandler.extractItem(0, itemHandler.getStackInSlot(0).getCount(), false);
                addOrPopItem(player, altStack, true);
                return true;
            }
            for (int i = itemHandler.getSlots() - 1; i > 0; --i) {
                ItemStack altStack = itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false);
                if (!altStack.isEmpty()) {
                    addOrPopItem(player, altStack, true);
                    --this.filledSlots;
                    return true;
                }
            }
        } else if (!stack.isEmpty()) {
            for (int i = 1; i < itemHandler.getSlots(); ++i) {
                if (itemHandler.insertItem(i, stack, true).isEmpty()) {
                    ItemStack remainder = itemHandler.insertItem(i, stack.copy(), false);
                    stack.setCount(remainder.getCount());
                    this.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ++this.filledSlots;
                    update();
                    return true;
                }
            }
        }
        return false;
    }

    protected void addOrPopItem(Player player, ItemStack stack, boolean sound) {
        player.getInventory().add(stack);
        if (!stack.isEmpty()) {
            this.level.addFreshEntity(new ItemEntity(this.level, player.getX(), player.getY(), player.getZ(), stack));
        }
        if (sound) {
            if (stack.isEmpty()) {
                this.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.2F, 1.0F);
            } else {
                this.level.playSound((Player) null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
        update();
    }

    public ItemStack getItem(int i) {
        return this.itemHandler.getStackInSlot(i);
    }

    public int getFilledSlots() {
        return this.filledSlots;
    }

    public float getActiveRotation(float mod) {
        return this.activeRotation + mod;
    }

    public float getActiveSpin(float mod) {
        return this.activeSpin + mod;
    }

    protected void refreshFilledSlots() {
        this.filledSlots = 0;
        for (int i = 1; i < itemHandler.getSlots(); ++i) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                ++this.filledSlots;
            }
        }
    }

    private boolean hasTansferCooldown() {
        return this.cooldownTime > 0;
    }

    public void addMob(Mob mob) {
        this.boundEntitites.add(mob);
    }

    public Mob getMob(int i) {
        return this.boundEntitites.get(i);
    }

    public void clearMobs() {
        this.boundEntitites.clear();
    }
}