package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.blockEntities.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SBBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> reg = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SpellBook.MOD_ID);

    public static final RegistryObject<BlockEntityType<CampfireBlockEntity>> END_CAMPFIRE = reg.register("end_campfire",
            () -> BlockEntityType.Builder.of(CampfireBlockEntity::new, SBBlocks.END_CAMPFIRE.get()).build(null));

    public static final RegistryObject<BlockEntityType<AlchemicalFurnaceBE>> ALCHEMICAL_FURNACE = reg.register("alchemical_furnace",
            () -> BlockEntityType.Builder.of(AlchemicalFurnaceBE::new, SBBlocks.ALCHEMICAL_FURNACE.get()).build(null));

    public static final RegistryObject<BlockEntityType<AlchemicalFoundryBE>> ALCHEMICAL_FOUNDRY = reg.register("alchemical_foundry",
            () -> BlockEntityType.Builder.of(AlchemicalFoundryBE::new, SBBlocks.ALCHEMICAL_FOUNDRY.get()).build(null));

    public static final RegistryObject<BlockEntityType<AlterBE>> ALTER = reg.register("alter",
            () -> BlockEntityType.Builder.of(AlterBE::new, SBBlocks.ALTER_1.get(), SBBlocks.ALTER_2.get(), SBBlocks.ALTER_3.get()).build(null));

    public static final RegistryObject<BlockEntityType<PedestalStillBE>> PEDESTAL_STILL = reg.register("pedestal_still",
            () -> BlockEntityType.Builder.of(PedestalStillBE::new, SBBlocks.PEDESTAL_STILL.get(), SBBlocks.BLACK_PEDESTAL_STILL.get(), SBBlocks.GRAY_PEDESTAL_STILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<PedestalStillGlowBE>> PEDESTAL_STILL_GLOW = reg.register("pedestal_still_glow",
            () -> BlockEntityType.Builder.of(PedestalStillGlowBE::new, SBBlocks.PEDESTAL_GLOW_STILL.get(), SBBlocks.BLACK_PEDESTAL_GLOW_STILL.get(), SBBlocks.GRAY_PEDESTAL_GLOW_STILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<PedestalSpinBE>> PEDESTAL_SPIN = reg.register("pedestal_spin",
            () -> BlockEntityType.Builder.of(PedestalSpinBE::new, SBBlocks.PEDESTAL_SPIN.get(), SBBlocks.BLACK_PEDESTAL_SPIN.get(), SBBlocks.GRAY_PEDESTAL_SPIN.get()).build(null));

    public static final RegistryObject<BlockEntityType<PedestalSpinGlowBE>> PEDESTAL_SPIN_GLOW = reg.register("pedestal_spin_glow",
            () -> BlockEntityType.Builder.of(PedestalSpinGlowBE::new, SBBlocks.PEDESTAL_GLOW_SPIN.get(), SBBlocks.BLACK_PEDESTAL_GLOW_SPIN.get(), SBBlocks.GRAY_PEDESTAL_GLOW_SPIN.get()).build(null));

    public static final RegistryObject<BlockEntityType<DimensionalBE>> DIMENSIONAL_BE = reg.register("dimensional_be",
            () -> BlockEntityType.Builder.of(DimensionalBE::new, SBBlocks.DIMENSIONAL_ALLOY_BLOCK.get()).build(null));
}