package net.teamsolar.simplest_broadaxes

import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ModTagItems {
    val BROADAXES: TagKey<Item> = forgeTag("broadaxes")

    private fun tag(name: String): TagKey<Item> {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, name))
    }

    private fun forgeTag(name: String): TagKey<Item> {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", name))
    }
}