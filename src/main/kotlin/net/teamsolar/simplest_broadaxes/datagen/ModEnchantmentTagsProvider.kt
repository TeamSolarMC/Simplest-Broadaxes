package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EnchantmentTagsProvider
import net.minecraft.tags.EnchantmentTags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.enchantment.ModEnchantments
import java.util.concurrent.CompletableFuture

class ModEnchantmentTagsProvider(
    packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>, existingFileHelper: ExistingFileHelper
): EnchantmentTagsProvider(packOutput, future, SimplestBroadaxes.MODID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        // SimplestBroadaxes.LOGGER.info("Adding broadaxe enchantment to non-treasure enchantments list")
        tag(EnchantmentTags.NON_TREASURE)
            .add(ModEnchantments.BROADAXE_ENCHANTMENT)
    }
}