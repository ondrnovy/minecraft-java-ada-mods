package com.example.item

import net.minecraft.world.item.Item

class RainbowSpearItem(properties: Properties) : Item(properties) {
    // The dash ability is handled through client-side double-tap detection
    // and server-side packet handling, not through item use actions
}
