package net.teamsolar.simplest_broadaxes.enchantment

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.item.BroadaxeItem

class TrimmingEnchantment: Enchantment(Enchantments.UNBREAKING.rarity, EnchantmentTarget.DIGGER, arrayOf(EquipmentSlot.MAINHAND)) {
    override fun isAcceptableItem(stack: ItemStack): Boolean {
        SimplestBroadaxes.logger.info("Is Acceptable Item Trimming Enchantment was called")
        return stack.item is BroadaxeItem
    }

    override fun getMaxLevel(): Int = 3
    override fun getMinLevel(): Int = 1
    override fun getMinPower(level: Int): Int {
        return 5 + (level - 1) * 8
    }
    override fun getMaxPower(level: Int): Int {
        return super.getMinPower(level) + 50
    }
}

