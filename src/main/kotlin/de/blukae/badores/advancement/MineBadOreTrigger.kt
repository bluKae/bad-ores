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
import net.minecraft.advancements.critereon.*
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderGetter
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.LocationCheck
import java.util.*

class MineBadOreTrigger : SimpleCriterionTrigger<MineBadOreTrigger.TriggerInstance>() {
    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer, level: ServerLevel, pos: BlockPos, state: BlockState, tool: ItemStack) {
        val params = LootParams.Builder(level)
            .withParameter(LootContextParams.ORIGIN, pos.center)
            .withParameter(LootContextParams.THIS_ENTITY, player)
            .withParameter(LootContextParams.BLOCK_STATE, state)
            .withParameter(LootContextParams.TOOL, tool)
            .create(LootContextParamSets.ADVANCEMENT_LOCATION)
        val context: LootContext = LootContext.Builder(params).create(Optional.empty())
        super.trigger(player) { it.matches(context) }
    }

    class TriggerInstance(player: Optional<ContextAwarePredicate>, location: Optional<ContextAwarePredicate>) :
        SimpleInstance {
        private val _player = player
        private val _location = location

        override fun player() = _player
        fun location() = _location

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { instance ->
                instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                        .forGetter(TriggerInstance::player),
                    ContextAwarePredicate.CODEC.optionalFieldOf("location")
                        .forGetter(TriggerInstance::location)
                )
                    .apply(instance, ::TriggerInstance)
            }

            fun minedAny(): Criterion<TriggerInstance> =
                BadOres.MINE_BAD_ORE_TRIGGER.createCriterion(TriggerInstance(Optional.empty(), Optional.empty()))

            fun minedTag(blocks: HolderGetter<Block>, tag: TagKey<Block>): Criterion<TriggerInstance> {
                val locationPredicate = LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block().of(blocks, tag))

                val location = ContextAwarePredicate.create(LocationCheck.checkLocation(locationPredicate).build())

                return BadOres.MINE_BAD_ORE_TRIGGER.createCriterion(
                    TriggerInstance(
                        Optional.empty(),
                        Optional.of(location)
                    )
                )
            }
        }

        fun matches(context: LootContext) = _location.isEmpty || _location.get().matches(context)

        override fun validate(validator: CriterionValidator) {
            super.validate(validator)
            _location.ifPresent {
                validator.validate(
                    it,
                    LootContextParamSets.ADVANCEMENT_LOCATION,
                    "location"
                )
            }
        }

    }
}