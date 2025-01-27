package net.teamsolar.simplest_broadaxes

import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

object ModBlockTags {
    val FELLABLE_BLOCK = TagKey.of(RegistryKeys.BLOCK, Identifier.of(SimplestBroadaxes.modid, "can_be_felled"))
    val SECONDARY_FELLABLE_BLOCK = TagKey.of(RegistryKeys.BLOCK, Identifier.of(SimplestBroadaxes.modid, "secondary_can_be_felled"))
}