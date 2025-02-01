package net.teamsolar.simplest_broadaxes.enchantment

import net.minecraft.item.ItemStack

interface EnchantmentExclusiveTo {
    fun isExcludedItem(stack: ItemStack): Boolean
}