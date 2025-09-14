/*
 * Copyright (C) 2025 bluKae
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.blukae.badores.ore

import de.blukae.badores.ArmorInfo
import de.blukae.badores.RandomTranslation
import de.blukae.badores.ToolInfo
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.neoforged.neoforge.common.Tags


object Polite : BadOre("polite") {
    override fun biomes(lookup: HolderGetter<Biome>): HolderSet<Biome> = lookup.getOrThrow(Tags.Biomes.IS_COLD)
    override fun placement(): PlacementModifier = CountPlacement.of(UniformInt.of(4, 7))
    override fun hasIngot() = true
    override fun tools() = ToolInfo(1, 200, 5.0f, 2.0f, 15)
    override fun armor() = ArmorInfo(8, intArrayOf(2, 4, 3, 1), 8)

    override fun onDestroyedByPlayer(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        willHarvest: Boolean
    ) {
        val isNotOreBlock = (rawBlock?.let { state.`is`(it) } ?: false) || (ingotBlock?.let { state.`is`(it) } ?: false)

        if (player is ServerPlayer && !isNotOreBlock) {
            RandomTranslation("badores.polite.mined", "Polite ore mined").send(player)
        }
    }

    override fun onMine(stack: ItemStack, level: Level, state: BlockState, pos: BlockPos, miningEntity: LivingEntity) {
        if (miningEntity is ServerPlayer) {
            RandomTranslation("badores.polite.tool", "Politely mined").send(miningEntity)
        }
    }

    override fun onHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        if (attacker is ServerPlayer) {
            RandomTranslation("badores.polite.attack", "Politely attacked").send(attacker)
        }
    }

    override fun onArmorHurt(entity: LivingEntity, stack: ItemStack, slot: EquipmentSlot) {
        if (entity is ServerPlayer) {
            RandomTranslation("badores.polite.defend", "Politely defended").send(entity)
        }
    }

    // onToolMine
    // onToolAttack
    // onArmorAttacked
}