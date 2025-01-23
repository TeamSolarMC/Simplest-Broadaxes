package net.teamsolar.simplest_broadaxes.loot

import com.mojang.serialization.MapCodec
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.loot.IGlobalLootModifier
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.teamsolar.simplest_broadaxes.SimpestBroadaxes
import java.util.function.Supplier

object ModLootModifiers {
    val GLOBAL_LOOT_MODIFIER_SERIALIZERS: DeferredRegister<MapCodec<out IGlobalLootModifier?>> =
        DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, SimpestBroadaxes.MODID)

    val MY_LOOT_MODIFIER: Supplier<MapCodec<ModLootModifier>> =
        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register<MapCodec<ModLootModifier>>(
            "my_loot_modifier",
            Supplier { ModLootModifier.CODEC })


    fun register(eventBus: IEventBus) {
        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(eventBus)
    }
}
