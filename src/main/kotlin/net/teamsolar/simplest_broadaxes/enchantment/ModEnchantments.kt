package net.teamsolar.simplest_broadaxes.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes.modid

object ModEnchantments {
    public val TRIMMING: Enchantment = TrimmingEnchantment()
    fun initialize() {
        Registry.register(Registries.ENCHANTMENT, Identifier.of(modid, "trimming"), TRIMMING)
    }
}