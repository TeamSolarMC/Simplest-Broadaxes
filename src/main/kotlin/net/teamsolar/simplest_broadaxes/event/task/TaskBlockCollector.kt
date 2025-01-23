package net.teamsolar.simplest_broadaxes.event.task

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

interface TaskBlockCollector {
    val primaryTags: TagKey<Block>
    val level: Level
    val player: ServerPlayer
    val maxAdjacentBlocks: Int

    fun getBlocksToMine(position: BlockPos): MutableList<BlockPos> {
        val blocks = getBlocksTagged(start = position.offset(0, 1, 0), ignore = position, selectedTags = listOf(primaryTags))
        if(blocks.any { blockPos -> blockPos.y <= position.y } )
            return mutableListOf()
        else
            return blocks.toMutableList()
    }

    fun getBlocksTagged(start: BlockPos, ignore: BlockPos? = null, selectedTags: List<TagKey<Block>>): List<BlockPos> {
        fun isInSelectedTags(state: BlockState): Boolean {
            for(tag in selectedTags) {
                if(state.`is`(tag)) {
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

    fun adjacentPositions(pos: BlockPos): List<BlockPos> {
        val list = mutableListOf<BlockPos>()
        for(x in -1 .. 1) {
            for(y in -1 .. 1) {
                for(z in -1.. 1) {
                    val nextPos = pos.offset(x, y, z)
                    if(nextPos != pos) {
                        list.add(nextPos)
                    }
                }
            }
        }
        return list
    }
}