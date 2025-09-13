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

package de.blukae.badores.client

import de.blukae.badores.BadOres
import de.blukae.badores.entity.FleesonsiteEntity
import de.blukae.badores.entity.NosleeptoniteEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState

class FleesonsiteEntityRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<FleesonsiteEntity, LivingEntityRenderState, FleesonsiteEntityModel>(context,
        FleesonsiteEntityModel(context.bakeLayer(FleesonsiteEntityModel.LAYER)),
        1.0f) {

    override fun getTextureLocation(renderState: LivingEntityRenderState) = BadOres.rl("textures/entity/fleesonsite.png")
    override fun createRenderState() = LivingEntityRenderState()
}