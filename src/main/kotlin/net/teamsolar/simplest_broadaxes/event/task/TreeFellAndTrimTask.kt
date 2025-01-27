package net.teamsolar.simplest_broadaxes.event.task

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.teamsolar.simplest_broadaxes.Config

class TreeFellAndTrimTask(level: World, player: ServerPlayerEntity, position: BlockPos): TreeFellTask(level, player, position) {
    override fun collectBlocksToMine(): MutableList<BlockPos> =
        getEquippedBroadaxe()?.run {
            object : TaskBlockCollectorWithSecondaryTags {
                // In the context of broadaxes, primaryTags is logs but not leaves or other blocks.
                override val primaryTags: TagKey<Block> = item.mineableBlocks
                // In the context of broadaxes, secondaryTags is leaves, nether wart blocks, etc, and we want to collect them,
                // but not destroy entire forests.
                override val secondaryTags: TagKey<Block> = item.secondaryMineableBlocks
                override val level: World = this@TreeFellAndTrimTask.level
                override val player: ServerPlayerEntity = this@TreeFellAndTrimTask.player
                override val maxAdjacentBlocks: Int = Config.broadaxeBlocksPerSwing
            }.getBlocksToMine(position)
        } ?: mutableListOf()


    override fun canBreakBlock(pos: BlockPos): Boolean {
        val state = level.getBlockState(pos)
        // If the BroadaxeCtx is null, do not apply (i.e. do not run this block)
        getEquippedBroadaxe()?.apply {
            val mineableBlocks = item.mineableBlocks
            val secondaryMineableBlocks = item.secondaryMineableBlocks
            return state.isIn(mineableBlocks) || state.isIn(secondaryMineableBlocks)
        }
        return false
    }

    override fun damageBroadaxeIfEquipped(blockState: BlockState) {
        getEquippedBroadaxe()?.apply {
            if(blockState.isIn(item.secondaryMineableBlocks)) {
                val trimmingEnchantmentLevel = item.getTrimmingLevel(itemStack)
                val chanceOfDurabilityLost = 1.0 / (trimmingEnchantmentLevel + 1)
                if (player.random.nextDouble() < chanceOfDurabilityLost) {
                    super.damageBroadaxeIfEquipped(blockState)
                } else {
                    return
                }
            } else {
                return super.damageBroadaxeIfEquipped(blockState)
            }
        } ?: return super.damageBroadaxeIfEquipped(blockState)
    }
}