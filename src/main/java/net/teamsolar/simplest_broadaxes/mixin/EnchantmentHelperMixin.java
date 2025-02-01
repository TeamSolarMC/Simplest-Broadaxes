package net.teamsolar.simplest_broadaxes.mixin;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes;
import net.teamsolar.simplest_broadaxes.enchantment.EnchantmentExclusiveTo;
import net.teamsolar.simplest_broadaxes.enchantment.ItemExclusiveTo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(net.minecraft.enchantment.EnchantmentHelper.class)
abstract public class EnchantmentHelperMixin {
    // isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z
    // Make Trimming not suitable for other digging tools.
    @Inject(at = @At("TAIL"), method = "getPossibleEntries", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> CIR) {
        // ????
        var list = CIR.getReturnValue();
        // SimplestBroadaxes.INSTANCE.getLogger().info("List starting size: %d".formatted(list.size()));
        if(stack.isOf(Items.BOOK)) {
            // SimplestBroadaxes.INSTANCE.getLogger().info("New list size: %d".formatted(list.size()));
            return;
        } else {
            list.removeIf(entry -> {
                if(entry.enchantment instanceof EnchantmentExclusiveTo) {
                    if(((EnchantmentExclusiveTo) entry.enchantment).isExcludedItem(stack)) {
                        return true;
                    }
                }
                if(stack.getItem() instanceof ItemExclusiveTo) {
                    if(((ItemExclusiveTo) stack.getItem()).isExcludedEnchantment(entry.enchantment)) {
                        return true;
                    }
                }
                return false;
            });
            // SimplestBroadaxes.INSTANCE.getLogger().info("New list size: %d".formatted(list.size()));
            CIR.setReturnValue(list);
        }
    }
}