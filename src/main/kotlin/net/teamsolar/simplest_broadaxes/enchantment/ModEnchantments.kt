package net.teamsolar.simplest_broadaxes.enchantment

import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment
import net.teamsolar.simplest_broadaxes.ModTagItems
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes

object ModEnchantments {
    val BROADAXE_ENCHANTMENT: ResourceKey<Enchantment> = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(SimplestBroadaxes.MODID, "trimming")
    )

    public fun bootstrap(context: BootstrapContext<Enchantment>) {
        val enchantments = context.lookup(Registries.ENCHANTMENT)
        val items = context.lookup(Registries.ITEM)

        /*
        register(
            context,
            UNBREAKING,
            Enchantment.enchantment(
                    Enchantment.definition(
                        holdergetter2.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(5, 8),
                        Enchantment.dynamicCost(55, 8),
                        2,
                        EquipmentSlotGroup.ANY
                    )
                )
                .withEffect(
                    EnchantmentEffectComponents.ITEM_DAMAGE,
                    new RemoveBinomial(new LevelBasedValue.Fraction(LevelBasedValue.perLevel(2.0F), LevelBasedValue.perLevel(10.0F, 5.0F))),
                    MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.ARMOR_ENCHANTABLE))
                )
                .withEffect(
                    EnchantmentEffectComponents.ITEM_DAMAGE,
                    new RemoveBinomial(new LevelBasedValue.Fraction(LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(2.0F, 1.0F))),
                    InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.ARMOR_ENCHANTABLE)))
                )
        );
         */
        SimplestBroadaxes.LOGGER.info("Adding broadaxe enchantment to registry")
        register(context, BROADAXE_ENCHANTMENT,
            Enchantment.enchantment(
                Enchantment.definition(
                    // Primary items only
                    items.getOrThrow(ModTagItems.BROADAXES),
                    5, // Weight
                    3, // Max level
                    // Min cost
                    Enchantment.dynamicCost(5, 8),
                    // Max cost
                    Enchantment.dynamicCost(55, 8),
                    // Anvil cost
                    2,
                    EquipmentSlotGroup.MAINHAND
                )
            )
        )
    }

    private fun register(registry: BootstrapContext<Enchantment>, key: ResourceKey<Enchantment>, builder: Enchantment.Builder) {
        registry.register(key, builder.build(key.location()));
    }

    fun register() {

    }
}