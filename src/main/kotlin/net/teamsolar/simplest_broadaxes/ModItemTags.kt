package net.teamsolar.simplest_broadaxes

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ModItemTags {
    val BROADAXES = TagKey.of(RegistryKeys.ITEM, Identifier.of(SimplestBroadaxes.modid, "broadaxe"))
}