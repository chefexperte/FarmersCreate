package de.chefexperte.farmersCreate.client.recipes;

import com.zurrtum.create.AllRecipeTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

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
                HolderLookup.RegistryLookup<Item> itemLookup = provider.lookupOrThrow(Registries.ITEM);
                CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CABBAGE_LEAF.get()), Ingredient.of(itemLookup.getOrThrow(CommonTags.TOOLS_KNIFE)), ModItems.MINCED_BEEF.get(), 2)
                        .build(recipeOutput);
                itemLookup.listElementIds().limit(10).forEach(
                        System.out::println
                );
            }
        };
    }

    @Override
    public @NonNull String getName() {
        return "FarmersCreateRecipeProvider";
    }
}
