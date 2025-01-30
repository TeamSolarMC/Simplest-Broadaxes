package net.teamsolar.simplest_broadaxes.item

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.*

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.item.MiningToolItemWithoutDurability.createAttributeModifiers

object ModItems {
    fun broadaxeSupplier(tier: ToolMaterial, durability: Int, attackDamageModifier: Float, attackSpeed: Float, additional: ((Item.Settings) -> Item.Settings)? = null): BroadaxeItem {
        return BroadaxeItem(
            tier,
            attackDamageModifier,
            attackSpeed,
            Item.Settings().maxDamage(durability)
                .attributeModifiers(
                    createAttributeModifiers(tier, attackDamageModifier, attackSpeed)
                    .with(
                        EntityAttributes.PLAYER_MINING_EFFICIENCY,
                        EntityAttributeModifier(
                            Identifier.of(SimplestBroadaxes.modid, "tool.broadaxe.efficiency"),
                            (BroadaxeItem.efficiencyStatModifier - 1.0f).toDouble(),
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        ),
                        AttributeModifierSlot.MAINHAND
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
    /*val broadaxeItem = BroadaxeItem(
        ToolMaterials.DIAMOND,
        attackDamage = 6.0f,
        attackSpeed = -3.2f,
        Item.Settings().maxDamage(4683)
    )
    .also {
        Registry.register(
            Registries.ITEM,
            Identifier.of(SimplestBroadaxes.modid, "diamond_broadaxe"),
            it
        )
    }*/
    val WOODEN_BROADAXE = Registry.register(Registries.ITEM,
        Identifier.of(SimplestBroadaxes.modid, "wooden_broadaxe"),
        broadaxeSupplier(ToolMaterials.WOOD, durability = 177, attackDamageModifier = 7.0f, attackSpeed = -3.4f)
    )
    val STONE_BROADAXE = Registry.register(Registries.ITEM,
        Identifier.of(SimplestBroadaxes.modid, "stone_broadaxe"),
        broadaxeSupplier(ToolMaterials.STONE, durability = 393, attackDamageModifier = 8.0f, attackSpeed = -3.4f)
    )
    val IRON_BROADAXE = Registry.register(Registries.ITEM,
        Identifier.of(SimplestBroadaxes.modid, "iron_broadaxe"),
        broadaxeSupplier(ToolMaterials.IRON, durability = 750, attackDamageModifier = 7.0f, attackSpeed = -3.3f)
    )
    val GOLDEN_BROADAXE = Registry.register(Registries.ITEM,
        Identifier.of(SimplestBroadaxes.modid, "golden_broadaxe"),
        broadaxeSupplier(ToolMaterials.GOLD, durability = 96, attackDamageModifier = 7.0f, attackSpeed = -3.2f)
    )
    val DIAMOND_BROADAXE = Registry.register(Registries.ITEM,
        Identifier.of(SimplestBroadaxes.modid, "diamond_broadaxe"),
        broadaxeSupplier(ToolMaterials.DIAMOND, durability = 4683, attackDamageModifier = 6.0f, attackSpeed = -3.2f)
    )
    val NETHERITE_BROADAXE = Registry.register(Registries.ITEM,
        Identifier.of(SimplestBroadaxes.modid, "netherite_broadaxe"),
        broadaxeSupplier(ToolMaterials.NETHERITE, durability = 6093, attackDamageModifier = 6.0f, attackSpeed = -3.2f) {
            it.fireproof()
        }
    )

    val broadaxes = listOf(
        WOODEN_BROADAXE,
        STONE_BROADAXE,
        IRON_BROADAXE,
        GOLDEN_BROADAXE,
        DIAMOND_BROADAXE,
        NETHERITE_BROADAXE
    )

    val BROADAXE_SMITHING_TEMPLATE = Registry.register(Registries.ITEM,
        Identifier.of(SimplestBroadaxes.modid, "broadaxe_smithing_template"),
        SmithingTemplateItem(
            Text.translatable("item.simplest_broadaxes.broadaxe_smithing_template.applies_to")
                .formatted(Formatting.BLUE),  // DESCRIPTION_FORMAT
            Text.translatable("item.simplest_broadaxes.broadaxe_smithing_template.ingredients")
                .formatted(Formatting.BLUE),  // DESCRIPTION_FORMAT
            Text.translatable("item.simplest_broadaxes.broadaxe_smithing_template.upgrade_description")
                .formatted(Formatting.GRAY),  // TITLE_FORMAT
            Text.translatable("item.simplest_broadaxes.broadaxe_smithing_template.base_slot_description"),  // No formatting
            Text.translatable("item.simplest_broadaxes.broadaxe_smithing_template.additions_slot_description"),  // No formatting
            // Base slot empty icons
            listOf(
                Identifier.of("minecraft", "item/empty_slot_axe"),
                Identifier.of(SimplestBroadaxes.modid, "item/empty_slot_broadaxe")
            ),
            listOf(
                Identifier.of(SimplestBroadaxes.modid, "item/empty_slot_block")
            ).toList() // Additional slot empty icons
        ))
    inline fun RegistryKey<ItemGroup>.modify(crossinline callback: FabricItemGroupEntries.() -> Unit) {
        val group = RegistryKey.of(Registries.ITEM_GROUP.key, this.value)
        ItemGroupEvents.modifyEntriesEvent(group).register{
            itemGroupEntries -> itemGroupEntries.callback()
        }
    }
    fun initialize() {
        /*val toolsGroup = RegistryKey.of(Registries.ITEM_GROUP.key, ItemGroups.TOOLS.value)
        ItemGroupEvents.modifyEntriesEvent(toolsGroup).register{
            itemGroupEntries ->
            for(item in broadaxes) {
                itemGroupEntries.add(item)
            }
        }
        val combatGroup = RegistryKey.of(Registries.ITEM_GROUP.key, ItemGroups.COMBAT.value)
        ItemGroupEvents.modifyEntriesEvent(combatGroup).register{
            itemGroupEntries ->
            for(item in broadaxes) {
                itemGroupEntries.add(item)
            }
        }*/
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register{
            for(item in broadaxes) {
                it.add(item)
            }
        }
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register{
            for(item in broadaxes) {
                it.add(item)
            }
        }
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register{
            it.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, BROADAXE_SMITHING_TEMPLATE)
        }

        SimplestBroadaxes.logger.info("Items registered")
    }
}