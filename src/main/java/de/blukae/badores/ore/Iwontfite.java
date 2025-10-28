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
import de.blukae.badores.advancement.HurtIwontfiteTrigger;

import java.util.function.Supplier;

public class Iwontfite implements OreTemplate {
    public static final Supplier<HurtIwontfiteTrigger> HURT_IWONTFITE_TRIGGER = BadOres.TRIGGER_TYPES.register(
            "hurt_iwontfite",
            HurtIwontfiteTrigger::new);
}
