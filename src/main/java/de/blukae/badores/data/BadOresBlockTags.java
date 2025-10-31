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

package de.blukae.badores.data;

import de.blukae.badores.BadOres;
import de.blukae.badores.ore.BadOre;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BadOresBlockTags extends BlockTagsProvider {
    public BadOresBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BadOres.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        IntrinsicTagAppender<Block> allOres = tag(BadOres.BAD_ORES_TAG);

        for (BadOre ore : BadOre.values()) {
            allOres.add(ore.oreBlock.get());
            IntrinsicTagAppender<Block> ores = tag(ore.ores).add(ore.oreBlock.get());
            IntrinsicTagAppender<Block> toolNeeded = tag(ore.template.toolTag()).add(ore.oreBlock.get());

            if (ore.deepslateOreBlock != null) {
                allOres.add(ore.deepslateOreBlock.get());
                ores.add(ore.deepslateOreBlock.get());
                toolNeeded.add(ore.deepslateOreBlock.get());
            }

            TagKey<Block> levelTag = ore.template.levelTag();
            if (levelTag != null) {
                IntrinsicTagAppender<Block> mineableWith = tag(levelTag).add(ore.oreBlock.get());
                if (ore.deepslateOreBlock != null) {
                    mineableWith.add(ore.deepslateOreBlock.get());
                }
            }

            if (ore.tools != null) {
                tag(ore.tools.tier.getIncorrectBlocksForDrops())
                        .addTag(ore.template.getToolInfo().incorrectBlocksForDrops());
            }
        }
    }
}
