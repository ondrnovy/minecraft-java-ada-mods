package com.example.registry

import com.example.item.RainbowSpearItem
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item

object ModItems {
    val RAINBOW_SPEAR: Item = registerItem("rainbow_spear") { key ->
        RainbowSpearItem(Item.Properties().stacksTo(1).setId(key))
    }

    private fun registerItem(name: String, factory: (ResourceKey<Item>) -> Item): Item {
        val id = Identifier.fromNamespaceAndPath("template-mod", name)
        val key = ResourceKey.create(Registries.ITEM, id)
        val item = factory(key)
        return Registry.register(BuiltInRegistries.ITEM, key, item)
    }

    fun register() {
        // Called to trigger static initialization
    }
}
