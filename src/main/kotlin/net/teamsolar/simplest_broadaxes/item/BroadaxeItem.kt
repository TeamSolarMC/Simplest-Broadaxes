package net.teamsolar.simplest_broadaxes.item

import net.minecraft.advancement.criterion.Criteria
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Oxidizable
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import net.teamsolar.simplest_broadaxes.ModBlockTags
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import net.teamsolar.simplest_broadaxes.event.ModLevelTickEvent
import net.teamsolar.simplest_broadaxes.event.task.TreeFellAndTrimTask
import net.teamsolar.simplest_broadaxes.event.task.TreeFellTask
import net.teamsolar.simplest_broadaxes.item.AxeUtils.Companion.offset
import java.util.*


open class BroadaxeItem
    : MiningToolItemWithoutDurability {
    constructor(toolMaterial: ToolMaterial, attackDamage: Float, attackSpeed: Float, properties: Item.Settings) : super(toolMaterial, BlockTags.AXE_MINEABLE, properties.attributeModifiers(
        createAttributeModifiers(toolMaterial, attackDamage, attackSpeed)
    ))

    override fun postMine(
        stack: ItemStack, world: World, state: BlockState, pos: BlockPos, miningEntity: LivingEntity
    ): Boolean {
        return super.postMine(stack, world, state, pos, miningEntity) . also {
            if(!world.isClient) {
                if(miningEntity is ServerPlayerEntity && state.isIn(mineableBlocks) && !miningEntity.isSneaking) {
                    if(tickEvent.queuedBroadaxeTasks[world]?.let{
                            pos in it.blocksBeingMined
                        } != true) {
                        val trimmingLevel = getTrimmingLevel(stack)
                        if(trimmingLevel > 0) {
                            tickEvent.addTask(world, TreeFellAndTrimTask(world, miningEntity, pos))
                        } else {
                            tickEvent.addTask(world, TreeFellTask(world, miningEntity, pos))
                        }
                    }
                }
            }
        }
    }

    fun useOnBlockWithoutRecursion(context: ItemUsageContext, originalItem: Item) {
        val world = context.world
        val blockPos = context.blockPos
        val playerEntity = context.player
        if(world.isAir(blockPos)) {
            return
        }
        val itemStack = context.stack
        if(itemStack.item != originalItem) {
            return
        }
        val blockState = world.getBlockState(blockPos)
        val strippedState: Optional<BlockState> = AxeUtils.getStrippedState(blockState)
        val scrapedState = Oxidizable.getDecreasedOxidationState(blockState)
        val waxRemovedState =
            Optional.ofNullable((HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get())[blockState.block])
                .map { block: Block ->
                    block.getStateWithProperties(blockState)
                }
        var optional4 = Optional.empty<BlockState>()
        if (strippedState.isPresent) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f)
            optional4 = strippedState
        } else if (scrapedState.isPresent) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0f, 1.0f)
            world.syncWorldEvent(playerEntity, 3005, blockPos, 0)
            optional4 = scrapedState
        } else if (waxRemovedState.isPresent) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f)
            world.syncWorldEvent(playerEntity, 3004, blockPos, 0)
            optional4 = waxRemovedState
        }
        if (optional4.isPresent) {
            if (playerEntity is ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger(playerEntity, blockPos, itemStack)
            }

            world.setBlockState(blockPos, optional4.get(), 11)
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, optional4.get()))
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, EquipmentSlot.MAINHAND)
            }
        }
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val blockPos = context.blockPos
        val playerEntity = context.player
        if(AxeUtils.shouldCancelStripAttempt(context)) {
            return ActionResult.PASS
        }
        val blockState = world.getBlockState(blockPos)
        val strippedState: Optional<BlockState> = AxeUtils.getStrippedState(blockState)
        val scrapedState = Oxidizable.getDecreasedOxidationState(blockState)
        val waxRemovedState =
            Optional.ofNullable((HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get())[blockState.block])
                .map { block: Block ->
                    block.getStateWithProperties(blockState)
                }
        val itemStack = context.stack
        var optional4 = Optional.empty<BlockState>()
        if (strippedState.isPresent) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0f, 1.0f)
            optional4 = strippedState
        } else if (scrapedState.isPresent) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0f, 1.0f)
            world.syncWorldEvent(playerEntity, 3005, blockPos, 0)
            optional4 = scrapedState
        } else if (waxRemovedState.isPresent) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0f, 1.0f)
            world.syncWorldEvent(playerEntity, 3004, blockPos, 0)
            optional4 = waxRemovedState
        }

        if (optional4.isPresent) {
            if (playerEntity is ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger(playerEntity, blockPos, itemStack)
            }

            world.setBlockState(blockPos, optional4.get(), 11)
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, optional4.get()))
            if (playerEntity != null) {
                itemStack.damage(1, playerEntity, EquipmentSlot.MAINHAND)
                if(!playerEntity.isSneaking) {
                    for(newBlockPos in getSurroundingBlocks(blockPos, context.side)) {
                        useOnBlockWithoutRecursion(
                            context.offset(newBlockPos.subtract(blockPos)),
                            itemStack.item
                        )
                    }
                }
            }

            return ActionResult.success(world.isClient)
        } else {
            return ActionResult.PASS
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
        }.vector
        val yVector = when(facing) {
            Direction.DOWN -> Direction.EAST
            Direction.UP -> Direction.WEST
            Direction.NORTH -> Direction.EAST
            Direction.SOUTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
            Direction.EAST -> Direction.SOUTH
        }.vector
        for(x in -1 .. 1) {
            for(y in -1 .. 1){
                val nextPos = pos.add(xVector.multiply(x).add(yVector.multiply(y)))
                if(nextPos != pos) {
                    list.add(nextPos)
                }
            }
        }
        return list
    }

    fun getTrimmingLevel(itemStack: ItemStack): Int {
        var myInt = 0
        for((enchantment, level) in EnchantmentHelper.getEnchantments(itemStack).enchantmentEntries) {
            val key = enchantment.key
            if(key.isPresent) {
                if(key.get() == ModEnchantments.TRIMMING) {
                    myInt += level
                }
            }
        }
        return myInt
    }

    val miningSpeedModifier = 0.4f

    override fun getMiningSpeed(stack: ItemStack, state: BlockState): Float {
        return super.getMiningSpeed(stack, state) * miningSpeedModifier
    }

    /*override fun getMiningSpeedMultiplier(stack: ItemStack, state: BlockState): Float {
        return super.getMiningSpeedMultiplier(stack, state).also {
            SimplestBroadaxes.logger.info("Base destroy speed: $it")
        } * miningSpeedModifier
    }*/

    // val effectiveBlocks = BlockTags.AXE_MINEABLE
    val mineableBlocks: TagKey<Block> = ModBlockTags.FELLABLE_BLOCK
    val secondaryMineableBlocks: TagKey<Block> = ModBlockTags.SECONDARY_FELLABLE_BLOCK

    companion object {
        val tickEvent = ModLevelTickEvent()
    }
}