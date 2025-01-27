package net.teamsolar.simplest_broadaxes.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.*
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.item.ModItems
import java.util.concurrent.CompletableFuture

class ModRecipeGenerator(generator: FabricDataOutput,
                         registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
): FabricRecipeProvider(generator, registriesFuture) {
    override fun generate(exporter: RecipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BROADAXE_SMITHING_TEMPLATE)
            .pattern("ABA")
            .pattern("ACA")
            .pattern("AAA")
            .input('A', Items.EMERALD)
            .input('B', ModItems.BROADAXE_SMITHING_TEMPLATE)
            .input('C', Items.COBBLESTONE)
            .criterion(hasItem(ModItems.BROADAXE_SMITHING_TEMPLATE), RecipeProvider.conditionsFromItem(ModItems.BROADAXE_SMITHING_TEMPLATE))
            .offerTo(exporter)

        broadaxeSmithingRecipe(
            Ingredient.ofItems(Items.WOODEN_AXE),
            Ingredient.fromTag(ItemTags.LOGS),
            ModItems.WOODEN_BROADAXE,
            exporter
        )
        broadaxeSmithingRecipe(
            Ingredient.ofItems(Items.STONE_AXE),
            Ingredient.ofItems(Items.SMOOTH_STONE),
            ModItems.STONE_BROADAXE,
            exporter
        )
        broadaxeSmithingRecipe(
            Ingredient.ofItems(Items.IRON_AXE),
            Ingredient.ofItems(Items.IRON_BLOCK.asItem()),
            ModItems.IRON_BROADAXE,
            exporter
        )
        broadaxeSmithingRecipe(
            Ingredient.ofItems(Items.GOLDEN_AXE),
            Ingredient.ofItems(Items.GOLD_BLOCK),
            ModItems.GOLDEN_BROADAXE,
            exporter
        )
        broadaxeSmithingRecipe(
            Ingredient.ofItems(Items.DIAMOND_AXE),
            Ingredient.ofItems(Items.DIAMOND_BLOCK),
            ModItems.DIAMOND_BROADAXE,
            exporter
        )
        broadaxeSmithingRecipe(
            Ingredient.ofItems(Items.NETHERITE_AXE),
            Ingredient.ofItems(Items.DIAMOND_BLOCK),
            ModItems.NETHERITE_BROADAXE,
            exporter
        )
        // Upgrades
        broadaxeUpgradeRecipe(
            Ingredient.ofItems(ModItems.WOODEN_BROADAXE),
            Ingredient.ofItems(Items.SMOOTH_STONE),
            ModItems.STONE_BROADAXE,
            exporter
        )
        broadaxeUpgradeRecipe(
            Ingredient.ofItems(ModItems.STONE_BROADAXE),
            Ingredient.ofItems(Items.IRON_BLOCK),
            ModItems.IRON_BROADAXE,
            exporter
        )
        broadaxeUpgradeRecipe(
            Ingredient.ofItems(ModItems.IRON_BROADAXE),
            Ingredient.ofItems(Items.GOLD_BLOCK),
            ModItems.GOLDEN_BROADAXE,
            exporter
        )
        broadaxeUpgradeRecipe(
            Ingredient.ofItems(ModItems.GOLDEN_BROADAXE),
            Ingredient.ofItems(Items.DIAMOND_BLOCK),
            ModItems.DIAMOND_BROADAXE,
            exporter
        )

        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_BROADAXE, RecipeCategory.TOOLS, ModItems.NETHERITE_BROADAXE)

        basicBlastingAndSmeltingRecipe(ModItems.IRON_BROADAXE, Items.IRON_NUGGET, exporter)
        basicBlastingAndSmeltingRecipe(ModItems.GOLDEN_BROADAXE, Items.GOLD_NUGGET, exporter)
    }

    private fun getItemName(item: Item): String {
        return Registries.ITEM.getKey(item).get().value.path
    }

    private fun broadaxeSmithingRecipe(base: Ingredient, additional: Ingredient, outputItem: Item, output: RecipeExporter) {
        SmithingTransformRecipeJsonBuilder.create(
            Ingredient.ofItems(ModItems.BROADAXE_SMITHING_TEMPLATE),
            base,
            additional,
            RecipeCategory.TOOLS,
            outputItem
        )
            .criterion(hasItem(ModItems.BROADAXE_SMITHING_TEMPLATE), RecipeProvider.conditionsFromItem(ModItems.BROADAXE_SMITHING_TEMPLATE))
            .offerTo(
                output,
                Identifier.of(
                    SimplestBroadaxes.modid,
                    getItemName(outputItem) + "_from_axe"
                )
            )
    }

    private fun broadaxeUpgradeRecipe(base: Ingredient, additional: Ingredient, outputItem: Item, exporter: RecipeExporter) {
        SmithingTransformRecipeJsonBuilder.create(
            Ingredient.ofItems(ModItems.BROADAXE_SMITHING_TEMPLATE),
            base,
            additional,
            RecipeCategory.TOOLS,
            outputItem
        )
            .criterion(hasItem(ModItems.BROADAXE_SMITHING_TEMPLATE), RecipeProvider.conditionsFromItem(ModItems.BROADAXE_SMITHING_TEMPLATE))
            .offerTo(
                exporter,
                Identifier.of(SimplestBroadaxes.modid, getItemName(outputItem) + "from_upgrade")
            )
    }

    private fun basicBlastingAndSmeltingRecipe(input: Item, outputItem: Item, exporter: RecipeExporter) {
        val unqualifiedItemName = getItemName(input)
        CookingRecipeJsonBuilder.createBlasting(
            Ingredient.ofItems(input),
            RecipeCategory.MISC,
            outputItem,
            0.1f,
            100
        )
            .criterion(hasItem(input), RecipeProvider.conditionsFromItem(input))
            .offerTo(exporter, Identifier.of(SimplestBroadaxes.modid, unqualifiedItemName + "_blasting"))
        CookingRecipeJsonBuilder.createSmelting(
            Ingredient.ofItems(input),
            RecipeCategory.MISC,
            outputItem,
            0.1f,
            200
        )
            .criterion(hasItem(input), RecipeProvider.conditionsFromItem(input))
            .offerTo(exporter, Identifier.of(SimplestBroadaxes.modid, unqualifiedItemName + "_smelting"))
    }

}