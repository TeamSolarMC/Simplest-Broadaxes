package net.teamsolar.simplest_broadaxes.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.teamsolar.simplest_broadaxes.SimplestBroadaxes;
import net.teamsolar.simplest_broadaxes.item.WorseAtMining;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.entity.player.PlayerEntity.class)
abstract public class PlayerMiningWorsenerMixin {
    private float getBaseBlockBreakingSpeed(PlayerEntity player, BlockState block) {
        return player.getInventory().getBlockBreakingSpeed(block);
    }
    private float getBaseEfficiencyBonus(PlayerEntity player, BlockState block) {
        int i = EnchantmentHelper.getEfficiency(player);
        return (float) (i * i + 1);
    }
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> CIR) {
        float returnValue = CIR.getReturnValueF();
        @SuppressWarnings("all")
        PlayerEntity player = (PlayerEntity) ((Object) this);
        ItemStack itemStack = player.getInventory().getMainHandStack();
        if(itemStack.getItem() instanceof WorseAtMining) {
            float efficiencyModifier = ((WorseAtMining) itemStack.getItem()).getEfficiencyBonusMultiplier();
            float base = getBaseBlockBreakingSpeed(player, block);
            float bonus = getBaseEfficiencyBonus(player, block);
            float total = base + bonus;
            // solve for the current proportion of the return value that is from efficiency... eh.. cant be bothered
            float c = returnValue / total;
            float newReturnValue = returnValue + (c * (bonus * (efficiencyModifier - 1.0f)));
            CIR.setReturnValue(newReturnValue);
        }
    }
}
