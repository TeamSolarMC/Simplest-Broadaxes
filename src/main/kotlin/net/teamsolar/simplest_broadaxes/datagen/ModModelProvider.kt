package net.teamsolar.simplest_broadaxes.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models
import net.teamsolar.simplest_broadaxes.item.ModItems

class ModModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) {

    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) {
        for(broadaxe in ModItems.broadaxes) {
            itemModelGenerator.register(broadaxe, Models.HANDHELD)

        }
        itemModelGenerator.register(ModItems.BROADAXE_SMITHING_TEMPLATE, Models.GENERATED)
    }
}