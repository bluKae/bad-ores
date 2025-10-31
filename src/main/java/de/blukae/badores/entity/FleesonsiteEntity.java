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

import de.blukae.badores.ore.BadOre;
import de.blukae.badores.ore.Fleesonsite;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class FleesonsiteEntity extends PathfinderMob {
    public static final EntityDataAccessor<Boolean> DATA_IS_DEEPSLATE = SynchedEntityData.defineId(
            FleesonsiteEntity.class,
            EntityDataSerializers.BOOLEAN);

    public FleesonsiteEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public boolean isDeepslate() {
        return entityData.get(DATA_IS_DEEPSLATE);
    }

    public void setDeepslate(boolean deepslate) {
        entityData.set(DATA_IS_DEEPSLATE, deepslate);
    }

    public BlockState getBlockState() {
        return isDeepslate() ?
                BadOre.FLEESONSITE.deepslateOreBlock.get().defaultBlockState() :
                BadOre.FLEESONSITE.oreBlock.get().defaultBlockState();
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, 1.25));
        goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0f, 1.0, 1.2));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_DEEPSLATE, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount % 10 == 0 && !level().isClientSide() && level().getNearestPlayer(
                getX(),
                getY(),
                getZ(),
                12.0,
                true) == null) {
            level().setBlockAndUpdate(blockPosition(), getBlockState());

            playSound(SoundEvents.CHICKEN_EGG);
            spawnAnim();
            discard();
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return Fleesonsite.FLEESONSITE_AMBIENT.get();
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("IsDeepslate", isDeepslate());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setDeepslate(input.getBooleanOr("IsDeepslate", false));
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return Fleesonsite.FLEESONSITE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return Fleesonsite.FLEESONSITE_DEATH.get();
    }
}
