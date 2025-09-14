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
import de.blukae.badores.ToolInfo
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.PlacementModifier


object Crappium : BadOre("crappium") {
    override fun placement(): PlacementModifier = CountPlacement.of(UniformInt.of(0, 7))
    override fun heightPlacement(): HeightRangePlacement =
        HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(120))

    override fun size() = 8
    override fun hasIngot() = true
    override fun tools() = ToolInfo(0, 1, 2.0f, 0.0f, 15)
    override fun armor() = ArmorInfo(1, intArrayOf(1, 1, 1, 1), 1)
}