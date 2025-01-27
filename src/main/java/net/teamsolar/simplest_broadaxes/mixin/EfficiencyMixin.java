package net.teamsolar.simplest_broadaxes.mixin;

import net.minecraft.item.ItemStack;
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes;
import net.teamsolar.simplest_broadaxes.item.BroadaxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(net.minecraft.enchantment.EfficiencyEnchantment.class)
abstract public class EfficiencyMixin {
    // Make Efficiency not suitable for broadaxes.
    @Inject(at = @At("HEAD"), method = "isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    public void isAcceptableItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> CI) {
        if(itemStack.getItem() instanceof BroadaxeItem) {
            CI.setReturnValue(false);
        }
    }
}
