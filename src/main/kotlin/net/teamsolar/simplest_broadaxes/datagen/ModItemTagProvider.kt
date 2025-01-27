package net.teamsolar.simplest_broadaxes.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.ItemTags
import net.teamsolar.simplest_broadaxes.ModItemTags
import net.teamsolar.simplest_broadaxes.item.ModItems
import java.util.concurrent.CompletableFuture

class ModItemTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<WrapperLookup>
): ItemTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: WrapperLookup) {
        val tagBuilder = getOrCreateTagBuilder(ModItemTags.BROADAXES)
        for(broadaxe in ModItems.broadaxes) {
            tagBuilder.add(broadaxe)
        }
        // Sharpness enchantment
        getOrCreateTagBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE)
            .addTag(ModItemTags.BROADAXES)
        // Efficiency enchantment
        // getOrCreateTagBuilder(ItemTags.MINING_ENCHANTABLE)
        //      .addTag(ModTagItems.BROADAXES)
        // Fortune & Silk Touch
        getOrCreateTagBuilder(ItemTags.MINING_LOOT_ENCHANTABLE)
            .addTag(ModItemTags.BROADAXES)
        // Unbreaking, Mending, & Vanishing
        // (by transitive relationship with the VANISHING_ENCHANTABLE item tag - see VanillaItemTagsProvider)
        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
            .addTag(ModItemTags.BROADAXES)
    }
}