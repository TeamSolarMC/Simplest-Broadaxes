package net.teamsolar.simplest_broadaxes

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object ModTagBlocks {
    val FELLABLE_BLOCK          : TagKey<Block> = BlockTags.create(ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, "can_be_felled"))
    val SECONDARY_FELLABLE_BLOCK: TagKey<Block> = BlockTags.create(ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, "secondary_can_be_felled"))
}