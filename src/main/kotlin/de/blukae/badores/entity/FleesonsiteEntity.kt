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
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

class FleesonsiteEntity(type: EntityType<out PathfinderMob>, level: Level) : PathfinderMob(type, level) {
    companion object {
        private val DATA_IS_DEEPSLATE =
            SynchedEntityData.defineId(FleesonsiteEntity::class.java, EntityDataSerializers.BOOLEAN)
    }

    var isDeepslate: Boolean
        get() = entityData.get(DATA_IS_DEEPSLATE)
        set(value) = entityData.set(DATA_IS_DEEPSLATE, value)

    val blockState: BlockState
        get() {
            val block = if (isDeepslate) Fleesonsite.deepslateOreBlock!! else Fleesonsite.oreBlock
            return block.get().defaultBlockState()
        }

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
        if (tickCount % 10 == 0 && !level().isClientSide && level().getNearestPlayer(x, y, z, 12.0, true) == null) {
            level().setBlockAndUpdate(blockPosition(), blockState)

            playSound(SoundEvents.CHICKEN_EGG)
            spawnAnim()
            discard()
        }
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_IS_DEEPSLATE, false)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        isDeepslate = input.getBooleanOr("IsDeepslate", false)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.putBoolean("IsDeepslate", isDeepslate)
    }
}