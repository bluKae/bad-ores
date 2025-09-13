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

package de.blukae.badores.entity

import de.blukae.badores.BadOres
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class NosleeptoniteEntity(type: EntityType<out Monster>, level: Level) : Monster(type, level) {
    override fun registerGoals() {
        goalSelector.addGoal(1, FloatGoal(this))
        goalSelector.addGoal(4, MeleeAttackGoal(this, 1.0, false))
        goalSelector.addGoal(7, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(8, LookAtPlayerGoal(this, Player::class.java, 8.0f))
        goalSelector.addGoal(8, RandomLookAroundGoal(this))

        targetSelector.addGoal(1, HurtByTargetGoal(this).setAlertOthers())
        targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Player::class.java, true))
    }

    override fun getMovementEmission(): MovementEmission {
        return MovementEmission.EVENTS
    }

    override fun getAmbientSound(): SoundEvent = BadOres.NOSLEEPTONITE_AMBIENT

    override fun getDeathSound(): SoundEvent = BadOres.NOSLEEPTONITE_DEATH

    override fun getHurtSound(damageSource: DamageSource): SoundEvent = BadOres.NOSLEEPTONITE_HURT
}