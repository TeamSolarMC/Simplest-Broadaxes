package net.teamsolar.simplest_broadaxes.datagen

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.teamsolar.simplest_broadaxes.SimpestBroadaxes

@EventBusSubscriber(modid = SimpestBroadaxes.MODID, bus = EventBusSubscriber.Bus.MOD)
object DataGenerators {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        SimpestBroadaxes.LOGGER.info("Loading gatherdata")
        val generator = event.generator
        val packOutput = generator.packOutput
        val existingFileHelper = event.existingFileHelper
        val lookupProvider = event.lookupProvider
        generator.addProvider(event.includeServer(), ModItemModelProvider(packOutput, existingFileHelper))

        val blockTagGenerator = generator.addProvider(
            event.includeServer(),
            ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper)
        )
        generator.addProvider(
            event.includeServer(),
            ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper)
        )

        generator.addProvider(event.includeServer(), ModRecipeProvider(packOutput, lookupProvider))

        generator.addProvider(event.includeServer(), ModGlobalLootModifierProvider(packOutput, lookupProvider))

        generator.addProvider(event.includeServer(), ModDataMapProvider(packOutput, lookupProvider))
    }
}