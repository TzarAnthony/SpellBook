package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFoundryMenu;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFurnaceMenu;
import com.tzaranthony.spellbook.core.containers.menus.ArchitectBagMenu;
import com.tzaranthony.spellbook.core.containers.menus.BuilderBagMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SBMenus {
    public static final DeferredRegister<MenuType<?>> reg = DeferredRegister.create(Registry.MENU_REGISTRY, SpellBook.MOD_ID);

    public static final RegistryObject<MenuType<AlchemicalFurnaceMenu>> ALCHEMICAL_FURNACE = reg.register("alchemical_furnace", () -> IForgeMenuType.create((id, inv, data) -> new AlchemicalFurnaceMenu(id, inv, data.readBlockPos())));
    public static final RegistryObject<MenuType<AlchemicalFoundryMenu>> ALCHEMICAL_FOUNDRY = reg.register("alchemical_foundry", () -> IForgeMenuType.create((id, inv, data) -> new AlchemicalFoundryMenu(id, inv, data.readBlockPos())));

    public static final RegistryObject<MenuType<BuilderBagMenu>> BUILDER_BAG = reg.register("builder_bag", () -> IForgeMenuType.create((id, inv, data) -> new BuilderBagMenu(id, inv, new SimpleContainer(3), new SimpleContainerData(2), new ItemStack(SBItems.BUILDER_BAG.get()))));
    public static final RegistryObject<MenuType<ArchitectBagMenu>> ARCH_BAG = reg.register("architect_bag", () -> IForgeMenuType.create((id, inv, data) -> new ArchitectBagMenu(id, inv, new SimpleContainer(3), new SimpleContainerData(2), new ItemStack(SBItems.BUILDER_BAG.get()))));
}