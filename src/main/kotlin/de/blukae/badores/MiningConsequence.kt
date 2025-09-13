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

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.blukae.badores.MiningConsequence.Condition
import net.minecraft.util.StringRepresentable
import net.minecraft.util.context.ContextKey
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

enum class MiningConsequence: StringRepresentable {
    NONE,
    KILLED_BY_KILLIUM;

    override fun getSerializedName(): String = name

    class Condition(val consequence: MiningConsequence): LootItemCondition {
        companion object {
            val CODEC: MapCodec<MiningConsequence.Condition> = RecordCodecBuilder.mapCodec { instance ->
                instance.group(
                    StringRepresentable.fromEnum(::values)
                        .optionalFieldOf("consequence", NONE)
                        .forGetter { it.consequence }
                ).apply(instance, ::Condition)
            }
        }

        override fun getReferencedContextParams(): Set<ContextKey<*>> = setOf(BadOres.MINING_CONSEQUENCE_CONTEXT_KEY)

        override fun getType(): LootItemConditionType = BadOres.MINING_CONSEQUENCE_CONDITION_TYPE

        override fun test(context: LootContext): Boolean {
            val contextConsequence =
                context.getOptionalParameter(BadOres.MINING_CONSEQUENCE_CONTEXT_KEY) ?: return consequence == NONE

            return contextConsequence == consequence
        }
    }
}