package de.chefexperte.farmersCreate.mixin.client;

import com.zurrtum.create.AllRecipeSerializers;
import com.zurrtum.create.content.kinetics.belt.BeltHelper;
import com.zurrtum.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.zurrtum.create.content.kinetics.belt.transport.TransportedItemStack;
import com.zurrtum.create.content.kinetics.deployer.BeltDeployerCallbacks;
import com.zurrtum.create.content.kinetics.deployer.DeployerBlockEntity;
import com.zurrtum.create.content.kinetics.deployer.ItemApplicationRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;

@Mixin(BeltDeployerCallbacks.class)
public abstract class BeltDeployerCallbacksMixin {

    @Inject(at = @At(
            value = "INVOKE",
            target = "com/zurrtum/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour.handleProcessingOnItem (Lcom/zurrtum/create/content/kinetics/belt/transport/TransportedItemStack;Lcom/zurrtum/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour$TransportedResult;)V"
    ), method = "activate(Lcom/zurrtum/create/content/kinetics/belt/transport/TransportedItemStack;Lcom/zurrtum/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour;Lcom/zurrtum/create/content/kinetics/deployer/DeployerBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;)V", cancellable = true)
    private static void activateMixin(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler, DeployerBlockEntity blockEntity, Recipe<?> recipe, CallbackInfo ci) {

        var player = blockEntity.getPlayer().cast();
        ItemStack heldItem = player.getMainHandItem();
        var level = blockEntity.getLevel();
        if (!(recipe instanceof ItemApplicationRecipe recipeInput)) {
            return;
        }
        if (!(heldItem.getItem() instanceof KnifeItem)) {
            return;
        }
        var matchingRecipes = level.recipeAccess().getSynchronizedRecipes().recipes().stream()
                .map(RecipeHolder::value)
                .filter(r -> r.getType().equals(ModRecipeTypes.CUTTING.get()))
                .filter(r -> r instanceof CuttingBoardRecipe)
                .map(r -> (CuttingBoardRecipe) r)
                .filter(cuttingBoardRecipe -> {
                    @SuppressWarnings("deprecation")
                    var cut = cuttingBoardRecipe.getInput().items().toList();
                    @SuppressWarnings("deprecation")
                    var inp = recipeInput.target().items().toList();
                    return cut.equals(inp);
                })
                .toList();
        if (matchingRecipes.isEmpty()) {
            return;
        }
        CuttingBoardRecipe recipeHolderMatch = matchingRecipes.getFirst();
        var heldStack = transported.copy();
        heldStack.stack.shrink(1);
        var cuttingResults = recipeHolderMatch.rollResults(level.random, EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), heldItem));
        var results = new ArrayList<>(cuttingResults.stream().map(
                r -> {
                    var result = transported.copy();
                    boolean centered = BeltHelper.isItemUpright(r);
                    result.stack = r.copy();
                    result.locked = false;
                    result.angle = centered ? 180 : player.getRandom().nextInt(360);
                    return result;
                }
        ).toList());
        if (results.isEmpty()) return;
        System.out.println(results);
        handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(results, heldStack));
        ci.cancel();
        level.playSound(null, blockEntity.getBlockPos(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, .25f, .75f);
//        results.removeFirst();
//        var location = blockEntity.getBlockPos().below();
//        for (TransportedItemStack result : results) {
//            ItemEntity entity = new ItemEntity(blockEntity.getLevel(), location.getX(), location.getY(), location.getZ(), result.stack.copy());
//            blockEntity.getLevel().addFreshEntity(entity);
//        }
    }
}
