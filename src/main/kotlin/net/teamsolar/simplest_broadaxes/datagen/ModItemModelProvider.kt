package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredItem
import net.teamsolar.simplest_broadaxes.SimpestBroadaxes
import net.teamsolar.simplest_broadaxes.item.ModItems.BROADAXE_SMITHING_TEMPLATE
import net.teamsolar.simplest_broadaxes.item.ModItems.DIAMOND_BROADAXE
import net.teamsolar.simplest_broadaxes.item.ModItems.GOLDEN_BROADAXE
import net.teamsolar.simplest_broadaxes.item.ModItems.IRON_BROADAXE
import net.teamsolar.simplest_broadaxes.item.ModItems.NETHERITE_BROADAXE
import net.teamsolar.simplest_broadaxes.item.ModItems.STONE_BROADAXE
import net.teamsolar.simplest_broadaxes.item.ModItems.WOODEN_BROADAXE

class ModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, SimpestBroadaxes.MODID, existingFileHelper) {
    override fun registerModels() {
        handheldItem(WOODEN_BROADAXE)
        handheldItem(STONE_BROADAXE)
        handheldItem(IRON_BROADAXE)
        handheldItem(GOLDEN_BROADAXE)
        handheldItem(DIAMOND_BROADAXE)
        handheldItem(NETHERITE_BROADAXE)

        simpleItem(BROADAXE_SMITHING_TEMPLATE)
    }

    private fun <T : Item> handheldItem(item: DeferredItem<T>): ItemModelBuilder {
        return withExistingParent(
            item.id.path,
            mcLoc("item/handheld")
        ).texture(
            "layer0",
            modLoc("item/" + item.id.path)
        )
    }

    private fun <T : Item> simpleItem(item: DeferredItem<T>): ItemModelBuilder {
        return withExistingParent(
            item.id.path,
            mcLoc("item/generated")
        ).texture(
            "layer0",
            modLoc("item/" + item.id.path)
        )
    }
}
