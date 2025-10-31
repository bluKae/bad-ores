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
import de.blukae.badores.ore.Iwontfite;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class HurtIwontfiteTrigger extends SimpleCriterionTrigger<HurtIwontfiteTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, instance -> true);
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                .forGetter(TriggerInstance::player))
                .apply(instance, TriggerInstance::new));

        public static Criterion<TriggerInstance> hurtWithIwontfite() {
            return Iwontfite.HURT_IWONTFITE_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty()));
        }
    }
}
