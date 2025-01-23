package net.teamsolar.simplest_broadaxes

import net.minecraft.client.gui.screens.Screen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.common.ModConfigSpec
import java.util.stream.Collectors

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = SimpestBroadaxes.MODID, bus = EventBusSubscriber.Bus.MOD)
object Config {
    private val BUILDER: ModConfigSpec.Builder = ModConfigSpec.Builder()

    private val BROADAXE_MINING_SPEED_MODIFIER: ModConfigSpec.DoubleValue =
        BUILDER.comment("What percentage of a vanilla axe's mining speed the broadaxe should be set to.")
            .gameRestart()
            .defineInRange("broadaxeMiningSpeedModifier", 0.5, 0.0, Double.MAX_VALUE)

    private val BROADAXE_BLOCKS_PER_TICK: ModConfigSpec.IntValue =
        BUILDER.comment("Number of log blocks that a broadaxe can fell in one tick (Lower -> Takes more ticks to fully fell a tree)")
            .comment("(Some interactions may not work as intended with lower # of blocks per tick)")
            .gameRestart()
            .defineInRange("broadaxeBlocksPerTick", 64, 1, Int.MAX_VALUE)
    private val BROADAXE_BLOCKS_PER_SWING: ModConfigSpec.IntValue =
        BUILDER.comment("Number of log blocks that a broadaxe can fell in one swing (Lower -> Less chance of the server crashing)")
            .comment("(Some interactions may not work as intended with lower # of blocks per tick)")
            .gameRestart()
            .defineInRange("broadaxeBlocksPerSwing", 1000, 1, Int.MAX_VALUE)
    private val DROPS_FELLED_BLOCKS: ModConfigSpec.BooleanValue =
        BUILDER.comment("Drops felled blocks")
            .comment(" true: blocks felled with a broadaxe are stacked together (less lag) and delivered to the player")
            .comment(" false: blocks are dropped where they are broken")
            .gameRestart()
            .define("dropsFelledBlocks", true)

    val SPEC: ModConfigSpec = BUILDER.build()

    var broadaxeMiningSpeedModifier: Double = 0.5
    var broadaxeBlocksPerTick: Int = 64
    var broadaxeBlocksPerSwing: Int = 1000
    var dropsFelledBlocks: Boolean = true

    fun registerConfig(modContainer: ModContainer) {
        modContainer.registerExtensionPoint(
            IConfigScreenFactory::class.java,
            IConfigScreenFactory { mod: ModContainer, parent: Screen -> ConfigurationScreen(mod, parent) })
    }

    @SubscribeEvent
    fun onLoad(event: ModConfigEvent) {
        broadaxeMiningSpeedModifier = BROADAXE_MINING_SPEED_MODIFIER.get()
        broadaxeBlocksPerTick = BROADAXE_BLOCKS_PER_TICK.get()
        broadaxeBlocksPerSwing = BROADAXE_BLOCKS_PER_SWING.get()
        dropsFelledBlocks = DROPS_FELLED_BLOCKS.get()
    }
}