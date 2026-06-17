package com.example

import com.example.entity.ModEntities
import com.example.item.HamsterItem
import com.example.network.DashPacketHandler
import com.example.registry.ModItems
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.world.entity.LivingEntity
import org.slf4j.LoggerFactory

object TemplateMod : ModInitializer {
    private val logger = LoggerFactory.getLogger("template-mod")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

		// Register entities
		ModEntities.register()

		// Register items
		ModItems.register()

		// Register network packets
		DashPacketHandler.register()

		// Register retaliation detection for Hamster item
		ServerLivingEntityEvents.AFTER_DAMAGE.register { target, source, _, _, _ ->
			val attacker = source.entity
			if (attacker is LivingEntity) {
				// Target hit attacker back — reset attacker's consecutive hit counter
				HamsterItem.onTargetRetaliates(target.uuid, attacker.uuid)
			}
		}
	}
}