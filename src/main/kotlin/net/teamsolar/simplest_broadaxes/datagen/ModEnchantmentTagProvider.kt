package net.teamsolar.simplest_broadaxes.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EnchantmentTagProvider
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.EnchantmentTags
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import java.util.concurrent.CompletableFuture

class ModEnchantmentTagProvider(output: FabricDataOutput, completableFuture: CompletableFuture<WrapperLookup>): EnchantmentTagProvider(output, completableFuture) {
    override fun configure(wrapperLookup: WrapperLookup) {
        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
            .add(ModEnchantments.TRIMMING)
    }
}