package net.teamsolar.simplest_broadaxes.event

import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.trading.ItemCost
import net.minecraft.world.item.trading.MerchantOffer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.village.WandererTradesEvent
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes
import net.teamsolar.simplest_broadaxes.item.ModItems

@EventBusSubscriber(modid = SimplestBroadaxes.MODID)
object ModWanderingTraderEvent {
    @SubscribeEvent
    fun wanderingVillagerTrade(event: WandererTradesEvent) {
        // SimplestBroadaxes.LOGGER.info("Wanderer Trades Event on Kotlin")
        event.rareTrades.add(ItemListing { pTrader: Entity?, pRandom: RandomSource? ->
            MerchantOffer(
                ItemCost(Items.EMERALD, 12),
                ItemStack(ModItems.BROADAXE_SMITHING_TEMPLATE.get(), 1),
                1, 2, 0.2f
            )
        })
    }
}