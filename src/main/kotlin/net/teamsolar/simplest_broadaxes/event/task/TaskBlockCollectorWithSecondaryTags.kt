package net.teamsolar.simplest_broadaxes.event.task

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.math.BlockPos
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes

interface TaskBlockCollectorWithSecondaryTags: TaskBlockCollector {
    val secondaryTags: TagKey<Block>
    override fun getBlocksToMine(position: BlockPos): MutableList<BlockPos> {
        val blocks = getBlocksTagged(start = position.add(0, 1, 0), ignore = position, selectedTags = listOf(primaryTags, secondaryTags))
        val treeBlocks = blocks.filterTagged {it.isIn(primaryTags)}
        val primaryTreeBlocks = getBlocksTagged(start = position.add(0, 1, 0), ignore = position, selectedTags = listOf(primaryTags))
        SimplestBroadaxes.logger.info("Num primary tree blocks: ${primaryTreeBlocks.size}")
        if(primaryTreeBlocks.any { blockPos -> blockPos.y <= position.y }) {
            return mutableListOf()
        } else {
            val nonPrimaryTreeBlocks = treeBlocks.minus(primaryTreeBlocks.toSet())
            val leaves = blocks.filterTagged{it.isIn(secondaryTags)}

            SimplestBroadaxes.logger.info("Num non-primary tree blocks: ${nonPrimaryTreeBlocks.size}")
            SimplestBroadaxes.logger.info("Num leaves: ${leaves.size}")

            fun isCloserToPrimaryTreeBlocks(blockPos: BlockPos): Boolean {
                val distance1: Double? = primaryTreeBlocks.minOfOrNull { blockPos.getSquaredDistance(it) }
                val distance2: Double? = nonPrimaryTreeBlocks.minOfOrNull { blockPos.getSquaredDistance(it) }
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

            val leavesToMine = leaves.filter(::isCloserToPrimaryTreeBlocks)

            return (
                primaryTreeBlocks
                + leaves.filter(::isCloserToPrimaryTreeBlocks).also {
                    SimplestBroadaxes.logger.info("(Num of leaves to mine: ${it.size})")
                }
            ).toMutableList()
        }
    }
    
    private inline fun List<BlockPos>.filterTagged(predicate: (BlockState) -> Boolean): List<BlockPos> = this.filter { pos -> predicate(level.getBlockState(pos)) }
}