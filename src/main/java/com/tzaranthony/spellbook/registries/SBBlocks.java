package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.blocks.SBBlockProperties;
import com.tzaranthony.spellbook.core.blocks.block.*;
import com.tzaranthony.spellbook.core.blocks.containsBE.*;
import com.tzaranthony.spellbook.core.blocks.fire.EndFire;
import com.tzaranthony.spellbook.core.blocks.fire.SBTorch;
import com.tzaranthony.spellbook.core.blocks.fire.SBWallTorch;
import com.tzaranthony.spellbook.core.blocks.plant.*;
import com.tzaranthony.spellbook.core.blocks.spellBlocks.BlackHole;
import com.tzaranthony.spellbook.core.blocks.spellBlocks.Snare;
import com.tzaranthony.spellbook.core.blocks.spellBlocks.Timer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SBBlocks {
    public static final DeferredRegister<Block> reg = DeferredRegister.create(ForgeRegistries.BLOCKS, SpellBook.MOD_ID);

    // world-gen
    public static final RegistryObject<Block> SILVER_ORE = registerBlockAndItem("silver_ore", () -> new SBOreBlock(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = registerBlockAndItem("deepslate_silver_ore", () -> new SBOreBlock(SBBlockProperties.StandardDeepslate()));
    public static final RegistryObject<Block> CINNABAR_ORE = registerBlockAndItem("cinnabar_ore", () -> new SBOreBlock(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> DEEPSLATE_CINNABAR_ORE = registerBlockAndItem("deepslate_cinnabar_ore", () -> new SBOreBlock(SBBlockProperties.StandardDeepslate()));
    public static final RegistryObject<Block> NYMPH_BUSH = reg.register("nymph_bush", () -> new WaterBerries(SBBlockProperties.TickingPlant(Material.WATER_PLANT, SoundType.SWEET_BERRY_BUSH)));
    public static final RegistryObject<Block> NETHER_BUSH = reg.register("nether_bush", () -> new LavaBerries(SBBlockProperties.TickingLavaPlant(Material.PLANT, SoundType.WEEPING_VINES)));
    public static final RegistryObject<Block> TERRAN_BUSH = reg.register("terran_bush", () -> new StoneBerries(SBBlockProperties.TickingPlant(Material.PLANT, SoundType.SWEET_BERRY_BUSH)));
    public static final RegistryObject<Block> NUBIBUS_BUSH = reg.register("nubibus_bush", () -> new HeightBerries(SBBlockProperties.TickingPlant(Material.PLANT, SoundType.SWEET_BERRY_BUSH)));

    // storage blocks
    public static final RegistryObject<Block> SILVER_BLOCK = registerBlockAndItem("silver_block", () -> new Block(SBBlockProperties.StandardMetal()));
    public static final RegistryObject<Block> CINNABAR_BLOCK = registerBlockAndItem("cinnabar_block", () -> new Block(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> RAW_SILVER_BLOCK = registerBlockAndItem("raw_silver_block", () -> new Block(SBBlockProperties.StandardMetal()));
    public static final RegistryObject<Block> SILVER_STEEL_BLOCK = registerBlockAndItem("silver_steel_block", () -> new Block(SBBlockProperties.StandardMetal()));
    public static final RegistryObject<Block> IMBUED_CLOTH_BLOCK = registerBlockAndItem("imbued_cloth_block", () -> new Block(SBBlockProperties.ClothBlock(MaterialColor.COLOR_PURPLE)));
    public static final RegistryObject<Block> CURSED_SILVER_BLOCK = registerBlockAndRareUnburnableItem("cursed_silver_block", () -> new CursedBlock(30.0F), Rarity.UNCOMMON);
    public static final RegistryObject<Block> ENCHANTED_STEEL_BLOCK = registerBlockAndRareUnburnableItem("enchanted_steel_block", () -> new EnderBlock(30.0F), Rarity.UNCOMMON);
    public static final RegistryObject<Block> CRYSTALAN_BUD_SMALL = reg.register("crystalan_bud_small", () -> new GrowableCrystal(3, 4, SoundType.SMALL_AMETHYST_BUD, 2));
    public static final RegistryObject<Block> CRYSTALAN_BUD_MEDIUM = registerBlockAndRareItem("crystalan_bud_medium", () -> new GrowableCrystal(5, 3, SoundType.MEDIUM_AMETHYST_BUD, 4), Rarity.RARE);
    public static final RegistryObject<Block> CRYSTALAN_BUD_LARGE = registerBlockAndRareItem("crystalan_bud_large", () -> new GrowableCrystal(6, 3, SoundType.LARGE_AMETHYST_BUD, 5), Rarity.RARE);
    public static final RegistryObject<Block> CRYSTALAN_CLUSTER = registerBlockAndRareItem("crystalan_cluster", () -> new GrowableCrystal(8, 2, SoundType.AMETHYST_CLUSTER, 6), Rarity.RARE);
    public static final RegistryObject<Block> PHOENIX_METAL_BLOCK = registerBlockAndRareUnburnableItem("phoenix_metal_block", () -> new Block(SBBlockProperties.LightyNetheriteBasedBlock(30.0F, 10)), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK = registerBlockAndRareUnburnableItem("light_essence_block", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_BLACK = registerBlockAndRareUnburnableItem("light_essence_block_black", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_BLUE = registerBlockAndRareUnburnableItem("light_essence_block_blue", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_BROWN = registerBlockAndRareUnburnableItem("light_essence_block_brown", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_CYAN = registerBlockAndRareUnburnableItem("light_essence_block_cyan", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_GRAY = registerBlockAndRareUnburnableItem("light_essence_block_gray", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_GREEN = registerBlockAndRareUnburnableItem("light_essence_block_green", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_LIGHT_BLUE = registerBlockAndRareUnburnableItem("light_essence_block_light_blue", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_LIGHT_GRAY = registerBlockAndRareUnburnableItem("light_essence_block_light_gray", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_LIME = registerBlockAndRareUnburnableItem("light_essence_block_lime", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_MAGENTA = registerBlockAndRareUnburnableItem("light_essence_block_magenta", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_ORANGE = registerBlockAndRareUnburnableItem("light_essence_block_orange", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_PINK = registerBlockAndRareUnburnableItem("light_essence_block_pink", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_PURPLE = registerBlockAndRareUnburnableItem("light_essence_block_purple", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_RED = registerBlockAndRareUnburnableItem("light_essence_block_red", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_WHITE = registerBlockAndRareUnburnableItem("light_essence_block_white", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> LIGHT_ESSENCE_BLOCK_YELLOW = registerBlockAndRareUnburnableItem("light_essence_block_yellow", () -> new LightEssenceBlock(1200.0F, 15), Rarity.RARE);
    public static final RegistryObject<Block> CRYSTALAN_BLOCK = registerBlockAndRareUnburnableItem("crystalan_matrix_block", () -> new Block(SBBlockProperties.LightyNetheriteBasedBlock(30.0F, 3)), Rarity.RARE);
    public static final RegistryObject<Block> DIMENSIONAL_ALLOY_BLOCK = registerBlockAndRareUnburnableItem("dimensional_alloy_block", () -> new DimensionalBlock(SBBlockProperties.NetheriteBasedBlock(30.0F, 20)), Rarity.EPIC);
    public static final RegistryObject<Block> ENDER_INFUSED_CLOTH_BLOCK = registerBlockAndRareUnburnableItem("ender_infused_cloth_block", () -> new Block(SBBlockProperties.ClothBlock(MaterialColor.COLOR_GREEN)), Rarity.EPIC);

    // Alchemy Results
    public static final RegistryObject<Block> NETHERITE_ORE = registerBlockAndUnburnableItem("netherite_ore", () -> new SBOreBlock(SBBlockProperties.StandardRock(SoundType.NETHER_ORE, 35.0F, 1200.0F)));
    public static final RegistryObject<Block> BLOOD_ROSE = registerBlockAndRareItem("blood_rose", () -> new BloodRose(), Rarity.UNCOMMON);
    public static final RegistryObject<Block> POTTED_BLOOD_ROSE = reg.register("potted_blood_rose", () -> new FlowerPotBlock(BLOOD_ROSE.get(), SBBlockProperties.PottedPlant()));
    public static final RegistryObject<Block> INFUSED_GLASS = registerBlockAndUnburnableItem("infused_glass", () -> new SBGlassBlock(SBBlockProperties.StandardGlass(0.3F, 1200.0F)));
    public static final RegistryObject<Block> INFUSED_END_STONE = registerBlockAndUnburnableItem("infused_end_stone", () -> new Block(SBBlockProperties.StandardRock(5.0F, 12.0F)));
    public static final RegistryObject<Block> CRYSTALLIZED_ENDER_CLUSTER = registerBlockAndRareUnburnableItem("crystallized_ender_cluster", () -> new EnderCrystal(8, 2, SoundType.AMETHYST_CLUSTER, 10), Rarity.RARE); //TODO: create transparent and cutout render

    // "machines"
    public static final RegistryObject<Block> ALTER_1 = registerBlockAndItem("alter_1", () -> new Alter(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> ALTER_2 = registerBlockAndRareItem("alter_2", () -> new Alter(SBBlockProperties.StandardRock(SoundType.ANCIENT_DEBRIS)), Rarity.UNCOMMON);
    public static final RegistryObject<Block> ALTER_3 = registerBlockAndRareItem("alter_3", () -> new Alter(SBBlockProperties.StandardRock(SoundType.LODESTONE)), Rarity.RARE);
    public static final RegistryObject<Block> ALCHEMICAL_FURNACE = registerBlockAndItem("alchemical_furnace", () -> new AlchemicalFurnace(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> ALCHEMICAL_FOUNDRY = registerBlockAndItem("alchemical_forge", () -> new AlchemicalFoundry(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> POTION_MANUFACTORIUM = registerBlockAndItem("potion_manufactorium", () -> new Block(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> POTION_SEPARATOR = registerBlockAndItem("potion_separator", () -> new Block(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> ENCHANTED_CRAFTING_TABLES = registerBlockAndRareItem("enchanted_crafting_table", () -> new Block(SBBlockProperties.StandardRock()), Rarity.UNCOMMON); //TODO: Autocrafts items
    public static final RegistryObject<Block> DIMENSIONAL_DIRECTORY = registerBlockAndRareItem("dimensional_directory", () -> new Block(SBBlockProperties.StandardRock()), Rarity.RARE); //TODO: Created by placing a Tome of Coordination in a Lectern -- drops the book and a lectern when broken -- like an ME Controler + Monitor
    public static final RegistryObject<Block> NEOPHYTE_BOOKSHELF = registerBlockAndItem("neophyte_bookshelf", () -> new Block(SBBlockProperties.StandardRock())); //TODO: these store 4 Dimensional Codicies -- like an upgradable ME Drive
    public static final RegistryObject<Block> ENCHANTED_BOOKSHELF = registerBlockAndItem("enchanted_bookshelf", () -> new Block(SBBlockProperties.StandardRock())); //TODO: these store 8 Dimensional Codicies
    public static final RegistryObject<Block> CRYSTAL_BOOKSHELF = registerBlockAndItem("crystal_bookshelf", () -> new Block(SBBlockProperties.StandardRock())); //TODO: these store 16 Dimensional Codicies
    public static final RegistryObject<Block> ENDER_BOOKSHELF = registerBlockAndItem("ender_bookshelf", () -> new Block(SBBlockProperties.StandardRock())); //TODO: these store 32 Dimensional Codicies
    public static final RegistryObject<Block> EXCHANGER = registerBlockAndItem("exchanger", () -> new Block(SBBlockProperties.StandardRock())); //TODO: a RegistryObject<Block> where librarian Golems (or pipes) can collect and deposit items

    // pedestals
    public static final RegistryObject<Block> PEDESTAL_SPIN = registerBlockAndItem("pedestal_spin", () -> new Pedestal(true, false, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> PEDESTAL_STILL = registerBlockAndItem("pedestal_still", () -> new Pedestal(false, false, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> PEDESTAL_GLOW_SPIN = registerBlockAndItem("pedestal_glow_spin", () -> new Pedestal(true, true, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> PEDESTAL_GLOW_STILL = registerBlockAndItem("pedestal_glow_still", () -> new Pedestal(false, true, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> BLACK_PEDESTAL_SPIN = registerBlockAndItem("black_pedestal_spin", () -> new Pedestal(true, false, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> BLACK_PEDESTAL_STILL = registerBlockAndItem("black_pedestal_still", () -> new Pedestal(false, false, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> BLACK_PEDESTAL_GLOW_SPIN = registerBlockAndItem("black_pedestal_glow_spin", () -> new Pedestal(true, true, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> BLACK_PEDESTAL_GLOW_STILL = registerBlockAndItem("black_pedestal_glow_still", () -> new Pedestal(false, true, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> GRAY_PEDESTAL_SPIN = registerBlockAndItem("gray_pedestal_spin", () -> new Pedestal(true, false, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> GRAY_PEDESTAL_STILL = registerBlockAndItem("gray_pedestal_still", () -> new Pedestal(false, false, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> GRAY_PEDESTAL_GLOW_SPIN = registerBlockAndItem("gray_pedestal_glow_spin", () -> new Pedestal(true, true, SBBlockProperties.StandardRock().noOcclusion()));
    public static final RegistryObject<Block> GRAY_PEDESTAL_GLOW_STILL = registerBlockAndItem("gray_pedestal_glow_still", () -> new Pedestal(false, true, SBBlockProperties.StandardRock().noOcclusion()));

    // spell blocks
    public static final RegistryObject<Block> SNARE = reg.register("snare", () -> new Snare());
    public static final RegistryObject<Block> BLACK_HOLE = reg.register("dark_rift", () -> new BlackHole());
    public static final RegistryObject<Block> ENDER_RIFT = reg.register("ender_rift", () -> new Block(SBBlockProperties.StandardRock()));
    public static final RegistryObject<Block> TIMER = reg.register("timer", () -> new Timer());

    // end fire stuff
    public static final RegistryObject<Block> END_CAMPFIRE = registerBlockAndItem("end_campfire", () -> new SBCampfire(3, SBBlockProperties.Campfire(10)));
    public static final RegistryObject<Block> END_LANTERN = registerBlockAndItem("end_lantern", () -> new LanternBlock(SBBlockProperties.Lantern(10)));
    public static final RegistryObject<Block> END_TORCH = reg.register("end_torch", () -> new SBTorch(SBBlockProperties.Torch(10), ParticleTypes.SOUL_FIRE_FLAME));
    public static final RegistryObject<Block> END_WALL_TORCH = reg.register("end_wall_torch", () -> new SBWallTorch(SBBlockProperties.WallTorch(10, END_TORCH.get()), ParticleTypes.SOUL_FIRE_FLAME));
    public static final RegistryObject<Block> END_FIRE = reg.register("end_fire", () -> new EndFire(MaterialColor.EMERALD, 10));

    public static RegistryObject<Block> registerBlockAndUnburnableItem(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = reg.register(name, block);
        SBItems.reg.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties().fireResistant().tab(SpellBook.TAB)));
        return blockObj;
    }

    public static RegistryObject<Block> registerBlockAndRareUnburnableItem(String name, Supplier<Block> block, Rarity rare) {
        RegistryObject<Block> blockObj = reg.register(name, block);
        SBItems.reg.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties().fireResistant().rarity(rare).tab(SpellBook.TAB)));
        return blockObj;
    }

    public static RegistryObject<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = reg.register(name, block);
        SBItems.reg.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties().tab(SpellBook.TAB)));
        return blockObj;
    }

    public static RegistryObject<Block> registerBlockAndRareItem(String name, Supplier<Block> block, Rarity rare) {
        RegistryObject<Block> blockObj = reg.register(name, block);
        SBItems.reg.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties().rarity(rare).tab(SpellBook.TAB)));
        return blockObj;
    }
}