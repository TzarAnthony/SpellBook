package com.tzaranthony.spellbook.core.util.events;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.items.otherEquipment.ResearchBook;
import com.tzaranthony.spellbook.registries.SBBlocks;
import com.tzaranthony.spellbook.registries.SBItems;
import com.tzaranthony.spellbook.registries.SBVillagers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SpellBook.MOD_ID)
public class VillagerTrading {
    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if(event.getType() == SBVillagers.MAGE.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add(new VillagerSells(SBItems.DYE_BASE.get(), 3, 7, 8, 1));
            trades.get(1).add(new VillagerBuys(SBItems.ALCHEMICAL_RESIDUE.get(), 16, 3, 8, 1));

            trades.get(2).add(new VillagerSells(SBItems.IMBUED_CLOTH.get(), 4, 1, 12, 10));
            trades.get(2).add(new VillagerBuys(SBItems.SILVER_INGOT.get(), 3, 1, 12, 10));
            trades.get(2).add(new VillagerBuys(SBItems.CINNABAR_FRAGMENT.get(), 1, 1, 8, 10));

            trades.get(3).add(new VillagerSells(SBItems.RESEARCH_BOOK.get(), 16, 1, 8, 10, 0.2F));
            trades.get(3).add(new VillagerSells(SBItems.RESEARCH_BOOK.get(), 16, 1, 8, 10, 0.2F));
            trades.get(3).add(new VillagerBuys(SBItems.BOTTLE_OF_MERCURY.get(), 1, 8, 12, 10));
            trades.get(3).add(new VillagerSells(SBItems.BOOKMARK.get(), 48, 1, 8, 10, 0.2F));

            trades.get(4).add(new VillagerBuys(SBItems.VAMPIRE_BLOOD.get(), 1, 5, 4, 15));
            trades.get(4).add(new VillagerBuys(SBItems.DRACONITE.get(), 1, 10, 4, 15));
            trades.get(4).add(new VillagerSells(SBBlocks.BLOOD_ROSE.get(), 20, 1, 4, 15, 0.2F));

            trades.get(5).add(new VillagerSells(SBItems.NEOPHYTE_HELMET.get(), 7, 3, 30, true));
            trades.get(5).add(new VillagerSells(SBItems.NEOPHYTE_CHESTPLATE.get(), 17, 3, 30, true));
            trades.get(5).add(new VillagerSells(SBItems.NEOPHYTE_PANTS.get(), 15, 3, 30, true));
            trades.get(5).add(new VillagerSells(SBItems.NEOPHYTE_BOOTS.get(), 7, 3, 30, true));
        }
    }

    static class VillagerBuys implements VillagerTrades.ItemListing {
        private final Item item;
        private final int amount;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public VillagerBuys(ItemLike wants, int amount, int price, int uses, int xp) {
            this.item = wants.asItem();
            this.amount = amount;
            this.cost = price;
            this.maxUses = uses;
            this.villagerXp = xp;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_35662_, Random p_35663_) {
            return new MerchantOffer(new ItemStack(this.item, this.amount), new ItemStack(Items.EMERALD, this.cost), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class VillagerSells implements VillagerTrades.ItemListing {
        private final ItemStack itemStack;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        private final boolean enchant;

        public VillagerSells(Block block, int cost, int count, int uses, int xp, float priceMod) {
            this(new ItemStack(block), cost, count, uses, xp, priceMod, false);
        }

        public VillagerSells(Item item, int cost, int count, int uses, int xp, float priceMod) {
            this(new ItemStack(item), cost, count, uses, xp, priceMod, false);
        }

        public VillagerSells(Item item, int cost, int count, int uses, int xp) {
            this(new ItemStack(item), cost, count, uses, xp, 0.05F, false);
        }

        public VillagerSells(Item item, int cost, int uses, int xp, boolean enchant) {
            this(new ItemStack(item), cost, 1, uses, xp, 0.2F, enchant);
        }

        public VillagerSells(ItemStack stack, int cost, int count, int uses, int xp, float priceMod, boolean enchant) {
            this.itemStack = stack;
            this.emeraldCost = cost;
            this.numberOfItems = count;
            this.maxUses = uses;
            this.villagerXp = xp;
            this.priceMultiplier = priceMod;
            this.enchant = enchant;
        }

        public MerchantOffer getOffer(Entity entity, Random rand) {
            if (this.enchant) {
                int i = 5 + rand.nextInt(15);
                ItemStack itemstack = EnchantmentHelper.enchantItem(rand, new ItemStack(this.itemStack.getItem()), i, false);
                int j = Math.min(this.emeraldCost + i, 64);
                return new MerchantOffer(new ItemStack(Items.EMERALD, j), itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
            }
            if (this.itemStack.is(SBItems.RESEARCH_BOOK.get())) {
                int i = 1 + rand.nextInt(10);
                ItemStack itemstack = ResearchBook.assignResearchPoints(this.itemStack, i);
                int j = Math.min(this.emeraldCost + i, 64);
                return new MerchantOffer(new ItemStack(Items.EMERALD, j), itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
            }
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
//				trades.get(1).add(new VillagerSells(LEItems.BOTTLE_OF_IGNI, 3, 1, 12, 1));
//				trades.get(1).add(new VillagerSells(LEItems.BOTTLE_OF_AQUA, 3, 1, 12, 1));
//				trades.get(1).add(new VillagerSells(LEItems.BOTTLE_OF_TERRA, 3, 1, 12, 1));
//				trades.get(1).add(new VillagerSells(LEItems.BOTTLE_OF_CAELUM, 3, 1, 12, 1));
//				trades.get(1).add(new VillagerSells(LEItems.BOTTLE_OF_FULGUR, 3, 1, 12, 1));
//				trades.get(1).add(new VillagerSells(LEItems.BOTTLE_OF_MENS, 3, 1, 12, 1));
//
//				trades.get(2).add(new VillagerBuys(LEItems.SILVER_INGOT, 3, 12, 10));
//				trades.get(2).add(new VillagerBuys(LEItems.CINNABAR_FRAGMENT, 2, 12, 10));
//				trades.get(2).add(new VillagerBuys(Items.PHANTOM_MEMBRANE, 6, 12, 10));
//
//				// maybe add potion buckets here
//				trades.get(4).add(new VillagerSells(LEItems.BOTTLE_OF_MERCURY, 6, 1, 12, 15));
//				trades.get(4).add(new VillagerSells(LEItems.VAMPIRE_BLOOD, 16, 1, 12, 15));
//
//				trades.get(5).add(new VillagerSells(LEItems.NYMPHA_BERRIES, 64, 1, 6, 30));
//				trades.get(5).add(new VillagerSells(LEItems.DRACONITE, 64, 1, 10, 30));
}