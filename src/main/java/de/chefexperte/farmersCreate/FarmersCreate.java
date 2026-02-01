package de.chefexperte.farmersCreate;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.item.crafting.RecipeManager;

public class FarmersCreate implements ModInitializer {

    public static final String MOD_ID = "farmerscreate";

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            RecipeManager recipeManager = server.getRecipeManager();
            // Hier kannst du mit dem RecipeManager arbeiten
            //recipeManager.getRecipes().forEach(recipe -> System.out.println("Recipe found: " + recipe));
        });
    }
}
