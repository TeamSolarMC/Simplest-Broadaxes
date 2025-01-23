package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import java.util.concurrent.CompletableFuture

class ModDatapackProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>): DatapackBuiltinEntriesProvider(output, registries, BUILDER, setOf(SimplestBroadaxes.MODID)) {
    companion object {
        val BUILDER = RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap)
    }
}