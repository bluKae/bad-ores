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

package de.blukae.badores.block;

import de.blukae.badores.BadOres;
import de.blukae.badores.ore.OreTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BadOreBlock extends Block implements EntityBlock {
    @Nullable
    public final IntProvider tickRate;
    private final OreTemplate template;
    private final boolean isOreBlock;

    public BadOreBlock(OreTemplate template, boolean isOreBlock, Properties properties) {
        super(properties);
        this.template = template;
        this.isOreBlock = isOreBlock;
        this.tickRate = template.getTickRate(isOreBlock);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return tickRate != null ? new BadOreBlockEntity(blockPos, blockState) : null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                            BlockEntityType<T> blockEntityType) {
        if (blockEntityType == BadOres.BAD_ORE_BLOCK_ENTITY.get() && tickRate != null) {
            return (level1, pos, state1, blockEntity) -> ((BadOreBlockEntity) blockEntity)
                    .tick(level1, pos, state1, template, tickRate);
        }

        return null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        return template.onUseWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemInteractionResult result = template.onUseItemOn(stack, state, level, pos, player, hand, hitResult);
        if (result != null) {
            return result;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = template.getShape(isOreBlock, state, level, pos, context);
        if (shape != null) {
            return shape;
        }
        return super.getShape(state, level, pos, context);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        template.onRandomTick(state, level, pos, random);
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float progress = template.getDestroyProgress(state, player, level, pos);
        if (!Float.isNaN(progress)) {
            return progress;
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack,
                                   boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        template.spawnAfterBreak(state, level, pos, stack, dropExperience);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        super.attack(state, level, pos, player);
        template.onAttack(state, level, pos, player);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       boolean willHarvest, FluidState fluid) {
        template.onDestroyedByPlayer(state, level, pos, player, willHarvest);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);
        template.onExploded(state, level, pos, explosion);
    }
}
