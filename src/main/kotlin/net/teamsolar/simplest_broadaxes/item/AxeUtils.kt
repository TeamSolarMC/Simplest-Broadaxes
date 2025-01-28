package net.teamsolar.simplest_broadaxes.item

import net.fabricmc.fabric.mixin.content.registry.AxeItemAccessor
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.PillarBlock
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import java.util.*

class AxeUtils {
    companion object {
        public fun shouldCancelStripAttempt(context: ItemUsageContext): Boolean {
            val playerEntity = context.player
            return context.hand == Hand.MAIN_HAND && playerEntity!!.offHandStack.isOf(Items.SHIELD) && !playerEntity.shouldCancelInteraction()
        }
        public fun getStrippedState(state: BlockState): Optional<BlockState> {
            val STRIPPABLES = AxeItemAccessor.getStrippedBlocks()
            return Optional.ofNullable(STRIPPABLES[state.block]).map { block: Block ->
                block.defaultState.with(
                    PillarBlock.AXIS,
                    state.get(PillarBlock.AXIS) as Direction.Axis
                ) as BlockState
            }
        }

        public fun ItemUsageContext.offset(offset: Vec3i): ItemUsageContext {
            val newLocation = hitPos.add(Vec3d.of(offset))
            val newBlockPos = blockPos.add(offset)
            return ItemUsageContext(world, player, hand, stack,
                BlockHitResult(
                    newLocation, side, newBlockPos, hitsInsideBlock()
                )
            )
        }
    }
}