package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.ModTags
import net.teamsolar.simplest_broadaxes.item.ModItems
import java.util.concurrent.CompletableFuture
import javax.annotation.ParametersAreNonnullByDefault

class ModItemTagGenerator(
    packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider?>,
    completableFuture: CompletableFuture<TagLookup<Block?>?>, existingFileHelper: ExistingFileHelper?
) :
    ItemTagsProvider(packOutput, future, completableFuture, SimplestBroadaxes.MODID, existingFileHelper) {
    @ParametersAreNonnullByDefault
    override fun addTags(provider: HolderLookup.Provider) {
        tag(ModTags.Items.BROADAXES)
            .add(
                ModItems.WOODEN_BROADAXE.get(),
                ModItems.STONE_BROADAXE.get(),
                ModItems.IRON_BROADAXE.get(),
                ModItems.GOLDEN_BROADAXE.get(),
                ModItems.DIAMOND_BROADAXE.get(),
                ModItems.NETHERITE_BROADAXE.get()
            )
        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(ModTags.Items.BROADAXES)
        tag(ItemTags.MINING_ENCHANTABLE).addTag(ModTags.Items.BROADAXES)
        tag(ItemTags.MINING_LOOT_ENCHANTABLE).addTag(ModTags.Items.BROADAXES)
    }

    override fun getName(): String {
        return "Item Tags"
    }
}
