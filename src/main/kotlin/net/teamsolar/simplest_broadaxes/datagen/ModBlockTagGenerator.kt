package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.ModTagBlocks.FELLABLE_BLOCK
import net.teamsolar.simplest_broadaxes.ModTagBlocks.SECONDARY_FELLABLE_BLOCK
import java.util.concurrent.CompletableFuture

class ModBlockTagGenerator(
    output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) :
    BlockTagsProvider(output, lookupProvider, SimplestBroadaxes.MODID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(FELLABLE_BLOCK)
            .addTag(BlockTags.LOGS)
            .add(
                Blocks.MUSHROOM_STEM,
                Blocks.CHORUS_FLOWER,
                Blocks.CHORUS_PLANT,
                Blocks.MANGROVE_ROOTS
            )
        tag(SECONDARY_FELLABLE_BLOCK)
            .add(
                Blocks.BROWN_MUSHROOM_BLOCK,
                Blocks.RED_MUSHROOM_BLOCK,
                Blocks.SHROOMLIGHT,
                Blocks.BEE_NEST
            )
            .addTag(BlockTags.LEAVES)
            .addTag(BlockTags.WART_BLOCKS)
        // ?
    }

    override fun getName(): String {
        return "Block Tags"
    }
}
