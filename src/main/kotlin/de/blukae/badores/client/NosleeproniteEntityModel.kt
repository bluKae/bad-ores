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

class NosleeproniteEntityModel(root: ModelPart) : EntityModel<LivingEntityRenderState>(root) {
    private val bottom = root.getChild("bottom")
    private val top = root.getChild("top")
    private val mouthbottom = root.getChild("mouthbottom")
    private val mouthtop = root.getChild("mouthtop")
    private val teethbottom1 = root.getChild("teethbottom1")
    private val teethbottom2 = root.getChild("teethbottom2")
    private val teethbottom3 = root.getChild("teethbottom3")
    private val teethtop1 = root.getChild("teethtop1")
    private val teethtop2 = root.getChild("teethtop2")
    private val teethtop3 = root.getChild("teethtop3")

    companion object {
        // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
        val LAYER = ModelLayerLocation(rl("nosleeptonite"), "main")

        fun createBodyLayer(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            partdefinition.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                    .addBox(-8.0f, 0.0f, -14.0f, 16.0f, 8.0f, 16.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, 0.632f, 0.0f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "top",
                CubeListBuilder.create().texOffs(0, 25).mirror()
                    .addBox(-8.0f, -8.0f, -14.0f, 16.0f, 8.0f, 16.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, -0.632f, 0.0f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "mouthbottom",
                CubeListBuilder.create().texOffs(65, 0).mirror()
                    .addBox(-7.5f, -2.0f, -13.5f, 15.0f, 2.0f, 15.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, 0.632f, 0.0f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "mouthtop",
                CubeListBuilder.create().texOffs(65, 18).mirror()
                    .addBox(-7.5f, 0.0f, -13.5f, 15.0f, 2.0f, 15.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, -0.632f, 0.0f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "teethbottom1",
                CubeListBuilder.create().texOffs(65, 36).mirror()
                    .addBox(-7.0f, -6.0f, -13.0f, 14.0f, 5.0f, 0.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, 0.7436f, 0.0f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "teethbottom2",
                CubeListBuilder.create().texOffs(65, 28).mirror()
                    .addBox(0.0f, -5.0f, -13.0f, 0.0f, 5.0f, 14.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(-7.0f, 16.0f, 6.0f, 0.7064f, 0.1115f, -0.1487f)
            )

            partdefinition.addOrReplaceChild(
                "teethbottom3",
                CubeListBuilder.create().texOffs(65, 34).mirror()
                    .addBox(0.0f, -5.0f, -13.0f, 0.0f, 5.0f, 14.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(7.0f, 16.0f, 6.0f, 0.7064f, -0.1115f, 0.1487f)
            )

            partdefinition.addOrReplaceChild(
                "teethtop1",
                CubeListBuilder.create().texOffs(95, 36).mirror()
                    .addBox(-7.0f, -1.0f, -13.0f, 14.0f, 5.0f, 0.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, -0.5577f, 0.0f, 0.0f)
            )

            partdefinition.addOrReplaceChild(
                "teethtop2",
                CubeListBuilder.create().texOffs(95, 28).mirror()
                    .addBox(0.0f, 0.0f, -13.0f, 0.0f, 5.0f, 14.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(-7.0f, 16.0f, 6.0f, -0.632f, -0.1115f, -0.1859f)
            )

            partdefinition.addOrReplaceChild(
                "teethtop3",
                CubeListBuilder.create().texOffs(95, 34).mirror()
                    .addBox(0.0f, 0.0f, -13.0f, 0.0f, 5.0f, 14.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(7.0f, 16.0f, 6.0f, -0.632f, 0.1115f, 0.1859f)
            )

            return LayerDefinition.create(meshdefinition, 128, 64)
        }
    }

    override fun setupAnim(renderState: LivingEntityRenderState) {
        super.setupAnim(renderState)

        val pos: Float = renderState.walkAnimationPos
        val speed: Float = renderState.walkAnimationSpeed

        mouthbottom.xRot = (Mth.cos(pos * 0.04F) + 1) * 20.0F * speed
        mouthtop.xRot = -mouthbottom.xRot
        bottom.xRot = mouthbottom.xRot
        teethbottom1.xRot = mouthbottom.xRot
        teethbottom2.xRot = mouthbottom.xRot
        teethbottom3.xRot = mouthbottom.xRot
        top.xRot = mouthtop.xRot
        teethtop1.xRot = mouthtop.xRot
        teethtop2.xRot = mouthtop.xRot
        teethtop3.xRot = mouthtop.xRot
    }

}