package net.teamsolar.simplest_broadaxes

import eu.midnightdust.lib.config.MidnightConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.world.WorldTickCallback
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.world.tick.WorldTickScheduler
import net.teamsolar.simplest_broadaxes.datagen.ModLootTableModifiers
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import net.teamsolar.simplest_broadaxes.enchantment.TrimmingEnchantment
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

		WorldTickCallback.EVENT.register(BroadaxeItem.tickEvent::onLevelTick)
		logger.info("Hello Fabric world!")
	}
}