package com.example

import com.example.entity.HamsterModel
import com.example.entity.HamsterRenderer
import com.example.entity.ModEntities
import com.example.entity.ModModelLayers
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry
import net.minecraft.client.renderer.entity.EntityRenderers

object TemplateModClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// Register the dash handler for double-tap space detection
		DashHandler.register()

		// Register hamster eat handler (H key while riding)
		HamsterEatHandler.register()

		// Register hamster mob model layer and renderer
		EntityModelLayerRegistry.registerModelLayer(ModModelLayers.HAMSTER_MOB) {
			HamsterModel.createBodyLayer()
		}
		EntityRenderers.register(ModEntities.HAMSTER_MOB) { context ->
			HamsterRenderer(context)
		}
	}
}
