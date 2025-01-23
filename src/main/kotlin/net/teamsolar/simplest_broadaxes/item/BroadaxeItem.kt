package net.teamsolar.simplest_broadaxes.item

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.teamsolar.simplest_broadaxes.Config
import net.teamsolar.simplest_broadaxes.ModBlockTags
import net.teamsolar.simplest_broadaxes.event.ModLevelTickEvent
import net.teamsolar.simplest_broadaxes.event.TreeFellTask


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
                        tickEvent.addTask(level, TreeFellTask(level, miningEntity, pos, secondaryMineableBlocks = ModBlockTags.SECONDARY_FELLABLE_BLOCK))
                    }
                }
            }
        }
    }

    override fun getDestroySpeed(stack: ItemStack, state: BlockState): Float {
        return super.getDestroySpeed(stack, state) * Config.broadaxeMiningSpeedModifier.toFloat()
    }

    fun getMineableBlocks(stack: ItemStack): TagKey<Block> {
        return mineableBlocks
    }

    companion object {
        val tickEvent = ModLevelTickEvent()
        val mineableBlocks: TagKey<Block> = ModBlockTags.FELLABLE_BLOCK
    }
}