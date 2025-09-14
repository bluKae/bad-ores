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

import de.blukae.badores.BadOres.rl
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.util.Mth

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

class FleesonsiteEntityModel(root: ModelPart) : EntityModel<LivingEntityRenderState>(root) {
    private val block: ModelPart = root.getChild("block")
    private val footright: ModelPart = root.getChild("footright")
    private val toeright1: ModelPart = root.getChild("toeright1")
    private val toeright2: ModelPart = root.getChild("toeright2")
    private val toeright3: ModelPart = root.getChild("toeright3")
    private val footleft: ModelPart = root.getChild("footleft")
    private val toeleft1: ModelPart = root.getChild("toeleft1")
    private val toeleft2: ModelPart = root.getChild("toeleft2")
    private val toeleft3: ModelPart = root.getChild("toeleft3")

    companion object {
        val LAYER = ModelLayerLocation(rl("fleesonsite"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            partdefinition.addOrReplaceChild(
                "block",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                    .addBox(-8.0f, 0.0f, -6.0f, 16.0f, 16.0f, 16.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 5.0f, 0.0f, -0.1115f, 0.0f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "footright",
                CubeListBuilder.create().texOffs(0, 33).mirror()
                    .addBox(-3.0f, 0.0f, -3.0f, 6.0f, 4.0f, 6.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.0f, 0.3718f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "toeright1",
                CubeListBuilder.create().texOffs(0, 44).mirror()
                    .addBox(-1.5f, 0.0f, -8.0f, 4.0f, 4.0f, 7.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.2231f, 0.0721f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "toeright2",
                CubeListBuilder.create().texOffs(70, 0).mirror()
                    .addBox(-2.0f, 0.5f, -7.5f, 3.0f, 3.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.2231f, 0.4811f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "toeright3",
                CubeListBuilder.create().texOffs(70, 9).mirror()
                    .addBox(-2.5f, 0.5f, -6.0f, 3.0f, 3.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.2231f, 0.9666f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "footleft",
                CubeListBuilder.create().texOffs(25, 33).mirror()
                    .addBox(-3.0f, 0.0f, -3.0f, 6.0f, 4.0f, 6.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.0f, -0.3718f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "toeleft1",
                CubeListBuilder.create().texOffs(25, 44).mirror()
                    .addBox(-2.5f, 0.0f, -8.0f, 4.0f, 4.0f, 7.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.2231f, -0.0721f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "toeleft2",
                CubeListBuilder.create().texOffs(90, 0).mirror()
                    .addBox(-1.0f, 0.5f, -7.5f, 3.0f, 3.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.2231f, -0.481f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "toeleft3",
                CubeListBuilder.create().texOffs(90, 9).mirror()
                    .addBox(-0.5f, 0.5f, -6.0f, 3.0f, 3.0f, 5.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.2231f, -0.9667f, 0.0f)
            )

            return LayerDefinition.create(meshdefinition, 128, 64)
        }
    }

    override fun setupAnim(renderState: LivingEntityRenderState) {
        super.setupAnim(renderState)

        val pos: Float = renderState.walkAnimationPos
        val speed: Float = renderState.walkAnimationSpeed

        block.yRot = 5f + Mth.cos(pos * 1.4f) * 2.5f * speed
        footright.xRot = Mth.cos(pos * 0.6662f) * 1.4f * speed
        toeright1.xRot = footright.xRot
        toeright2.xRot = footright.xRot
        toeright3.xRot = footright.xRot
        footleft.xRot = Mth.cos(pos * 0.6662f + Math.PI.toFloat()) * 1.4f * speed
        toeleft1.xRot = footleft.xRot
        toeleft2.xRot = footleft.xRot
        toeleft3.xRot = footleft.xRot
    }
}