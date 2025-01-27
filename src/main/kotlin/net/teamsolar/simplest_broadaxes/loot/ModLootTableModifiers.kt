package net.teamsolar.simplest_broadaxes.loot

import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.loot.LootPool
import net.minecraft.loot.condition.RandomChanceLootCondition
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.SetCountLootFunction
import net.minecraft.loot.provider.number.ConstantLootNumberProvider
import net.minecraft.loot.provider.number.UniformLootNumberProvider
import net.teamsolar.simplest_broadaxes.item.ModItems

object ModLootTableModifiers {
    val higherChanceChests: Map<String, Float> = mapOf(
        "chests/village/village_toolsmith" to 0.75f,
        "chests/village/village_armorer" to 0.75f,
        "chests/village/village_weaponsmith" to 0.75f
    )

    fun modifyLootTables() {
        val smithingTemplate = ModItems.BROADAXE_SMITHING_TEMPLATE

        LootTableEvents.MODIFY.register {
            registryKey, tableBuilder, source, wrapperLookup ->
            val id = registryKey.value
            if(matchHouseType(id.path) != null) {
                val poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
                    .conditionally(RandomChanceLootCondition.builder(0.15f))
                    .with(ItemEntry.builder(smithingTemplate))
                    .apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build()
                    )
                tableBuilder.pool(poolBuilder.build())
            }
            if(higherChanceChests[id.path] != null) {
                val poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
                    .conditionally(RandomChanceLootCondition.builder(higherChanceChests[id.path]!!))
                    .with(ItemEntry.builder(smithingTemplate))
                    .apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build()
                    )
                tableBuilder.pool(poolBuilder.build())
            }
        }

        /*LootTableEvents.MODIFY.register {
            resourceManager, lootManager, id, tableBuilder, source ->
            if(matchHouseType(id.path) != null) {
                val poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
                    .conditionally(RandomChanceLootCondition.builder(0.15f))
                    .with(ItemEntry.builder(smithingTemplate))
                    .apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build()
                    )
                tableBuilder.pool(poolBuilder.build())
            }
            if(higherChanceChests[id.path] != null) {
                val poolBuilder = LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f))
                    .conditionally(RandomChanceLootCondition.builder(higherChanceChests[id.path]!!))
                    .with(ItemEntry.builder(smithingTemplate))
                    .apply(
                        SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build()
                    )
                tableBuilder.pool(poolBuilder.build())
            }
        }*/
    }

    private fun matchHouseType(key: String): String? {
        val nameMatcher = Regex("chests/village/(.+_house)")
        val matchResults = nameMatcher.matchEntire(key)
        if(matchResults != null) {
            return matchResults.groupValues[1]
        } else {
            return null
        }
    }

}