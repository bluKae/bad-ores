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

import de.blukae.badores.BadOresClientConfig;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * This class implements the same functionality as the Website Ore of the original Bad Ores mod.
 * Only a fixed set of URLs can be automatically opened through the implementation internally used by Minecraft.
 * This behaviour can be changed in the client with the mod's config by setting the option websiteAllowOpen to false.
 */
public class Website implements OreTemplate {
    /**
     * These are the *only* URLs, this Ore can open automatically, corresponding to the URLs used in the original mod.
     */
    private static final String[] URLS = new String[]{
            "http://www.minecraft.net",
            "http://www.minecraftforge.net",
            "http://www.google.com",
            "http://www.minecraftforum.net",
            "http://www.minecraftwiki.net",
            "http://mcp.ocean-labs.de/modjam/"
    };

    @Override
    public LootTable.Builder getCustomLootTable(BlockLootSubProvider provider) {
        return LootTable.lootTable();
    }

    @Override
    public void onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest) {
        if (level.isClientSide() && !player.isCreative() && BadOresClientConfig.WEBSITE_ALLOW_OPEN.isTrue()) {
            String url = URLS[level.random.nextInt(URLS.length)];

            try {
                Util.getPlatform().openUri(new URI(url));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
