package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import net.teamsolar.simplest_broadaxes.item.ModItems.WOODEN_BROADAXE
import java.util.concurrent.CompletableFuture

class ModDataMapProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider?>) :
    DataMapProvider(packOutput, lookupProvider) {
    override fun gather() {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
            .add(WOODEN_BROADAXE.id, FurnaceFuel(200), false)
    }
}
