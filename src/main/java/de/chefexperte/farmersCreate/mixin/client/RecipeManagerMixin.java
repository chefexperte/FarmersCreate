package de.chefexperte.farmersCreate.mixin.client;

import com.zurrtum.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.zurrtum.create.content.kinetics.mixer.MixingRecipe;
import com.zurrtum.create.content.processing.recipe.HeatCondition;
import com.zurrtum.create.content.processing.recipe.SizedIngredient;
import com.zurrtum.create.infrastructure.fluids.FluidStack;
import de.chefexperte.farmersCreate.FarmersCreate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Shadow
    private RecipeMap recipes;

    @Inject(at = @At("RETURN"), method = "apply(Lnet/minecraft/world/item/crafting/RecipeMap;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V")
    public void callApply(RecipeMap recipeMap, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        var allRecipes = new ArrayList<>(recipeMap.values());
        // Get Farmer's Delight Cutting Board Recipes
        Collection<RecipeHolder<CuttingBoardRecipe>> cuttingBoardRecipes = recipeMap.byType(ModRecipeTypes.CUTTING.get()).stream().toList();
        System.out.println("Found " + cuttingBoardRecipes.size() + " Farmer's Delight Cutting Board Recipes");
        for (var cuttingBoardRecipeHolder : cuttingBoardRecipes) {
            var cuttingBoardRecipe = cuttingBoardRecipeHolder.value();
            var cuttingRecipe = new DeployerApplicationRecipe(cuttingBoardRecipe.getResults().getFirst(), true, cuttingBoardRecipe.getInput(), cuttingBoardRecipe.getTool());
            var namespace = cuttingBoardRecipeHolder.id().identifier().getNamespace();
            var path = cuttingBoardRecipeHolder.id().identifier().getPath();
            var newId = Identifier.fromNamespaceAndPath(FarmersCreate.MOD_ID, "cutting/" + namespace + "/" + path);
            var key = ResourceKey.create(Registries.RECIPE, newId);
            var recipeHolder = new RecipeHolder<>(key, cuttingRecipe);
            allRecipes.add(recipeHolder);
        }
        // Get Farmer's Delight Cooking Recipes
        Collection<RecipeHolder<CookingPotRecipe>> cookingRecipes = recipeMap.byType(ModRecipeTypes.COOKING.get()).stream().toList();
        System.out.println("Found " + cookingRecipes.size() + " Farmer's Delight Cooking Recipes");
        for (var tag : BuiltInRegistries.ITEM.getTags().toList()) {
            System.out.println("Found tag: " + tag);
        }
        for (var cookingRecipeHolder : cookingRecipes) {
            var cookingRecipe = cookingRecipeHolder.value();
            var recipeId = cookingRecipeHolder.id().identifier().toString();
            var skipRecipe = false;
            var cleanInput = new ArrayList<Ingredient>(cookingRecipe.input().size());
            // Check ingredients for CustomingredientImpl because ZurrTum Create fork does not support it for 1.21.11
            for (var ingredient : cookingRecipe.input()) {
                var customIngredient = ingredient.getCustomIngredient();
                if (customIngredient == null) {
                    cleanInput.add(ingredient);
                    continue;
                }
                // Filter out known recipes, they have been manually added
                switch (recipeId) {
                    case "farmersdelight:cooking/bone_broth":
                        // Does not work, idk why
                        //cleanInput.add(Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(FarmersCreate.BONE_BROTH_INGREDIENTS_TAG)));
                        skipRecipe = true;
                        break;
                    case "farmersdelight:cooking/dumplings":
                        // Does not work, idk why
                        //cleanInput.add(Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(FarmersCreate.DUMPLINGS_INGREDIENTS_TAG)));
                        skipRecipe = true;
                        break;
                    default:
                        System.out.println("The \"" + recipeId + "\" recipe uses custom ingredients, which is not compatible with ZurrTums Create fork for 1.21.11. Please report this to the FarmersCreate developer.");
                        skipRecipe = true;
                }
                if (skipRecipe) {
                    break;
                }
            }
            if (skipRecipe) {
                continue;
            }
            var ingredients = SizedIngredient.of(cleanInput);
            var mixingRecipe = new MixingRecipe(cookingRecipe.result(), FluidStack.EMPTY, HeatCondition.HEATED, new ArrayList<>(), ingredients);
            var namespace = cookingRecipeHolder.id().identifier().getNamespace();
            var path = cookingRecipeHolder.id().identifier().getPath();
            var newId = Identifier.fromNamespaceAndPath(FarmersCreate.MOD_ID, "cooking/" + namespace + "/" + path);
            var key = ResourceKey.create(Registries.RECIPE, newId);
            var recipeHolder = new RecipeHolder<>(key, mixingRecipe);
            allRecipes.add(recipeHolder);
        }
        this.recipes = RecipeMap.create(allRecipes);
    }
}
