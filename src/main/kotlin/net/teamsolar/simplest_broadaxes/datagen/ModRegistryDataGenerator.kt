package net.teamsolar.simplest_broadaxes.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import java.util.concurrent.CompletableFuture

class ModRegistryDataGenerator(output: FabricDataOutput,
                               registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun getName(): String = ""

    override fun configure(registries: RegistryWrapper.WrapperLookup, entries: Entries) {
        entries.addAll(
            registries.getWrapperOrThrow(RegistryKeys.ENCHANTMENT)
        )
    }
}