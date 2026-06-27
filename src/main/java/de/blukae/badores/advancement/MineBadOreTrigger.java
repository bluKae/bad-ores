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

package de.blukae.badores.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.blukae.badores.BadOres;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.ContextAwarePredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class MineBadOreTrigger extends SimpleCriterionTrigger<MineBadOreTrigger.TriggerInstance> {

    @Override
    public Codec<MineBadOreTrigger.TriggerInstance> codec() {
        return MineBadOreTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, ServerLevel level, BlockPos pos, BlockState state, ItemStack tool) {
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.TOOL, tool)
                .create(LootContextParamSets.ADVANCEMENT_LOCATION);
        LootContext context = new LootContext.Builder(params).create(Optional.empty());
        super.trigger(player, instance -> instance.matches(context));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<ContextAwarePredicate> location) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                        .forGetter(TriggerInstance::player),
                                ContextAwarePredicate.CODEC.optionalFieldOf("location")
                                        .forGetter(TriggerInstance::location))
                        .apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> minedAny() {
            return BadOres.MINE_BAD_ORE_TRIGGER.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
        }

        public static Criterion<TriggerInstance> minedTag(HolderGetter<Block> blocks, TagKey<Block> tag) {
            LocationPredicate.Builder locationPredicate = LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block().of(blocks, tag));

            ContextAwarePredicate location = ContextAwarePredicate.create(LocationCheck.checkLocation(locationPredicate)
                    .build());

            return BadOres.MINE_BAD_ORE_TRIGGER.get()
                    .createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
        }

        public void validate(ValidationContextSource validator) {
            SimpleInstance.super.validate(validator);
            Validatable.validate(
                    validator.context(LootContextParamSets.ADVANCEMENT_LOCATION),
                    "location",
                    this.location);
        }

        public boolean matches(LootContext context) {
            return location.isEmpty() || location.get().matches(context);
        }
    }
}
