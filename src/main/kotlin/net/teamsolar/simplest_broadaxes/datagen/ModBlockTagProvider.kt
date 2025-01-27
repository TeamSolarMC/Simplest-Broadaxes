package net.teamsolar.simplest_broadaxes.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider
import net.minecraft.block.Blocks
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.registry.tag.BlockTags
import net.teamsolar.simplest_broadaxes.ModBlockTags
import java.util.concurrent.CompletableFuture

class ModBlockTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<WrapperLookup>
): BlockTagProvider(output, registriesFuture) {
    override fun configure(wrapperLookup: WrapperLookup) {
        val wrapper = wrapperLookup.createRegistryLookup()
        getOrCreateTagBuilder(ModBlockTags.FELLABLE_BLOCK)
            .forceAddTag(BlockTags.LOGS)
            .add(
                Blocks.MUSHROOM_STEM,
                Blocks.CHORUS_FLOWER,
                Blocks.CHORUS_PLANT,
                Blocks.MANGROVE_ROOTS
            )
        getOrCreateTagBuilder(ModBlockTags.SECONDARY_FELLABLE_BLOCK)
            .add(
                Blocks.BROWN_MUSHROOM_BLOCK,
                Blocks.RED_MUSHROOM_BLOCK,
                Blocks.SHROOMLIGHT,
                Blocks.BEE_NEST
            )
            .forceAddTag(BlockTags.LEAVES)
            .forceAddTag(BlockTags.WART_BLOCKS)
    }
}