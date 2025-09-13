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

package de.blukae.badores

data class ArmorInfo(val durability: Int, val reductions: IntArray, val enchantility: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArmorInfo

        if (durability != other.durability) return false
        if (enchantility != other.enchantility) return false
        if (!reductions.contentEquals(other.reductions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = durability
        result = 31 * result + enchantility
        result = 31 * result + reductions.contentHashCode()
        return result
    }
}