package net.teamsolar.simplest_broadaxes.enchantment

import net.minecraft.enchantment.Enchantment

interface ItemExclusiveTo {
    fun isExcludedEnchantment(enchantment: Enchantment): Boolean
}