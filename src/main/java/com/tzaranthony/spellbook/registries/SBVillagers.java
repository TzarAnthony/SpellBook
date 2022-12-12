package com.tzaranthony.spellbook.registries;

import com.google.common.collect.ImmutableSet;
import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;

public class SBVillagers {
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, SpellBook.MOD_ID);
    public static final DeferredRegister<VillagerProfession> profession = DeferredRegister.create(ForgeRegistries.PROFESSIONS, SpellBook.MOD_ID);

    public static final RegistryObject<PoiType> MAGE_POI = POI.register("mage_poi"
            , () -> new PoiType("mage_poi", PoiType.getBlockStates(SBBlocks.ALTER_1.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> MAGE = profession.register("mage"
            , () -> new VillagerProfession("mage", MAGE_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CLERIC));

    public static final RegistryObject<PoiType> BAG_POI = POI.register("bag_poi"
            , () -> new PoiType("bag_poi", PoiType.getBlockStates(Blocks.CHEST), 0, 1));

    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class)
                    .invoke(null, MAGE_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI.register(eventBus);
        profession.register(eventBus);
    }
}