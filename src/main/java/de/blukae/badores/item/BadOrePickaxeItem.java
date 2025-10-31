package de.blukae.badores.item;

import de.blukae.badores.ore.OreTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BadOrePickaxeItem extends PickaxeItem {
    private final OreTemplate template;

    public BadOrePickaxeItem(OreTemplate template, Tier tier, Properties properties) {
        super(tier, properties);
        this.template = template;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        template.onEntityHurt(stack, target, attacker);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        template.onMine(stack, level, state, pos, miningEntity);
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        template.onInventoryTick(stack, level, entity, slotId, isSelected);
    }
}
