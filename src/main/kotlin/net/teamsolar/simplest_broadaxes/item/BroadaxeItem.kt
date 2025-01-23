package net.teamsolar.simplest_broadaxes.item

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.teamsolar.simplest_broadaxes.ModTagBlocks
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import net.teamsolar.simplest_broadaxes.event.ModLevelTickEvent
import net.teamsolar.simplest_broadaxes.event.task.TreeFellAndTrimTask
import net.teamsolar.simplest_broadaxes.event.task.TreeFellTask


class BroadaxeItem(tier: Tier, properties: Properties) : DiggerItemWithoutDurability(tier, BlockTags.MINEABLE_WITH_AXE, properties) {
    override fun mineBlock(
        stack: ItemStack,
        level: Level,
        state: BlockState,
        pos: BlockPos,
        miningEntity: LivingEntity
    ): Boolean {
        return super.mineBlock(stack, level, state, pos, miningEntity).also {
            if(!level.isClientSide) {
                if(miningEntity is ServerPlayer && state.`is`(mineableBlocks) && !miningEntity.isShiftKeyDown) {
                    if(tickEvent.queuedBroadaxeTasks[level]?.let{
                            pos in it.blocksBeingMined
                        } != true) {
                        // tickEvent.addTask(level, FellTreeTask(level, miningEntity, pos))
                        // BroadaxeMod.LOGGER.info("Mineable blocks tag $mineableBlocks")
                        val trimmingLevel = getTrimmingLevel(stack)
                        SimplestBroadaxes.LOGGER.info("Trimming level on axe $trimmingLevel")
                        if(trimmingLevel > 0) {
                            tickEvent.addTask(level, TreeFellAndTrimTask(level, miningEntity, pos))
                        } else {
                            tickEvent.addTask(level, TreeFellTask(level, miningEntity, pos))
                        }
                    }
                }
            }
        }
    }

    fun getTrimmingLevel(itemStack: ItemStack): Int {
        var myInt = 0
        EnchantmentHelper.runIterationOnItem(itemStack, {enchantmentHolder, enchantLevel ->
            if(enchantmentHolder.`is` (ModEnchantments.BROADAXE_ENCHANTMENT)) {
                myInt += enchantLevel
            }
        })
        return myInt
    }

    val miningSpeedModifier = 0.4f

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        return super.getDestroySpeed(stack, state) * miningSpeedModifier
    }

    val mineableBlocks: TagKey<Block> = ModTagBlocks.FELLABLE_BLOCK
    val secondaryMineableBlocks: TagKey<Block> = ModTagBlocks.SECONDARY_FELLABLE_BLOCK

    companion object {
        val tickEvent = ModLevelTickEvent()
    }
}