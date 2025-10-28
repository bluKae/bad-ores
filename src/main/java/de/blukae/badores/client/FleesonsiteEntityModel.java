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

import de.blukae.badores.BadOres;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class FleesonsiteEntityModel extends EntityModel<FleesonsiteEntityRenderer.State> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer
    // and passed into this model's constructor
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(BadOres.rl("fleesonsite"), "main");

    private final ModelPart block = root.getChild("block");
    private final ModelPart footright = root.getChild("footright");
    private final ModelPart toeright1 = root.getChild("toeright1");
    private final ModelPart toeright2 = root.getChild("toeright2");
    private final ModelPart toeright3 = root.getChild("toeright3");
    private final ModelPart footleft = root.getChild("footleft");
    private final ModelPart toeleft1 = root.getChild("toeleft1");
    private final ModelPart toeleft2 = root.getChild("toeleft2");
    private final ModelPart toeleft3 = root.getChild("toeleft3");

    protected FleesonsiteEntityModel(ModelPart root) {
        super(root);
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
    public void setupAnim(FleesonsiteEntityRenderer.State renderState) {
        super.setupAnim(renderState);

        float pos = renderState.walkAnimationPos;
        float speed = renderState.walkAnimationSpeed;

        block.yRot = 5f + Mth.cos(pos * 1.4f) * 2.5f * speed;
        footright.xRot = Mth.cos(pos * 0.6662f) * 1.4f * speed;
        toeright1.xRot = footright.xRot;
        toeright2.xRot = footright.xRot;
        toeright3.xRot = footright.xRot;
        footleft.xRot = Mth.cos(pos * 0.6662f + (float) Math.PI) * 1.4f * speed;
        toeleft1.xRot = footleft.xRot;
        toeleft2.xRot = footleft.xRot;
        toeleft3.xRot = footleft.xRot;
    }
}
