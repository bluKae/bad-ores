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
import de.blukae.badores.ore.Fleesonsite
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class FleesonsiteEntity(type: EntityType<out PathfinderMob>, level: Level) : PathfinderMob(type, level) {
    override fun registerGoals() {
        goalSelector.addGoal(0, FloatGoal(this))
        goalSelector.addGoal(1, PanicGoal(this, 1.25))
        goalSelector.addGoal(3, AvoidEntityGoal(this, Player::class.java, 6.0f, 1.0, 1.2))
        goalSelector.addGoal(6, WaterAvoidingRandomStrollGoal(this, 1.0))
        goalSelector.addGoal(8, RandomLookAroundGoal(this))
    }

    override fun getAmbientSound(): SoundEvent = BadOres.FLEESONSITE_AMBIENT

    override fun getDeathSound(): SoundEvent = BadOres.FLEESONSITE_DEATH

    override fun getHurtSound(damageSource: DamageSource): SoundEvent = BadOres.FLEESONSITE_HURT

    override fun tick() {
        super.tick()
        if (tickCount % 10 == 0 && !level().isClientSide && level().getNearestPlayer(this, 10.0) == null) {
            level().setBlockAndUpdate(blockPosition(), Fleesonsite.oreBlock.get().defaultBlockState())
            playSound(SoundEvents.CHICKEN_EGG)
            spawnAnim()
            discard()
        }
    }
}