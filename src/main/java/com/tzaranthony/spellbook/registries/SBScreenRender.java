package com.tzaranthony.spellbook.registries;

import com.tzaranthony.spellbook.client.screens.AlchemicalFoundryScreen;
import com.tzaranthony.spellbook.client.screens.AlchemicalFurnaceScreen;
import com.tzaranthony.spellbook.client.screens.BuilderBagScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class SBScreenRender {
    public static void renderScreens() {
        MenuScreens.register(SBMenus.ALCHEMICAL_FURNACE.get(), AlchemicalFurnaceScreen::new);
        MenuScreens.register(SBMenus.ALCHEMICAL_FOUNDRY.get(), AlchemicalFoundryScreen::new);
        MenuScreens.register(SBMenus.BUILDER_BAG.get(), BuilderBagScreen::new);
    }
}