package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes

@EventBusSubscriber(modid = SimplestBroadaxes.MODID, bus = EventBusSubscriber.Bus.MOD)
object DataGenerators {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        fun <T: DataProvider> DataGenerator.add(provider: T) {
            addProvider(event.includeServer(), provider)
        }
        SimplestBroadaxes.LOGGER.info("Loading gatherdata")
        val generator = event.generator
        val packOutput = generator.packOutput
        val existingFileHelper = event.existingFileHelper
        val lookupProvider = event.lookupProvider
        generator.add(ModItemModelProvider(packOutput, existingFileHelper))
        val blockTagGenerator = ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper)
            .also{generator.add(it)}
        val datapackProvider = ModDatapackProvider(packOutput, lookupProvider)

        generator.add(ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper))
        // Must pass in our own lookupProvider or else datagen won't know where to find our enchantment in registries... apparently
        generator.add(ModEnchantmentTagsProvider(packOutput, datapackProvider.registryProvider, existingFileHelper))
        generator.add(ModRecipeProvider(packOutput, lookupProvider))
        generator.add(ModGlobalLootModifierProvider(packOutput, lookupProvider))
        generator.add(ModDataMapProvider(packOutput, lookupProvider))
        generator.add(datapackProvider)
    }
}