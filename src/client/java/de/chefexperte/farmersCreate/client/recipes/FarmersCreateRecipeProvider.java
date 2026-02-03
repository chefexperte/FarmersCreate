package de.chefexperte.farmersCreate.client.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class FarmersCreateRecipeProvider extends FabricRecipeProvider {

    public FarmersCreateRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            @Override
            public void buildRecipes() {
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return "FarmersCreateRecipeProvider";
    }
}
