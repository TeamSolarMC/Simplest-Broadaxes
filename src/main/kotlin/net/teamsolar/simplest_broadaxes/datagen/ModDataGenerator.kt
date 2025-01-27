package net.teamsolar.simplest_broadaxes.datagen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistryBuilder
import net.minecraft.registry.RegistryKeys
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments

object ModDataGenerator : DataGeneratorEntrypoint {
	override fun buildRegistry(registryBuilder: RegistryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap)
	}
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack = fabricDataGenerator.createPack()

		pack.addProvider(::ModBlockTagProvider)
		pack.addProvider(::ModModelProvider)
		pack.addProvider(::ModRecipeGenerator)
		// Must pass in our own lookupProvider or else datagen won't know where to find our enchantment in registries... apparently
		pack.addProvider(::ModEnchantmentTagProvider)
		pack.addProvider(::ModItemTagProvider)
		pack.addProvider(::ModRegistryDataGenerator)
	}
}