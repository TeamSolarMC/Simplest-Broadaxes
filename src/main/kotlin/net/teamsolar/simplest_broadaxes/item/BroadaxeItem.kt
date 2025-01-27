package net.teamsolar.simplest_broadaxes.item

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.enchantment.EfficiencyEnchantment
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolMaterial
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.teamsolar.simplest_broadaxes.ModBlockTags
import net.teamsolar.simplest_broadaxes.enchantment.ItemExclusiveTo
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import net.teamsolar.simplest_broadaxes.event.ModLevelTickEvent
import net.teamsolar.simplest_broadaxes.event.task.TreeFellAndTrimTask
import net.teamsolar.simplest_broadaxes.event.task.TreeFellTask


open class BroadaxeItem
    : MiningToolItem, ItemExclusiveTo {
    constructor(toolMaterial: ToolMaterial, attackDamage: Float, attackSpeed: Float, properties: Item.Settings) : super(attackDamage, attackSpeed, toolMaterial, BlockTags.AXE_MINEABLE, properties)

    override fun postMine(
        stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miningEntity: LivingEntity
    ): Boolean {
        return super.postMine(stack, world, state, pos, miningEntity) . also {
            if(!world.isClient) {
                if(miningEntity is ServerPlayerEntity && state.isIn(mineableBlocks) && !miningEntity.isSneaking) {
                    if(tickEvent.queuedBroadaxeTasks[world]?.let{
                            pos in it.blocksBeingMined
                        } != true) {
                        val trimmingLevel = getTrimmingLevel(stack)
                        if(trimmingLevel > 0) {
                            tickEvent.addTask(world, TreeFellAndTrimTask(world, miningEntity, pos))
                        } else {
                            tickEvent.addTask(world, TreeFellTask(world, miningEntity, pos))
                        }
                    }
                }
            }
        }
    }

    fun getTrimmingLevel(itemStack: ItemStack): Int {
        val level = EnchantmentHelper.getLevel(ModEnchantments.TRIMMING, itemStack)
        return level
    }

    val miningSpeedModifier = 0.4f

    override fun getMiningSpeedMultiplier(stack: ItemStack, state: BlockState): Float {
        return super.getMiningSpeedMultiplier(stack, state) * miningSpeedModifier
    }

    // disallow enchanting this item with efficiency
    override fun isExcludedEnchantment(enchantment: Enchantment): Boolean {
        return enchantment is EfficiencyEnchantment
    }

    /*override fun getMiningSpeedMultiplier(stack: ItemStack, state: BlockState): Float {
        return super.getMiningSpeedMultiplier(stack, state).also {
            SimplestBroadaxes.logger.info("Base destroy speed: $it")
        } * miningSpeedModifier
    }*/

    // val effectiveBlocks = BlockTags.AXE_MINEABLE
    val mineableBlocks: TagKey<Block> = ModBlockTags.FELLABLE_BLOCK
    val secondaryMineableBlocks: TagKey<Block> = ModBlockTags.SECONDARY_FELLABLE_BLOCK

    companion object {
        val tickEvent = ModLevelTickEvent()
    }
}