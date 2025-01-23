package net.teamsolar.simplest_broadaxes

import com.mojang.logging.LogUtils
import net.minecraft.client.Minecraft
import net.minecraft.world.item.*
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.server.ServerStartingEvent
import net.teamsolar.simplest_broadaxes.item.BroadaxeItem
import net.teamsolar.simplest_broadaxes.item.ModItems
import net.teamsolar.simplest_broadaxes.loot.ModLootModifiers

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SimplestBroadaxes.MODID)
class SimplestBroadaxes {
    companion object {
        // Define mod id in a common place for everything to reference
        const val MODID = "simplest_broadaxes"
        // Directly reference a slf4j logger
        public val LOGGER = LogUtils.getLogger();

        // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
        @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
        object ClientModEvents {
            @SubscribeEvent
            fun onClientSetup(event: FMLClientSetupEvent?) {
                // Some client setup code
                LOGGER.info("HELLO FROM CLIENT SETUP")
                LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().user.name)
            }
        }
    }

    constructor(modEventBus: IEventBus, modContainer: ModContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(::commonSetup)
        modEventBus.addListener(::serverSetup)

        ModItems.register(modEventBus)

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this)

        // Register the item to a creative tab
        modEventBus.addListener(::addCreative)

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC)

        ModLootModifiers.register(modEventBus)

        // Default config screen
        Config.registerConfig(modContainer)
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP")

        LOGGER.info("CREATING MyLevelTickEvent AND REGISTERING ON THE EVENT BUS")
        NeoForge.EVENT_BUS.register(BroadaxeItem.tickEvent)
    }

    private fun serverSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.info("HELLO FROM SERVER SETUP")
    }

    // Add the example block item to the building blocks tab
    private fun addCreative(event: BuildCreativeModeTabContentsEvent) {
        when(event.tabKey) {
            CreativeModeTabs.TOOLS_AND_UTILITIES, CreativeModeTabs.COMBAT -> {
                event.accept(ModItems.WOODEN_BROADAXE)
                event.accept(ModItems.STONE_BROADAXE)
                event.accept(ModItems.IRON_BROADAXE)
                event.accept(ModItems.GOLDEN_BROADAXE)
                event.accept(ModItems.DIAMOND_BROADAXE)
                event.accept(ModItems.NETHERITE_BROADAXE)
            }
            CreativeModeTabs.INGREDIENTS -> {
                event.accept(ModItems.BROADAXE_SMITHING_TEMPLATE)
            }
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    fun onServerStarting(event: ServerStartingEvent) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting")
    }
}