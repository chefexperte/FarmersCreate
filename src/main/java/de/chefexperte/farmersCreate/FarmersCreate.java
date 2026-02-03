package de.chefexperte.farmersCreate;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;

public class FarmersCreate implements ModInitializer {

    public static final String MOD_ID = "farmerscreate";

    public static final TagKey<Item> DUMPLINGS_INGREDIENTS_TAG = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "dumplings_ingredients"));
    public static final TagKey<Item> BONE_BROTH_INGREDIENTS_TAG = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "bone_broth_ingredients"));

    @Override
    public void onInitialize() {
    }
}
