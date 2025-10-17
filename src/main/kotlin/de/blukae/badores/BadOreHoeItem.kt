package de.blukae.badores

import de.blukae.badores.ore.BadOre
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class BadOreHoeItem(val ore: BadOre, material: ToolMaterial, attackDamage: Float, attackSpeed: Float, properties: Properties) : HoeItem(material, attackDamage, attackSpeed, properties) {
    override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(stack, level, entity, slot)
        ore.onInventoryTick(stack, level, entity, slot)
    }

    override fun postHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        super.postHurtEnemy(stack, target, attacker)
        ore.onHurtEnemy(stack, target, attacker)
    }

    override fun mineBlock(
        stack: ItemStack,
        level: Level,
        state: BlockState,
        pos: BlockPos,
        miningEntity: LivingEntity
    ): Boolean {
        ore.onMine(stack, level, state, pos, miningEntity)
        return super.mineBlock(stack, level, state, pos, miningEntity)
    }
}