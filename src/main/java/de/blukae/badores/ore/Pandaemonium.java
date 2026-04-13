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

package de.blukae.badores.ore;

import de.blukae.badores.BadOres;
import de.blukae.badores.block.BadOreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.DeferredSoundType;

import java.util.function.Supplier;

public class Pandaemonium implements OreTemplate {

    public static final Supplier<SoundEvent> PANDAEMONIUM_BREAK_SOUND_EVENT = BadOres.SOUND_EVENTS.register(
            "block.pandaemonium.break",
            SoundEvent::createVariableRangeEvent);

    @Override
    public HeightRangePlacement getHeightPlacementModifier() {
        return HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(10));
    }

    @Override
    public BlockBehaviour.Properties getOreBlockProperties(boolean isDeepslate) {
        return OreTemplate.super.getOreBlockProperties(isDeepslate)
                .sound(new DeferredSoundType(
                        1.0f,
                        1.0f,
                        PANDAEMONIUM_BREAK_SOUND_EVENT,
                        () -> SoundEvents.STONE_STEP,
                        () -> SoundEvents.STONE_PLACE,
                        () -> SoundEvents.STONE_HIT,
                        () -> SoundEvents.STONE_FALL));
    }

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(0.0f, 2.0f))
                        .add(LootItem.lootTableItem(Blocks.NETHERRACK))
                        .add(LootItem.lootTableItem(Blocks.OBSIDIAN))
                        .add(LootItem.lootTableItem(Items.NETHER_WART))
                        .add(LootItem.lootTableItem(Items.FIRE_CHARGE))
                        .add(LootItem.lootTableItem(Items.BLAZE_ROD))
                        .add(LootItem.lootTableItem(Items.MAGMA_CREAM)));
    }

    @Override
    public IntProvider getTickRate(boolean isIngotBlock) {
        return ConstantInt.of(1000);
    }

    @Override
    public void onTick(Level level, BlockPos pos, BlockState state, BadOreBlockEntity blockEntity) {
        if (!level.isClientSide() && level.getRandom().nextInt(10) == 0) {
            level.playSound(null, pos, PANDAEMONIUM_BREAK_SOUND_EVENT.get(), SoundSource.BLOCKS);
        }
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack,
                                boolean dropExperience) {
        if (!level.isClientSide()) {
            RandomSource random = level.getRandom();
            int veins = random.nextInt(12) + 3;
            for (int i = 0; i < veins; i++) {
                Vec3 direction = new Vec3(
                        random.nextDouble() - random.nextDouble(),
                        random.nextDouble() - random.nextDouble(),
                        random.nextDouble() - random.nextDouble())
                        .normalize();
                int length = random.nextInt(3, 12);

                for (int iteration = 0; iteration < length; iteration++) {
                    BlockPos blockPos = new BlockPos(
                            (int) (pos.getCenter().x + direction.x * iteration),
                            (int) (pos.getCenter().y + direction.y * iteration),
                            (int) (pos.getCenter().z + direction.z * iteration));
                    setBlock(level, blockPos, Blocks.NETHERRACK.defaultBlockState());
                    BlockPos above = blockPos.above();
                    if (random.nextFloat() < 0.2F) {
                        setBlock(level, above, Blocks.FIRE.defaultBlockState());
                    }
                    if (random.nextFloat() < 0.05F) {
                        Vec3 piglinPos = above.getBottomCenter();
                        AABB aabb = EntityType.ZOMBIFIED_PIGLIN.getDimensions()
                                .makeBoundingBox(piglinPos);
                        if (level.noBlockCollision(null, aabb)) {
                            ZombifiedPiglin piglin = EntityType.ZOMBIFIED_PIGLIN.create(
                                    level,
                                    EntitySpawnReason.TRIGGERED);
                            if (piglin != null) {
                                piglin.snapTo(piglinPos);
                                level.addFreshEntity(piglin);
                                piglin.spawnAnim();
                            }
                        }
                    }
                }
            }
        }
    }

    private void setBlock(Level level, BlockPos pos, BlockState state) {
        if (level.getBlockState(pos).canBeReplaced() && state.canSurvive(level, pos)) {
            level.setBlockAndUpdate(pos, state);
        }
    }
}
