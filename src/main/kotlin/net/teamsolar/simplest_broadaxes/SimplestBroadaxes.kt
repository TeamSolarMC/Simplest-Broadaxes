package net.teamsolar.simplest_broadaxes

import eu.midnightdust.lib.config.MidnightConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.teamsolar.simplest_broadaxes.loot.ModLootTableModifiers
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import net.teamsolar.simplest_broadaxes.event.ModCustomTrades
import net.teamsolar.simplest_broadaxes.item.BroadaxeItem
import net.teamsolar.simplest_broadaxes.item.ModItems
import org.slf4j.LoggerFactory

object SimplestBroadaxes : ModInitializer {
	val modid = "simplest_broadaxes"
    public val logger = LoggerFactory.getLogger("simplest_broadaxes")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		MidnightConfig.init(modid, Config::class.java)

		ModEnchantments.initialize()
		ModItems.initialize()

		ModLootTableModifiers.modifyLootTables()
		ModCustomTrades.registerCustomTrades()

		FuelRegistry.INSTANCE.add(ModItems.WOODEN_BROADAXE, 200)

		ServerTickEvents.START_WORLD_TICK.register(BroadaxeItem.tickEvent::onLevelTick)
		logger.info("Hello Fabric world!")
	}
}