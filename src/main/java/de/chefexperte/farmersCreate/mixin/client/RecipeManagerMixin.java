package de.chefexperte.farmersCreate.mixin.client;

import com.zurrtum.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.zurrtum.create.content.kinetics.mixer.MixingRecipe;
import com.zurrtum.create.content.processing.recipe.HeatCondition;
import com.zurrtum.create.content.processing.recipe.SizedIngredient;
import com.zurrtum.create.infrastructure.fluids.FluidStack;
import de.chefexperte.farmersCreate.FarmersCreate;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow
    private RecipeMap recipes;

    @Inject(at = @At("RETURN"), method = "apply(Lnet/minecraft/world/item/crafting/RecipeMap;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V")
    public void callApply(RecipeMap recipeMap, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        var allRecipes = new ArrayList<>(recipeMap.values());
        // Get Farmer's Delight Cutting Board Recipes
        Collection<RecipeHolder<CuttingBoardRecipe>> cuttingBoardRecipes = recipeMap.byType(ModRecipeTypes.CUTTING.get()).stream().toList();
        System.out.println("Found " + cuttingBoardRecipes.size() + "Farmer's Delight Cutting Board Recipes");
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
        System.out.println("Found " + cookingRecipes.size() + "Farmer's Delight Cooking Recipes");
        for (var cookingRecipeHolder : cookingRecipes) {
            var cookingRecipe = cookingRecipeHolder.value();
            var ingredients = SizedIngredient.of(cookingRecipe.input());
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
