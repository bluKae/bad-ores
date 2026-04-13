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

package de.blukae.badores;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BadOresClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue WEBSITE_ALLOW_OPEN = BUILDER.comment(
                    "Whether to allow the Website Ore to open a random, but curated URL in the browser")
            .define("websiteAllowOpen", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
