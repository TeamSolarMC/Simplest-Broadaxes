package net.teamsolar.simplest_broadaxes.datagen

import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.conditions.IConditionBuilder
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.item.ModItems
import net.teamsolar.simplest_broadaxes.item.ModItems.BROADAXE_SMITHING_TEMPLATE
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern

class ModRecipeProvider(packOutput: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider?>) :
    RecipeProvider(packOutput, lookupProvider), IConditionBuilder {
    override fun buildRecipes(output: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BROADAXE_SMITHING_TEMPLATE.get(), 2)
            .pattern("ABA")
            .pattern("ACA")
            .pattern("AAA")
            .define('A', Items.EMERALD)
            .define('B', BROADAXE_SMITHING_TEMPLATE.get())
            .define('C', Items.COBBLESTONE)
            .unlockedBy("has_broadaxe_template", hasInInventory(BROADAXE_SMITHING_TEMPLATE))
            .save(output)
        
        broadaxeSmithingRecipe(
            Ingredient.of(Items.WOODEN_AXE),
            Ingredient.of(ItemTags.LOGS),
            ModItems.WOODEN_BROADAXE.get(),
            output
        )
        broadaxeSmithingRecipe(
            Ingredient.of(Items.STONE_AXE),
            Ingredient.of(Items.SMOOTH_STONE),
            ModItems.STONE_BROADAXE.get(),
            output
        )
        broadaxeSmithingRecipe(
            Ingredient.of(Items.IRON_AXE),
            Ingredient.of(Items.IRON_BLOCK.asItem()),
            ModItems.IRON_BROADAXE.get(),
            output
        )
        broadaxeSmithingRecipe(
            Ingredient.of(Items.GOLDEN_AXE),
            Ingredient.of(Items.GOLD_BLOCK),
            ModItems.GOLDEN_BROADAXE.get(),
            output
        )
        broadaxeSmithingRecipe(
            Ingredient.of(Items.DIAMOND_AXE),
            Ingredient.of(Items.DIAMOND_BLOCK),
            ModItems.DIAMOND_BROADAXE.get(),
            output
        )
        broadaxeSmithingRecipe(
            Ingredient.of(Items.NETHERITE_AXE),
            Ingredient.of(Items.DIAMOND_BLOCK),
            ModItems.NETHERITE_BROADAXE.get(),
            output
        )
        // Upgrades
        broadaxeUpgradeRecipe(
            Ingredient.of(ModItems.WOODEN_BROADAXE.get()),
            Ingredient.of(Items.SMOOTH_STONE),
            ModItems.STONE_BROADAXE.get(),
            output
        )
        broadaxeUpgradeRecipe(
            Ingredient.of(ModItems.STONE_BROADAXE.get()),
            Ingredient.of(Items.IRON_BLOCK),
            ModItems.IRON_BROADAXE.get(),
            output
        )
        broadaxeUpgradeRecipe(
            Ingredient.of(ModItems.IRON_BROADAXE.get()),
            Ingredient.of(Items.GOLD_BLOCK),
            ModItems.GOLDEN_BROADAXE.get(),
            output
        )
        broadaxeUpgradeRecipe(
            Ingredient.of(ModItems.GOLDEN_BROADAXE.get()),
            Ingredient.of(Items.DIAMOND_BLOCK),
            ModItems.DIAMOND_BROADAXE.get(),
            output
        )
        netheriteSmithing(output, ModItems.DIAMOND_BROADAXE.get(), RecipeCategory.MISC, ModItems.NETHERITE_BROADAXE.get())

        basicBlastingAndSmeltingRecipe(ModItems.IRON_BROADAXE.get(), Items.IRON_NUGGET, output)
        basicBlastingAndSmeltingRecipe(ModItems.GOLDEN_BROADAXE.get(), Items.GOLD_NUGGET, output)
    }

    private fun hasInInventory(item: ItemLike): Criterion<InventoryChangeTrigger.TriggerInstance> {
        return inventoryTrigger(
            ItemPredicate.Builder.item()
                .of(item).build()
        )
    }

    private fun stripNamespace(itemString: String): String {
        val pattern = Pattern.compile("(.+):(.+)")
        val matches = pattern.matcher(itemString)
        if (matches.find()) {
            return matches.group(2)
        }
        return ""
    }

    private fun broadaxeSmithingRecipe(base: Ingredient, additional: Ingredient, outputItem: Item, output: RecipeOutput) {
        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ModItems.BROADAXE_SMITHING_TEMPLATE.get()),  // ModItems.BROADAXE_SMITHING_TEMPLATE.get(),
            base,
            additional,
            RecipeCategory.TOOLS,
            outputItem
        )
            .unlocks("has_broadaxe_template", hasInInventory(ModItems.BROADAXE_SMITHING_TEMPLATE.get()))
            .save(
                output,
                ResourceLocation.fromNamespaceAndPath(
                    SimplestBroadaxes.MODID,
                    stripNamespace(outputItem.toString()) + "_from_pickaxe"
                )
            )
    }

    private fun broadaxeUpgradeRecipe(base: Ingredient, additional: Ingredient, outputItem: Item, output: RecipeOutput) {
        SmithingTransformRecipeBuilder.smithing(
            Ingredient.of(ModItems.BROADAXE_SMITHING_TEMPLATE.get()),  // ModItems.BROADAXE_SMITHING_TEMPLATE.get(),
            base,
            additional,
            RecipeCategory.TOOLS,
            outputItem
        )
            .unlocks("has_broadaxe_template", hasInInventory(ModItems.BROADAXE_SMITHING_TEMPLATE.get()))
            .save(
                output,
                ResourceLocation.fromNamespaceAndPath(
                    SimplestBroadaxes.MODID,
                    stripNamespace(outputItem.toString()) + "_from_upgrade"
                )
            )
    }

    private fun basicBlastingAndSmeltingRecipe(input: Item, outputItem: Item, output: RecipeOutput) {
        val unqualifiedItemName = stripNamespace(input.toString())
        SimpleCookingRecipeBuilder.blasting(
            Ingredient.of(input),
            RecipeCategory.MISC,
            outputItem,
            0.1f,
            100
        )
            .unlockedBy("has_$unqualifiedItemName", hasInInventory(input))
            .save(output, ResourceLocation.withDefaultNamespace(unqualifiedItemName + "_blasting"))
        SimpleCookingRecipeBuilder.smelting(
            Ingredient.of(input),
            RecipeCategory.MISC,
            outputItem,
            0.1f,
            200
        )
            .unlockedBy("has_$unqualifiedItemName", hasInInventory(input))
            .save(output, ResourceLocation.withDefaultNamespace(unqualifiedItemName + "_smelting"))
    }
}
