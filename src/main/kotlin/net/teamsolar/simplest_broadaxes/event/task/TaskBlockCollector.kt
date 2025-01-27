package net.teamsolar.simplest_broadaxes.event.task

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface TaskBlockCollector {
    val primaryTags: TagKey<Block>
    val level: World
    val player: ServerPlayerEntity
    val maxAdjacentBlocks: Int

    fun getBlocksToMine(position: BlockPos): MutableList<BlockPos> {
        val blocks = getBlocksTagged(start = position.add(0, 1, 0), ignore = position, selectedTags = listOf(primaryTags))
        if(blocks.any { blockPos -> blockPos.y <= position.y } )
            return mutableListOf()
        else
            return blocks.toMutableList()
    }

    fun getBlocksTagged(start: BlockPos, ignore: BlockPos? = null, selectedTags: List<TagKey<Block>>): List<BlockPos> {
        fun isInSelectedTags(state: BlockState): Boolean {
            for(tag in selectedTags) {
                if(state.isIn(tag)) {
                    return true
                }
            }
            return false
        }
        val set = mutableSetOf(start)
        val searchQueue = mutableListOf(start)
        val list = mutableListOf(start)
        while(searchQueue.size > 0 && set.size < maxAdjacentBlocks) {
            val pos = searchQueue.removeFirst()
            for(pos2 in adjacentPositions(pos)) {
                if(pos2 != ignore && pos2 !in set && isInSelectedTags(level.getBlockState(pos2))) {
                    set.add(pos2)
                    searchQueue.add(pos2)
                    list.add(pos2)
                }
            }
        }
        return list
    }

    private fun adjacentPositions(pos: BlockPos): List<BlockPos> {
        val list = mutableListOf<BlockPos>()
        for(x in -1 .. 1) {
            for(y in -1 .. 1) {
                for(z in -1.. 1) {
                    val nextPos = pos.add(x, y, z)
                    if(nextPos != pos) {
                        list.add(nextPos)
                    }
                }
            }
        }
        return list
    }
}