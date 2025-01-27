package net.teamsolar.simplest_broadaxes.event

import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.teamsolar.simplest_broadaxes.item.ModItems
import net.minecraft.village.TradeOffer;

object ModCustomTrades {
    fun registerCustomTrades() {
        TradeOfferHelper.registerWanderingTraderOffers(2) {
            it.add{
                entity, random -> TradeOffer(
                    ItemStack(Items.EMERALD, 12),
                    ItemStack(ModItems.BROADAXE_SMITHING_TEMPLATE, 1),
1, 2, 0.2f
                )
            }
        }
    }
}