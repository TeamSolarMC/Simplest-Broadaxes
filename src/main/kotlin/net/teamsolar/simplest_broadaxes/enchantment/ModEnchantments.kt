package net.teamsolar.simplest_broadaxes.enchantment

import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EquipmentSlot
import net.minecraft.registry.*
import net.minecraft.util.Identifier
import net.teamsolar.simplest_broadaxes.ModItemTags
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes

object ModEnchantments {
    public val TRIMMING: RegistryKey<Enchantment> = of("trimming")

    private fun of(name: String) = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SimplestBroadaxes.modid, name))
    private fun register(registry: Registerable<Enchantment>, key: RegistryKey<Enchantment>, builder: Enchantment.Builder) {
        registry.register(key, builder.build(key.value))
    }
    fun bootstrap(registry: Registerable<Enchantment>) {
        val enchantments = registry.getRegistryLookup(RegistryKeys.ENCHANTMENT)
        val items = registry.getRegistryLookup(RegistryKeys.ITEM)

        register(registry, TRIMMING,
            Enchantment.builder(
                Enchantment.definition(
                    items.getOrThrow(ModItemTags.BROADAXES),
                    5,
                    3,
                    Enchantment.leveledCost(5, 8),
                    Enchantment.leveledCost(55, 8),
                    2,
                    AttributeModifierSlot.MAINHAND
                )
            )
        )
    }

    fun initialize() {

    }
}