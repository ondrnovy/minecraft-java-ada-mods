package com.example.entity

import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.resources.Identifier

class HamsterRenderer(context: EntityRendererProvider.Context) :
    MobRenderer<HamsterEntity, HamsterRenderState, HamsterModel>(
        context,
        HamsterModel(context.bakeLayer(ModModelLayers.HAMSTER_MOB)),
        0.3f
    ) {

    private var earTwitchCooldown: Int = 0
    private var earTwitchTimer: Int = 0

    override fun getTextureLocation(state: HamsterRenderState): Identifier {
        return TEXTURE
    }

    override fun createRenderState(): HamsterRenderState {
        return HamsterRenderState()
    }

    override fun extractRenderState(entity: HamsterEntity, state: HamsterRenderState, partialTick: Float) {
        super.extractRenderState(entity, state, partialTick)

        // Ear twitch logic: twitch for 10 ticks every ~80 ticks
        if (earTwitchCooldown <= 0) {
            earTwitchTimer = 10
            earTwitchCooldown = 70 + entity.random.nextInt(20)
        } else {
            earTwitchCooldown--
        }

        if (earTwitchTimer > 0) {
            state.earTwitchTimer = earTwitchTimer.toFloat() - partialTick
            earTwitchTimer--
        } else {
            state.earTwitchTimer = 0f
        }
    }

    companion object {
        private val TEXTURE = Identifier.fromNamespaceAndPath("template-mod", "textures/entity/hamster_mob.png")
    }
}
