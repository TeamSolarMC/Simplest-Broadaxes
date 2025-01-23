package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.LootTableIdCondition
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.item.ModItems
import net.teamsolar.simplest_broadaxes.loot.ChestLootModifier
import java.util.concurrent.CompletableFuture

class ModGlobalLootModifierProvider(output: PackOutput, completableFuture: CompletableFuture<HolderLookup.Provider?>) :
    GlobalLootModifierProvider(output, completableFuture, SimplestBroadaxes.MODID) {
    override fun start() {
        val template = ModItems.BROADAXE_SMITHING_TEMPLATE.get()
        add(
            "broadaxe_smithing_template_modifier_in_toolsmith_chests",
            toExistingLootPoolWithChance(
                "chests/village/village_toolsmith",
                0.75f,
                template
            )
        )
        add(
            "broadaxe_smithing_template_modifier_in_armorer_chests",
            toExistingLootPoolWithChance(
                "chests/village/village_armorer",
                0.75f,
                template
            )
        )
        add(
            "broadaxe_smithing_template_modifier_in_weaponsmith_chests",
            toExistingLootPoolWithChance(
                "chests/village/village_weaponsmith",
                0.75f,
                template
            )
        )

        for (table in BuiltInLootTables.all()) {
            val key = table.location().path
            if (key.startsWith("chests/village") && key.matches("chests/village/(.+_house)".toRegex())) {
                add(
                    "broadaxe_smithing_template_in_" + matchHouseType(key),
                    toExistingLootPoolWithChance(
                        table.location(),
                        0.15f,
                        template
                    )
                )
            }
        }
    }

    private fun matchHouseType(key: String): String {
        val nameMatcher = Regex("chests/village/(.+_house)")
        return nameMatcher.matchEntire(key)!!.groupValues[1]
    }

    private fun toExistingLootPoolWithChance(location: ResourceLocation, chance: Float, item: Item): ChestLootModifier {
        return ChestLootModifier(
            arrayOf(
                LootTableIdCondition.builder(location)
                    .and(
                        LootItemRandomChanceCondition.randomChance(chance)
                    ).build()
            ),
            item
        )
    }

    private fun toExistingLootPoolWithChance(location: String, chance: Float, item: Item): ChestLootModifier {
        return toExistingLootPoolWithChance(ResourceLocation.withDefaultNamespace(location), chance, item)
    }
}
