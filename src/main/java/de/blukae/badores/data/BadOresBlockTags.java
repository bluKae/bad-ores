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
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class BadOresBlockTags extends BlockTagsProvider {
    public BadOresBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BadOres.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TagAppender<Block> allOres = tag(BadOres.BAD_ORES_TAG);

        for (BadOre ore : BadOre.values()) {
            allOres.add(ore.oreBlock.getKey());
            TagAppender<Block> ores = tag(ore.ores).add(ore.oreBlock.getKey());
            TagAppender<Block> toolNeeded = tag(ore.template.toolTag()).add(ore.oreBlock.getKey());

            if (ore.deepslateOreBlock != null) {
                allOres.add(ore.deepslateOreBlock.getKey());
                ores.add(ore.deepslateOreBlock.getKey());
                toolNeeded.add(ore.deepslateOreBlock.getKey());
            }

            TagKey<Block> levelTag = ore.template.levelTag();
            if (levelTag != null) {
                TagAppender<Block> mineableWith = tag(levelTag).add(ore.oreBlock.getKey());
                if (ore.deepslateOreBlock != null) {
                    mineableWith.add(ore.deepslateOreBlock.getKey());
                }
            }

            if (ore.tools != null) {
                tag(ore.tools.material.incorrectBlocksForDrops())
                        .addTag(ore.template.getToolInfo().incorrectBlocksForDrops());
            }
        }
    }
}
