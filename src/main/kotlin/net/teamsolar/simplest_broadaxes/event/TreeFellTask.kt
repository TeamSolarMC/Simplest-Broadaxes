package net.teamsolar.simplest_broadaxes.event

import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3
import net.teamsolar.simplest_broadaxes.Config
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.item.BroadaxeItem

class TreeFellTask(level: Level, player: ServerPlayer, position: BlockPos, val secondaryMineableBlocks: TagKey<Block>): ModLevelTickEvent.BroadaxeTask(level, player, position) {
    // 1. Collect up to 1000 adjacent blocks
    // and ignore any non-log blocks
    // 2. Don't cut down any additional blocks if it is not a root
    // 3. Cut 64 blocks per tick from this list
    val maxAdjacentBlocks = Config.broadaxeBlocksPerSwing
    val blocksPerTick = Config.broadaxeBlocksPerTick
    val deliversBlocks = Config.dropsFelledBlocks

    private val blocksToMine: MutableList<BlockPos> = getBlocksToMine(start = position.offset(0, 1, 0), ignore = position).let {
            blocks ->

        val itemStack = player.mainHandItem
        val item = itemStack.item
        val mineableBlocks: TagKey<Block>? =
            if(item is BroadaxeItem) {
                item.getMineableBlocks(itemStack)
            } else {
                null
            }
        if(mineableBlocks == null) {
            mutableListOf()
        } else {
            val treeBlocks = filterBlocksTagged(blocks) {it.`is`(mineableBlocks)}
            val primaryTreeBlocks = getBlocksTagged(start = position.offset(0, 1, 0), ignore = position, selectedTags = listOf(mineableBlocks))
            SimplestBroadaxes.LOGGER.info("Num primary tree blocks: ${primaryTreeBlocks.size}")
            if(primaryTreeBlocks.any { blockPos -> blockPos.y <= position.y }) {
                mutableListOf()
            } else {
                val nonPrimaryTreeBlocks = treeBlocks.minus(primaryTreeBlocks.toSet())
                val leaves = filterBlocksTagged(blocks) {it.`is`(secondaryMineableBlocks)}

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
                (
                        primaryTreeBlocks
                                + leaves.filter(::isCloserToPrimaryTreeBlocks)
                            .also{
                                SimplestBroadaxes.LOGGER.info("Num leaves to actually mine: ${it.size}")
                            }
                        ).toMutableList()
            }
        }
    }

    fun canBreakBlock(pos: BlockPos): Boolean {
        val itemStack = player.mainHandItem
        val item = itemStack.item
        val state = level.getBlockState(pos)
        if(item is BroadaxeItem) {
            val mineableBlocks = item.getMineableBlocks(itemStack)
            return state.`is`(mineableBlocks) || state.`is`(secondaryMineableBlocks)
        }
        return false
    }
    fun popFirstBreakableBlock(): BlockPos? {
        while (blocksToMine.isNotEmpty()) {
            val pos = blocksToMine.removeLast()
            if(canBreakBlock(pos)) {
                return pos
            }
        }
        return null
    }
    fun isBroadaxeEquipped(): Boolean {
        val itemStack = player.mainHandItem
        if(itemStack.isEmpty) { return false }
        return itemStack.item is BroadaxeItem
    }
    fun damageBroadaxeIfEquipped() {
        val itemStack = player.mainHandItem
        val tool = itemStack.get(DataComponents.TOOL)!!
        itemStack.hurtAndBreak(tool.damagePerBlock(), player, EquipmentSlot.MAINHAND)
    }
    private fun transferItemStacks(stackFrom: ItemStack, stackTo: ItemStack) {
        // Refer to: Shift+click behavior from AbstractContainerMenu (moveItemStackTo)
        // and merge behavior from ItemEntity (merge)
        val j: Int = stackTo.count + stackFrom.count
        // Consider the drops as being stackable up to their max amount
        // (because they are dropped as item entities)
        val k = stackTo.maxStackSize
        // val k: Int = getMaxStackSize(stackTo)
        if (j <= k) {
            stackFrom.count = 0
            stackTo.count = j
        } else if (stackTo.count < k) {
            stackFrom.shrink(k - stackTo.count)
            stackTo.count = k
        }
    }

    var taskProgress = 0
    override fun progress() {
        SimplestBroadaxes.LOGGER.info("Broadaxe Task ($position) progress: $taskProgress (${blocksToMine.size} remaining blocks)")
        taskProgress = 0
        val listOfItemsToMove = mutableListOf<ItemStack>()
        while(taskProgress < blocksPerTick && blocksToMine.isNotEmpty()) {
            val pos = popFirstBreakableBlock()
            if(pos != null) {
                val blockState = level.getBlockState(pos)
                if(!deliversBlocks) {
                    level.destroyBlock(pos, true, player)
                } else {
                    val itemsFromBlock = blockState.getDrops(
                        LootParams.Builder(level as ServerLevel)
                        .withParameter(LootContextParams.TOOL, player.mainHandItem)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(this.position))
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos))
                    )
                    for(item in itemsFromBlock) {
                        for(presentItemStack in listOfItemsToMove) {
                            if(!presentItemStack.isEmpty && ItemStack.isSameItemSameComponents(item, presentItemStack)) {
                                transferItemStacks(item, presentItemStack)
                            }
                        }
                    }
                    for(item in itemsFromBlock) {
                        if(!item.isEmpty) {
                            listOfItemsToMove.add(item)
                        }
                    }
                    level.destroyBlock(pos, false, player)
                }
                damageBroadaxeIfEquipped()
            }
            taskProgress++
        }
        for(drop in listOfItemsToMove) {
            Block.popResource(level, position, drop)
        }
        /*taskProgress++
        if(taskProgress == 2) {
            if(isBroadaxeEquipped() && player.level() == level) {
                val pos = popFirstBreakableBlock()
                if(pos != null) {
                    Broadaxe.tickEvent.queuedBroadaxeTasks[level]!!.blocksBeingMined.add(pos)
                    val blockState = level.getBlockState(pos)
                    level.destroyBlock(pos, true, player)
                    damageBroadaxeIfEquipped()
                    Broadaxe.tickEvent.queuedBroadaxeTasks[level]!!.blocksBeingMined.remove(pos)
                    taskProgress = 0
                }
            }
        }*/
    }

    override fun isFinished(): Boolean {
        return blocksToMine.isEmpty() || !isBroadaxeEquipped() || player.level() != level
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
    // determine if this is the lowest block in the mineable tree
    fun getBlocksToMine(start: BlockPos = this.position, ignore: BlockPos? = null): List<BlockPos> {
        val set = mutableSetOf(start)
        val searchQueue = mutableListOf(start)
        val list = mutableListOf(start)
        while(searchQueue.size > 0 && set.size < maxAdjacentBlocks) {
            val pos = searchQueue.removeFirst()
            for(pos2 in adjacentPositions(pos)) {
                if(pos2 != ignore && pos2 !in set && canBreakBlock(pos2)) {
                    set.add(pos2)
                    searchQueue.add(pos2)
                    list.add(pos2)
                }
            }
        }
        return list
    }

    inline fun filterBlocksTagged(list: List<BlockPos>, predicate: (BlockState) -> Boolean): List<BlockPos> = list.filter { pos -> predicate(level.getBlockState(pos)) }

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
}