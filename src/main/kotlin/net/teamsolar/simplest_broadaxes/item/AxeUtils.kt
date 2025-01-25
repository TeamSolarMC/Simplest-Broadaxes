package net.teamsolar.simplest_broadaxes.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.ItemAbilities
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVector3d
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVector3f
import java.util.*

class AxeUtils {
    companion object {
        public fun playerHasShieldUseIntent(context: UseOnContext): Boolean {
            val player = context.player
            return context.hand == InteractionHand.MAIN_HAND && player!!.offhandItem.`is`(Items.SHIELD) && !player!!.isSecondaryUseActive
        }

        public fun evaluateNewBlockState(
            level: Level,
            pos: BlockPos,
            player: Player?,
            state: BlockState,
            context: UseOnContext
        ): Optional<BlockState> {
            val optional = Optional.ofNullable(state.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false))
            if (optional.isPresent) {
                level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f)
                return optional
            } else {
                val optional1 =
                    Optional.ofNullable(state.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false))
                if (optional1.isPresent) {
                    level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f)
                    level.levelEvent(player, 3005, pos, 0)
                    return optional1
                } else {
                    val optional2 =
                        Optional.ofNullable(state.getToolModifiedState(context, ItemAbilities.AXE_WAX_OFF, false))
                    if (optional2.isPresent) {
                        level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f)
                        level.levelEvent(player, 3004, pos, 0)
                        return optional2
                    } else {
                        return Optional.empty()
                    }
                }
            }
        }

        public fun UseOnContext.offset(offset: Vec3i): UseOnContext {
            val newLocation = clickLocation.add(offset.toVec3())
            val newBlockPos = clickedPos.offset(offset)
            return UseOnContext(level, player, hand, itemInHand,
                BlockHitResult(
                     newLocation, clickedFace, newBlockPos, isInside
                )
            )
        }
    }
}