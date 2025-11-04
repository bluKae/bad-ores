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
import de.blukae.badores.entity.FleesonsiteEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class FleesonsiteEntityModel extends EntityModel<FleesonsiteEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer
    // and passed into this model's constructor
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BadOres.rl("fleesonsite"), "main");

    private final ModelPart root;

    private final ModelPart block;
    private final ModelPart footright;
    private final ModelPart toeright1;
    private final ModelPart toeright2;
    private final ModelPart toeright3;
    private final ModelPart footleft;
    private final ModelPart toeleft1;
    private final ModelPart toeleft2;
    private final ModelPart toeleft3;

    protected FleesonsiteEntityModel(ModelPart root) {
        this.root = root;

        block = root.getChild("block");
        footright = root.getChild("footright");
        toeright1 = root.getChild("toeright1");
        toeright2 = root.getChild("toeright2");
        toeright3 = root.getChild("toeright3");
        footleft = root.getChild("footleft");
        toeleft1 = root.getChild("toeleft1");
        toeleft2 = root.getChild("toeleft2");
        toeleft3 = root.getChild("toeleft3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild(
                "block",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-8.0f, 0.0f, -6.0f, 16.0f, 16.0f, 16.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(0.0f, 5.0f, 0.0f, -0.1115f, 0.0f, 0.0f));

        partdefinition.addOrReplaceChild(
                "footright",
                CubeListBuilder.create()
                        .texOffs(0, 33)
                        .mirror()
                        .addBox(-3.0f, 0.0f, -3.0f, 6.0f, 4.0f, 6.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.0f, 0.3718f, 0.0f));

        partdefinition.addOrReplaceChild(
                "toeright1",
                CubeListBuilder.create()
                        .texOffs(0, 44)
                        .mirror()
                        .addBox(-1.5f, 0.0f, -8.0f, 4.0f, 4.0f, 7.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.2231f, 0.0721f, 0.0f));

        partdefinition.addOrReplaceChild(
                "toeright2",
                CubeListBuilder.create()
                        .texOffs(70, 0)
                        .mirror()
                        .addBox(-2.0f, 0.5f, -7.5f, 3.0f, 3.0f, 5.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.2231f, 0.4811f, 0.0f));

        partdefinition.addOrReplaceChild(
                "toeright3",
                CubeListBuilder.create()
                        .texOffs(70, 9)
                        .mirror()
                        .addBox(-2.5f, 0.5f, -6.0f, 3.0f, 3.0f, 5.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(-4.0f, 20.0f, 0.0f, 0.2231f, 0.9666f, 0.0f));

        partdefinition.addOrReplaceChild(
                "footleft",
                CubeListBuilder.create()
                        .texOffs(25, 33)
                        .mirror()
                        .addBox(-3.0f, 0.0f, -3.0f, 6.0f, 4.0f, 6.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.0f, -0.3718f, 0.0f));

        partdefinition.addOrReplaceChild(
                "toeleft1",
                CubeListBuilder.create()
                        .texOffs(25, 44)
                        .mirror()
                        .addBox(-2.5f, 0.0f, -8.0f, 4.0f, 4.0f, 7.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.2231f, -0.0721f, 0.0f));

        partdefinition.addOrReplaceChild(
                "toeleft2",
                CubeListBuilder.create()
                        .texOffs(90, 0)
                        .mirror()
                        .addBox(-1.0f, 0.5f, -7.5f, 3.0f, 3.0f, 5.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.2231f, -0.481f, 0.0f));

        partdefinition.addOrReplaceChild(
                "toeleft3",
                CubeListBuilder.create()
                        .texOffs(90, 9)
                        .mirror()
                        .addBox(-0.5f, 0.5f, -6.0f, 3.0f, 3.0f, 5.0f, new CubeDeformation(0.0f))
                        .mirror(false),
                PartPose.offsetAndRotation(4.0f, 20.0f, 0.0f, 0.2231f, -0.9667f, 0.0f));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(FleesonsiteEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        block.yRot = Mth.cos(limbSwing * 0.7f) * 1.5f * limbSwingAmount;
        footright.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        toeright1.xRot = footright.xRot;
        toeright2.xRot = footright.xRot;
        toeright3.xRot = footright.xRot;
        footleft.xRot = Mth.cos(limbSwing * 0.6662f + (float) Math.PI) * 1.4f * limbSwingAmount;
        toeleft1.xRot = footleft.xRot;
        toeleft2.xRot = footleft.xRot;
        toeleft3.xRot = footleft.xRot;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
