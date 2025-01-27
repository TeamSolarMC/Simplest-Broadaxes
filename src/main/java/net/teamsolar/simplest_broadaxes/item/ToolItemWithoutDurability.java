package net.teamsolar.simplest_broadaxes.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class ToolItemWithoutDurability extends Item {
    private final ToolMaterial material;

    public ToolItemWithoutDurability(ToolMaterial material, Item.Settings settings) {
        super(settings);
        this.material = material;
    }

    public ToolMaterial getMaterial() {
        return this.material;
    }

    public int getEnchantability() {
        return this.material.getEnchantability();
    }

    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }
}
