package net.teamsolar.simplest_broadaxes.event.task

import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes

interface TaskBlockCollectorWithSecondaryTags: TaskBlockCollector {
    val secondaryTags: TagKey<Block>
    override fun getBlocksToMine(position: BlockPos): MutableList<BlockPos> {
        val blocks = getBlocksTagged(start = position.offset(0, 1, 0), ignore = position, selectedTags = listOf(primaryTags, secondaryTags))
        val treeBlocks = blocks.filterTagged {it.`is`(primaryTags)}
        val primaryTreeBlocks = getBlocksTagged(start = position.offset(0, 1, 0), ignore = position, selectedTags = listOf(primaryTags))
        SimplestBroadaxes.LOGGER.info("Num primary tree blocks: ${primaryTreeBlocks.size}")
        if(primaryTreeBlocks.any { blockPos -> blockPos.y <= position.y }) {
            return mutableListOf()
        } else {
            val nonPrimaryTreeBlocks = treeBlocks.minus(primaryTreeBlocks.toSet())
            val leaves = blocks.filterTagged{it.`is`(secondaryTags)}

            SimplestBroadaxes.LOGGER.info("Num non-primary tree blocks: ${nonPrimaryTreeBlocks.size}")
            SimplestBroadaxes.LOGGER.info("Num leaves: ${leaves.size}")

            fun isCloserToPrimaryTreeBlocks(blockPos: BlockPos): Boolean {
                val distance1: Double? = primaryTreeBlocks.minOfOrNull { blockPos.distSqr(it) }
                val distance2: Double? = nonPrimaryTreeBlocks.minOfOrNull { blockPos.distSqr(it) }
                if(distance2 == null) {
                    return true
                }
                if(distance1 == null) {
                    return false
                }
                if(distance1 < distance2) {
                    return true
                } else if (distance1 == distance2) {
                    return level.random.nextBoolean()
                } else {
                    return false
                }
            }
            return (
                primaryTreeBlocks
                + leaves.filter(::isCloserToPrimaryTreeBlocks).also {
                    SimplestBroadaxes.LOGGER.info("(Num of leaves to mine: ${it.size})")
                }
            ).toMutableList()
        }
    }
    
    private inline fun List<BlockPos>.filterTagged(predicate: (BlockState) -> Boolean): List<BlockPos> = this.filter { pos -> predicate(level.getBlockState(pos)) }
}