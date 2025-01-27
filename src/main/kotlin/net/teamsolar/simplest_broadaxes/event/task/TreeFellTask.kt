package net.teamsolar.simplest_broadaxes.event.task

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.teamsolar.simplest_broadaxes.Config
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.event.ModLevelTickEvent
import net.teamsolar.simplest_broadaxes.item.BroadaxeItem

open class TreeFellTask(level: World, player: ServerPlayerEntity, position: BlockPos): ModLevelTickEvent.BroadaxeTask(level, player, position) {
    // 1. Collect up to 1000 adjacent blocks
    // and ignore any non-log blocks
    // 2. Don't cut down any additional blocks if it is not a root
    // 3. Cut 64 blocks per tick from this list
    val blocksPerTick = Config.broadaxeBlocksPerTick
    val deliversBlocks = Config.dropsFelledBlocks

    protected val blocksToMine: MutableList<BlockPos> = this.collectBlocksToMine()

    protected open fun collectBlocksToMine() : MutableList<BlockPos> =
        getEquippedBroadaxe()?.run {
            object : TaskBlockCollector {
                // In the context of broadaxes, primaryTags is logs but not leaves or other blocks.
                override val primaryTags: TagKey<Block> = item.mineableBlocks
                override val level: World = this@TreeFellTask.level
                override val player: ServerPlayerEntity = this@TreeFellTask.player
                override val maxAdjacentBlocks: Int = Config.broadaxeBlocksPerSwing
            }.getBlocksToMine(position)
        } ?: mutableListOf()

    // Get the broadaxe from the player listed in the task if equipped;
    // otherwise, return null
    // (This can change each tick, or even in a single tick if durability was used since the last time this was called)
    protected class BroadaxeCtx(val item: BroadaxeItem, val itemStack: ItemStack)
    fun isBroadaxeEquipped() = getEquippedBroadaxe() != null
    protected fun getEquippedBroadaxe(): BroadaxeCtx? {
        val itemStack = player.mainHandStack
        val item = itemStack.item
        if(item is BroadaxeItem) {
            return BroadaxeCtx(item, itemStack)
        }
        return null
    }

    open fun canBreakBlock(pos: BlockPos): Boolean {
        val state = level.getBlockState(pos)
        // If the BroadaxeCtx is null, do not apply (i.e. do not run this block)
        getEquippedBroadaxe()?.apply {
            val mineableBlocks = item.mineableBlocks
            return state.isIn(mineableBlocks)
        }
        return false
    }
    fun popFirstBreakableBlock(): BlockPos? {
        while(blocksToMine.isNotEmpty()) {
            val pos = blocksToMine.removeLast()
            if(canBreakBlock(pos)) {
                return pos
            }
        }
        return null
    }
    open fun damageBroadaxeIfEquipped(blockState: BlockState) {
        val itemStack = player.mainHandStack
        itemStack.damage(1, player, EquipmentSlot.MAINHAND)
    }
    protected fun transferItemStacks(stackFrom: ItemStack, stackTo: ItemStack) {
        // Refer to: Shift+click behavior from AbstractContainerMenu (moveItemStackTo)
        // and merge behavior from ItemEntity (merge)
        val j: Int = stackTo.count + stackFrom.count
        // Consider the drops as being stackable up to their max amount
        // (because they are dropped as item entities)
        val k = stackTo.maxCount
        // val k: Int = getMaxStackSize(stackTo)
        if (j <= k) {
            stackFrom.count = 0
            stackTo.count = j
        } else if (stackTo.count < k) {
            stackFrom.decrement(k - stackTo.count)
            stackTo.count = k
        }
    }
    var taskProgress = 0
    override fun progress() {
        SimplestBroadaxes.logger.info("Broadaxe Task ($position) progress: $taskProgress (${blocksToMine.size} remaining blocks)")
        taskProgress = 0
        val listOfItemsToMove = mutableListOf<ItemStack>()
        while(taskProgress < blocksPerTick && blocksToMine.isNotEmpty()) {
            val pos = popFirstBreakableBlock()
            if(pos != null) {
                val blockState = level.getBlockState(pos)
                if(!deliversBlocks) {
                    level.breakBlock(pos, true, player)
                } else {
                    val itemsFromBlock = blockState.getDroppedStacks(
                        LootContextParameterSet.Builder(level as ServerWorld)
                            .add(LootContextParameters.TOOL, player.mainHandStack)
                            .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.position))
                            .addOptional(LootContextParameters.THIS_ENTITY, player)
                            .addOptional(LootContextParameters.BLOCK_ENTITY, level.getBlockEntity(pos))
                    )
                    for(item in itemsFromBlock) {
                        for(presentItemStack in listOfItemsToMove) {
                            if(!presentItemStack.isEmpty && ItemStack.areEqual(item, presentItemStack)) {
                                transferItemStacks(item, presentItemStack)
                            }
                        }
                    }
                    for(item in itemsFromBlock) {
                        if(!item.isEmpty) {
                            listOfItemsToMove.add(item)
                        }
                    }
                    level.breakBlock(pos, false, player)
                }
                damageBroadaxeIfEquipped(blockState)
            }
            taskProgress++
        }
        for(drop in listOfItemsToMove) {
            Block.dropStack(level, position, drop)
        }
    }

    override fun isFinished(): Boolean {
        return blocksToMine.isEmpty() || !isBroadaxeEquipped() || player.world != level
    }
}