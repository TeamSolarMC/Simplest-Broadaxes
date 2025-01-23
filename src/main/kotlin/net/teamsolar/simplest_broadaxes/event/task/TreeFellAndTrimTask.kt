package net.teamsolar.simplest_broadaxes.event.task

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.teamsolar.simplest_broadaxes.Config

class TreeFellAndTrimTask(level: Level, player: ServerPlayer, position: BlockPos): TreeFellTask(level, player, position) {
    override fun collectBlocksToMine(): MutableList<BlockPos> =
        getEquippedBroadaxe()?.run {
            object : TaskBlockCollectorWithSecondaryTags {
                // In the context of broadaxes, primaryTags is logs but not leaves or other blocks.
                override val primaryTags: TagKey<Block> = item.mineableBlocks
                // In the context of broadaxes, secondaryTags is leaves, nether wart blocks, etc, and we want to collect them,
                // but not destroy entire forests.
                override val secondaryTags: TagKey<Block> = item.secondaryMineableBlocks
                override val level: Level = this@TreeFellAndTrimTask.level
                override val player: ServerPlayer = this@TreeFellAndTrimTask.player
                override val maxAdjacentBlocks: Int = Config.broadaxeBlocksPerSwing
            }.getBlocksToMine(position)
        } ?: mutableListOf()


    override fun canBreakBlock(pos: BlockPos): Boolean {
        val state = level.getBlockState(pos)
        // If the BroadaxeCtx is null, do not apply (i.e. do not run this block)
        getEquippedBroadaxe()?.apply {
            val mineableBlocks = item.mineableBlocks
            val secondaryMineableBlocks = item.secondaryMineableBlocks
            return state.`is`(mineableBlocks) || state.`is`(secondaryMineableBlocks)
        }
        return false
    }

    override fun damageBroadaxeIfEquipped(blockState: BlockState) {
        getEquippedBroadaxe()?.apply {
            if(blockState.`is`(item.secondaryMineableBlocks)) {
                val trimmingEnchantmentLevel = item.getTrimmingLevel(itemStack)
                val chanceOfDurabilityLost = 1.0 / (trimmingEnchantmentLevel + 1)
                if (player.random.nextDouble() < chanceOfDurabilityLost) {
                    super.damageBroadaxeIfEquipped(blockState)
                } else {
                    return
                }
            } else {
                return
            }
        } ?: return
    }
}