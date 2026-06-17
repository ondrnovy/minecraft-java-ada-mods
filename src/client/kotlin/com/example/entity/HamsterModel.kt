package com.example.entity

import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.util.Mth

class HamsterModel(root: ModelPart) : EntityModel<HamsterRenderState>(root) {

    private val body: ModelPart = root.getChild("body")
    private val head: ModelPart = root.getChild("head")
    private val nose: ModelPart = head.getChild("nose")
    private val earLeft: ModelPart = head.getChild("ear_left")
    private val earRight: ModelPart = head.getChild("ear_right")
    private val tail: ModelPart = root.getChild("tail")
    private val legFrontLeft: ModelPart = root.getChild("leg_front_left")
    private val legFrontRight: ModelPart = root.getChild("leg_front_right")
    private val legHindLeft: ModelPart = root.getChild("leg_hind_left")
    private val legHindRight: ModelPart = root.getChild("leg_hind_right")

    override fun setupAnim(state: HamsterRenderState) {
        super.setupAnim(state)

        val ageInTicks = state.ageInTicks
        val walkSpeed = state.walkAnimationSpeed
        val walkPos = state.walkAnimationPos

        // Head rotation: follow look direction
        head.yRot = state.yRot * (Math.PI.toFloat() / 180f)
        head.xRot = state.xRot * (Math.PI.toFloat() / 180f)

        // Leg walk cycle
        val legSwing = Mth.cos((walkPos * 0.6662f).toDouble()) * 1.4f * walkSpeed
        legFrontLeft.xRot = legSwing
        legFrontRight.xRot = -legSwing
        legHindLeft.xRot = -legSwing
        legHindRight.xRot = legSwing

        // Tail wiggle
        tail.yRot = Mth.sin((ageInTicks * 0.3f).toDouble()) * 0.3f

        // Ear ambient sway
        val earSway = Mth.sin((ageInTicks * 0.1f).toDouble()) * 0.05f
        earLeft.zRot = -0.2f + earSway
        earRight.zRot = 0.2f - earSway

        // Ear twitch (periodic)
        if (state.earTwitchTimer > 0f) {
            val twitchAmount = Mth.sin((state.earTwitchTimer * 1.5f).toDouble()) * 0.3f
            earLeft.zRot += twitchAmount
            earRight.zRot -= twitchAmount
        }

        // Baby scaling: bigger head
        if (state.isBaby) {
            head.xScale = 1.4f
            head.yScale = 1.4f
            head.zScale = 1.4f
        }
    }

    companion object {
        fun createBodyLayer(): LayerDefinition {
            val meshDefinition = MeshDefinition()
            val partDefinition = meshDefinition.root

            // Body: chubby oval, 8x6x6
            partDefinition.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                    .texOffs(0, 0)
                    .addBox(-4.0f, -3.0f, -3.0f, 8.0f, 6.0f, 6.0f),
                PartPose.offset(0.0f, 20.0f, 0.0f)
            )

            // Head: 5x5x4, attached at front of body
            val headPart = partDefinition.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                    .texOffs(28, 0)
                    .addBox(-2.5f, -2.5f, -4.0f, 5.0f, 5.0f, 4.0f),
                PartPose.offset(0.0f, 18.5f, -3.0f)
            )

            // Nose: tiny bump on face
            headPart.addOrReplaceChild(
                "nose",
                CubeListBuilder.create()
                    .texOffs(46, 0)
                    .addBox(-0.5f, 0.0f, -1.0f, 1.0f, 1.0f, 1.0f),
                PartPose.offset(0.0f, 0.5f, -4.0f)
            )

            // Left ear
            headPart.addOrReplaceChild(
                "ear_left",
                CubeListBuilder.create()
                    .texOffs(50, 0)
                    .addBox(-1.0f, -2.0f, 0.0f, 2.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(-1.5f, -2.5f, -2.0f, 0.0f, 0.0f, -0.2f)
            )

            // Right ear
            headPart.addOrReplaceChild(
                "ear_right",
                CubeListBuilder.create()
                    .texOffs(56, 0)
                    .addBox(-1.0f, -2.0f, 0.0f, 2.0f, 2.0f, 1.0f),
                PartPose.offsetAndRotation(1.5f, -2.5f, -2.0f, 0.0f, 0.0f, 0.2f)
            )

            // Tail: small nub at back
            partDefinition.addOrReplaceChild(
                "tail",
                CubeListBuilder.create()
                    .texOffs(46, 4)
                    .addBox(-0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 2.0f),
                PartPose.offset(0.0f, 19.5f, 3.0f)
            )

            // Front left leg (short)
            partDefinition.addOrReplaceChild(
                "leg_front_left",
                CubeListBuilder.create()
                    .texOffs(0, 12)
                    .addBox(-1.0f, 0.0f, -1.0f, 2.0f, 2.0f, 2.0f),
                PartPose.offset(-2.5f, 22.0f, -2.0f)
            )

            // Front right leg (short)
            partDefinition.addOrReplaceChild(
                "leg_front_right",
                CubeListBuilder.create()
                    .texOffs(8, 12)
                    .addBox(-1.0f, 0.0f, -1.0f, 2.0f, 2.0f, 2.0f),
                PartPose.offset(2.5f, 22.0f, -2.0f)
            )

            // Hind left leg (slightly longer)
            partDefinition.addOrReplaceChild(
                "leg_hind_left",
                CubeListBuilder.create()
                    .texOffs(16, 12)
                    .addBox(-1.0f, 0.0f, -1.0f, 2.0f, 3.0f, 2.0f),
                PartPose.offset(-2.5f, 21.0f, 2.0f)
            )

            // Hind right leg (slightly longer)
            partDefinition.addOrReplaceChild(
                "leg_hind_right",
                CubeListBuilder.create()
                    .texOffs(24, 12)
                    .addBox(-1.0f, 0.0f, -1.0f, 2.0f, 3.0f, 2.0f),
                PartPose.offset(2.5f, 21.0f, 2.0f)
            )

            return LayerDefinition.create(meshDefinition, 64, 32)
        }
    }
}
