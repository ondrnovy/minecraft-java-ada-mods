package com.example

import net.fabricmc.api.ClientModInitializer

object TemplateModClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// Register the dash handler for double-tap space detection
		DashHandler.register()
	}
}