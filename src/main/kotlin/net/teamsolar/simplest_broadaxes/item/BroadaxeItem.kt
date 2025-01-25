package net.teamsolar.simplest_broadaxes.item

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility
import net.teamsolar.simplest_broadaxes.ModTagBlocks
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import net.teamsolar.simplest_broadaxes.event.ModLevelTickEvent
import net.teamsolar.simplest_broadaxes.event.task.TreeFellAndTrimTask
import net.teamsolar.simplest_broadaxes.event.task.TreeFellTask
import net.teamsolar.simplest_broadaxes.item.AxeUtils.Companion.evaluateNewBlockState
import net.teamsolar.simplest_broadaxes.item.AxeUtils.Companion.offset
import net.teamsolar.simplest_broadaxes.item.AxeUtils.Companion.playerHasShieldUseIntent
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.times
import java.util.*

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

    // Used for implementation of axe stripping, waxing off, and scraping
    fun useOnWithoutRecursion(context: UseOnContext, originalItem: Item) {
        // Does not return an interaction result, but passes the UseOnContext to requisite blocks when they are modified
        // Does nothing on air
        val level = context.level
        val blockpos = context.clickedPos
        val player = context.player
        if(level.isEmptyBlock(blockpos)) {
            return
        }
        val itemstack = context.itemInHand
        if(itemstack.item != originalItem) {
            return
        }
        val resultingBlockState: Optional<BlockState> =
            evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), context)
        if (resultingBlockState.isPresent) {
            if (player is ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, blockpos, itemstack)
            }

            level.setBlock(blockpos, resultingBlockState.get(), 11)
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, resultingBlockState.get()))
            if (player != null) {
                itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.hand))
            }
        }
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val blockpos = context.clickedPos
        val player = context.player
        if (playerHasShieldUseIntent(context)) {
            return InteractionResult.PASS
        } else {
            val resultingBlockState: Optional<BlockState> =
                evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), context)
            if (resultingBlockState.isEmpty) {
                return InteractionResult.PASS
            } else {
                val itemstack = context.itemInHand
                if (player is ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, blockpos, itemstack)
                }

                level.setBlock(blockpos, resultingBlockState.get(), 11)
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, resultingBlockState.get()))
                if (player != null) {
                    itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.hand))
                    if(!player.isShiftKeyDown) {
                        // also strip blocks in a 3x3 grid - provided that the player doesn't have their shift key down
                        for(newBlockPos in getSurroundingBlocks(context)) {
                            useOnWithoutRecursion(
                                context.offset(newBlockPos.subtract(blockpos)),
                                itemstack.item
                            )
                        }
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide)
            }
        }
    }


    fun getSurroundingBlocks(pos: BlockPos, facing: Direction): List<BlockPos> {
        val list = mutableListOf<BlockPos>()
        val xVector = when(facing) {
            Direction.DOWN -> Direction.NORTH
            Direction.UP -> Direction.SOUTH
            Direction.NORTH -> Direction.UP
            Direction.SOUTH -> Direction.UP
            Direction.WEST -> Direction.UP
            Direction.EAST -> Direction.UP
        }.normal
        val yVector = when(facing) {
            Direction.DOWN -> Direction.EAST
            Direction.UP -> Direction.WEST
            Direction.NORTH -> Direction.EAST
            Direction.SOUTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
            Direction.EAST -> Direction.SOUTH
        }.normal
        for(x in -1 .. 1) {
            for(y in -1 .. 1){
                val nextPos = pos.offset(xVector.times(x).offset(yVector.times(y)))
                if(nextPos != pos) {
                    list.add(nextPos)
                }
            }
        }
        return list
    }

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
    }

    fun getSurroundingBlocks(context: UseOnContext): List<BlockPos> {
        return getSurroundingBlocks(context.clickedPos, context.clickedFace)
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