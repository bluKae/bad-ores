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

package de.blukae.badores.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.blukae.badores.BadOres
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.EntityPredicate
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import java.util.*

class MineKilliumTrigger : SimpleCriterionTrigger<MineKilliumTrigger.TriggerInstance>() {
    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) {
        super.trigger(player) { _ -> true }
    }

    class TriggerInstance(player: Optional<ContextAwarePredicate>) : SimpleInstance {
        private val _player = player

        override fun player() = _player

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
                instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                        .forGetter(TriggerInstance::player)
                )
                    .apply(instance, ::TriggerInstance)
            }

            fun minedSafely(): Criterion<TriggerInstance> =
                BadOres.MINE_KILLIUM_TRIGGER.createCriterion(TriggerInstance(Optional.empty()))
        }


    }
}