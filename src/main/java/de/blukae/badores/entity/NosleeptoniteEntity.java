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

package de.blukae.badores.entity;

import de.blukae.badores.ore.Fleesonsite;
import de.blukae.badores.ore.Nosleeptonite;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class NosleeptoniteEntity extends Monster {
    public NosleeptoniteEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Nosleeptonite.NOSLEEPTONITE_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return Nosleeptonite.NOSLEEPTONITE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Nosleeptonite.NOSLEEPTONITE_DEATH.get();
    }
}
