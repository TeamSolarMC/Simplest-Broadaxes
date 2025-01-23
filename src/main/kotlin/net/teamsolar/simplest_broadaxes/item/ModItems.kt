package net.teamsolar.simplest_broadaxes.item

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.SmithingTemplateItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import java.util.function.Supplier

object ModItems {
    val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(SimplestBroadaxes.MODID)

    // Hammer durability will be 3x the durability of the vanilla pickaxe of the same tier.
    /*
        Supplier<BroadaxeItem> {
            BroadaxeItem(
                Tiers.WOOD,
                Item.Properties().durability(177).attributes(
                    DiggerItemWithoutDurability.createAttributes(
                        Tiers.WOOD, 6.0f, -3.4f
                    )
                )
            )
        }
     */
    fun broadaxeSupplier(tier: Tier, durability: Int, attackSpeed: Float, attackDamageModifier: Float, additional: ((Item.Properties) -> Item.Properties)? = null) = Supplier<BroadaxeItem> {
        BroadaxeItem(
            tier,
            Item.Properties().durability(durability).attributes(
                DiggerItemWithoutDurability.createAttributes(
                    tier, attackSpeed, attackDamageModifier
                )
            ).let{
                if(additional != null) {
                    additional(it)
                } else {
                    it
                }
            }
        )
    }
    val WOODEN_BROADAXE: DeferredItem<BroadaxeItem> = ITEMS.register<BroadaxeItem>(
        "wooden_broadaxe",
        broadaxeSupplier(Tiers.WOOD, durability =  177, attackSpeed = 6.0f, attackDamageModifier = -3.4f)
    )
    val STONE_BROADAXE: DeferredItem<BroadaxeItem> = ITEMS.register<BroadaxeItem>(
        "stone_broadaxe",
        broadaxeSupplier(Tiers.STONE, durability =  393, attackSpeed = 7.0f, attackDamageModifier = -3.4f)
    )
    val IRON_BROADAXE: DeferredItem<BroadaxeItem> = ITEMS.register<BroadaxeItem>(
        "iron_broadaxe",
        broadaxeSupplier(Tiers.IRON, durability =  750, attackSpeed = 6.0f, attackDamageModifier = -3.3f)
    )
    val GOLDEN_BROADAXE: DeferredItem<BroadaxeItem> = ITEMS.register<BroadaxeItem>(
        "golden_broadaxe",
        broadaxeSupplier(Tiers.GOLD, durability =  96, attackSpeed = 6.0f, attackDamageModifier = -3.2f)
    )
    val DIAMOND_BROADAXE: DeferredItem<BroadaxeItem> = ITEMS.register<BroadaxeItem>(
        "diamond_broadaxe",
        broadaxeSupplier(Tiers.DIAMOND, durability = 4683, attackSpeed = 5.0f, attackDamageModifier = -3.2f)
    )
    val NETHERITE_BROADAXE: DeferredItem<BroadaxeItem> = ITEMS.register<BroadaxeItem>(
        "netherite_broadaxe",
        broadaxeSupplier(Tiers.NETHERITE, durability = 6093, attackSpeed = 5.0f, attackDamageModifier = -3.2f) {
            it.fireResistant()
        }
    )
    val BROADAXE_SMITHING_TEMPLATE: DeferredItem<SmithingTemplateItem> = ITEMS.register<SmithingTemplateItem>(
        "broadaxe_smithing_template",
        Supplier<SmithingTemplateItem> {
            SmithingTemplateItem(
                Component.translatable("item.simplest_broadaxes.broadaxe_smithing_template.applies_to")
                    .withStyle(ChatFormatting.BLUE),  // DESCRIPTION_FORMAT
                Component.translatable("item.simplest_broadaxes.broadaxe_smithing_template.ingredients")
                    .withStyle(ChatFormatting.BLUE),  // DESCRIPTION_FORMAT
                Component.translatable("item.simplest_broadaxes.broadaxe_smithing_template.upgrade_description")
                    .withStyle(ChatFormatting.GRAY),  // TITLE_FORMAT
                Component.translatable("item.simplest_broadaxes.broadaxe_smithing_template.base_slot_description"),  // No formatting
                Component.translatable("item.simplest_broadaxes.broadaxe_smithing_template.additions_slot_description"),  // No formatting
                // Base slot empty icons
                listOf<ResourceLocation>(
                    ResourceLocation.withDefaultNamespace("item/empty_slot_pickaxe"),
                    ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, "item/empty_slot_broadaxe")
                ),
                listOf<ResourceLocation>(
                    ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, "item/empty_slot_block")
                ) // Additional slot empty icons
            )
        }
    )

    fun register(eventBus: IEventBus) {
        ITEMS.register(eventBus)
    }
}