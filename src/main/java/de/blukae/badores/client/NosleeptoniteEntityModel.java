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

package de.blukae.badores.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.blukae.badores.BadOres;
import de.blukae.badores.entity.NosleeptoniteEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class NosleeptoniteEntityModel extends EntityModel<NosleeptoniteEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer
    // and passed into this model's constructor
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BadOres.rl("nosleeptonite"), "main");

    private final ModelPart root;

    private final ModelPart bottom;
    private final ModelPart top;
    private final ModelPart mouthbottom;
    private final ModelPart mouthtop;
    private final ModelPart teethbottom1;
    private final ModelPart teethbottom2;
    private final ModelPart teethbottom3;
    private final ModelPart teethtop1;
    private final ModelPart teethtop2;
    private final ModelPart teethtop3;

    protected NosleeptoniteEntityModel(ModelPart root) {
        this.root = root;

        bottom = root.getChild("bottom");
        top = root.getChild("top");
        mouthbottom = root.getChild("mouthbottom");
        mouthtop = root.getChild("mouthtop");
        teethbottom1 = root.getChild("teethbottom1");
        teethbottom2 = root.getChild("teethbottom2");
        teethbottom3 = root.getChild("teethbottom3");
        teethtop1 = root.getChild("teethtop1");
        teethtop2 = root.getChild("teethtop2");
        teethtop3 = root.getChild("teethtop3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "bottom",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-8.0f, 0.0f, -14.0f, 16.0f, 8.0f, 16.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, 0.632f, 0.0f, 0.0f));

        partdefinition.addOrReplaceChild(
                "top",
                CubeListBuilder.create()
                        .texOffs(0, 25)
                        .mirror()
                        .addBox(-8.0f, -8.0f, -14.0f, 16.0f, 8.0f, 16.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, -0.632f, 0.0f, 0.0f));

        partdefinition.addOrReplaceChild(
                "mouthbottom",
                CubeListBuilder.create()
                        .texOffs(65, 0)
                        .mirror()
                        .addBox(-7.5f, -2.0f, -13.5f, 15.0f, 2.0f, 15.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, 0.632f, 0.0f, 0.0f));

        partdefinition.addOrReplaceChild(
                "mouthtop",
                CubeListBuilder.create()
                        .texOffs(65, 18)
                        .mirror()
                        .addBox(-7.5f, 0.0f, -13.5f, 15.0f, 2.0f, 15.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, -0.632f, 0.0f, 0.0f));

        partdefinition.addOrReplaceChild(
                "teethbottom1",
                CubeListBuilder.create()
                        .texOffs(65, 36)
                        .mirror()
                        .addBox(-7.0f, -6.0f, -13.0f, 14.0f, 5.0f, 0.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, 0.7436f, 0.0f, 0.0f));

        partdefinition.addOrReplaceChild(
                "teethbottom2",
                CubeListBuilder.create()
                        .texOffs(65, 28)
                        .mirror()
                        .addBox(0.0f, -5.0f, -13.0f, 0.0f, 5.0f, 14.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(-7.0f, 16.0f, 6.0f, 0.7064f, 0.1115f, -0.1487f));

        partdefinition.addOrReplaceChild(
                "teethbottom3",
                CubeListBuilder.create()
                        .texOffs(65, 34)
                        .mirror()
                        .addBox(0.0f, -5.0f, -13.0f, 0.0f, 5.0f, 14.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(7.0f, 16.0f, 6.0f, 0.7064f, -0.1115f, 0.1487f));

        partdefinition.addOrReplaceChild(
                "teethtop1",
                CubeListBuilder.create()
                        .texOffs(95, 36)
                        .mirror()
                        .addBox(-7.0f, -1.0f, -13.0f, 14.0f, 5.0f, 0.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(0.0f, 16.0f, 6.0f, -0.5577f, 0.0f, 0.0f));

        partdefinition.addOrReplaceChild(
                "teethtop2",
                CubeListBuilder.create()
                        .texOffs(95, 28)
                        .mirror()
                        .addBox(0.0f, 0.0f, -13.0f, 0.0f, 5.0f, 14.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(-7.0f, 16.0f, 6.0f, -0.632f, -0.1115f, -0.1859f));

        partdefinition.addOrReplaceChild(
                "teethtop3",
                CubeListBuilder.create()
                        .texOffs(95, 34)
                        .mirror()
                        .addBox(0.0f, 0.0f, -13.0f, 0.0f, 5.0f, 14.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(7.0f, 16.0f, 6.0f, -0.632f, 0.1115f, 0.1859f));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(NosleeptoniteEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        mouthbottom.xRot = (Mth.cos(limbSwing) + 1.0F) * 0.7F * limbSwingAmount;
        mouthtop.xRot = -mouthbottom.xRot;
        bottom.xRot = mouthbottom.xRot;
        teethbottom1.xRot = mouthbottom.xRot;
        teethbottom2.xRot = mouthbottom.xRot;
        teethbottom3.xRot = mouthbottom.xRot;
        top.xRot = mouthtop.xRot;
        teethtop1.xRot = mouthtop.xRot;
        teethtop2.xRot = mouthtop.xRot;
        teethtop3.xRot = mouthtop.xRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
