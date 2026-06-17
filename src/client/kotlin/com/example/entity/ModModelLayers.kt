package com.example.entity

import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.resources.Identifier

object ModModelLayers {
    val HAMSTER_MOB: ModelLayerLocation = ModelLayerLocation(
        Identifier.fromNamespaceAndPath("template-mod", "hamster_mob"), "main"
    )
}
