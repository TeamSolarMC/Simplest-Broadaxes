package net.teamsolar.simplest_broadaxes.event;

import net.teamsolar.simplest_broadaxes.SimpestBroadaxes;
import net.teamsolar.simplest_broadaxes.item.ModItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber(modid = SimpestBroadaxes.MODID)
public class ModWanderingTraderEvent {

    @SubscribeEvent
    public static void wanderingVillagerTrade(WandererTradesEvent event) {
        event.getRareTrades().add((pTrader, pRandom) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 12),
                new ItemStack(ModItems.INSTANCE.getBROADAXE_SMITHING_TEMPLATE().get(), 1),
                1, 2, 0.2f
        ));
    }
}