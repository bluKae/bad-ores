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

import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.PlacementModifier

object Lite : BadOre("lite") {
    override fun size() = 2
    override fun placement(): PlacementModifier = CountPlacement.of(UniformInt.of(4, 11))
    override fun addBlockProperties(properties: BlockBehaviour.Properties): BlockBehaviour.Properties =
        properties.lightLevel { 10 }
}