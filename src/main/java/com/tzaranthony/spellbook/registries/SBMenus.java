package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.SpellBook;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFoundryMenu;
import com.tzaranthony.spellbook.core.containers.menus.AlchemicalFurnaceMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SBMenus {
    public static final DeferredRegister<MenuType<?>> reg = DeferredRegister.create(Registry.MENU_REGISTRY, SpellBook.MOD_ID);

    public static final RegistryObject<MenuType<AlchemicalFurnaceMenu>> ALCHEMICAL_FURNACE = reg.register("alchemical_furnace", () -> IForgeMenuType.create((id, inv, data) -> new AlchemicalFurnaceMenu(id, inv, data.readBlockPos())));
    public static final RegistryObject<MenuType<AlchemicalFoundryMenu>> ALCHEMICAL_FOUNDRY = reg.register("alchemical_foundry", () -> IForgeMenuType.create((id, inv, data) -> new AlchemicalFoundryMenu(id, inv, data.readBlockPos())));
}