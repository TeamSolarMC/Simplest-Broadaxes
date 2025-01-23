package net.teamsolar.simplest_broadaxes

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object ModTags {
    object Blocks {
        val NEEDS_WOODEN_TOOL: TagKey<Block> = tag("needs_wooden_tool")
        val NEEDS_STONE_TOOL: TagKey<Block> = tag("needs_stone_tool")
        val NEEDS_IRON_TOOL: TagKey<Block> = tag("needs_iron_tool")
        val NEEDS_GOLDEN_TOOL: TagKey<Block> = tag("needs_golden_tool")
        val NEEDS_DIAMOND_TOOL: TagKey<Block> = tag("needs_diamond_tool")
        val NEEDS_NETHERITE_TOOL: TagKey<Block> = tag("needs_netherite_tool")

        private fun tag(name: String): TagKey<Block> {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, name))
        }

        private fun forgeTag(name: String): TagKey<Block> {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", name))
        }
    }

    object Items {
        val BROADAXES: TagKey<Item> = forgeTag("broadaxes")

        private fun tag(name: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, name))
        }

        private fun forgeTag(name: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", name))
        }
    }
}
