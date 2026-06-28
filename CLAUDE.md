# template-mod

A **Fabric mod for Minecraft 1.21.11**, written in **Kotlin** (Fabric Loom).
Package root `com.example`, mod id `template-mod`.

- Items are registered in `src/main/kotlin/com/example/registry/ModItems.kt`.
- Entities are registered in `src/main/kotlin/com/example/entity/ModEntities.kt`.
- Networking lives under `com/example/network/`; client-only logic under
  `src/client/kotlin/com/example/`.
- Assets/data live under `src/main/resources/(assets|data)/template-mod/`.
- To launch the game and verify changes, use the **`run-client`** skill
  (`.claude/skills/run-client/SKILL.md`).

## ⚠️ Keep the changelog updated

**Whenever you add, change, or remove in-game content (items, blocks, entities,
mechanics, recipes), update the "Changelog" section below in the same change.**
Add a dated entry describing what a player would notice. This log is the
human-readable record of what the mod adds to the game — keep it current.

## Changelog

Newest first. Dates are when the content was added.

### 2026-06-28

- **Red Diamond** — a new gem crafted from a diamond + red dye (shapeless).
  Acts as a full crafting material tier **stronger than diamond and netherite**.
  - **Tools:** Red Diamond sword, pickaxe, axe, shovel, and hoe (durability
    2500, mining speed 9.0, +5 attack damage bonus, enchantability 18; can mine
    everything a netherite tool can).
  - **Armor:** Red Diamond helmet, chestplate, leggings, and boots (better
    protection and toughness 4.0 than diamond; renders red on the player).
  - Tools and armor are repairable with the Red Diamond gem.
  - Textures are recolored from the vanilla diamond assets.
- **Hamster eat ability** — while riding a tamed Hamster, hold **H** to make it
  eat the block in front of it. The block is destroyed (no drops) with break
  particles and an eating sound, and the Hamster heals 4 HP. Server-validated
  (ownership checked; air and indestructible blocks are skipped); 0.5s cooldown.

### 2026-06-17

- **Hamster (item)** — a weapon dealing ~7 base damage. Landing a **second
  consecutive hit on the same target adds +50,000 bonus damage**; if the target
  retaliates (hits the attacker back), the combo counter resets. Durability 500.
- **Hamster (mob)** — a custom mob entity with an animated model and a spawn
  egg. **Tameable and rideable**, with a custom spawn-egg texture and saddle
  support.
- **Rainbow Spear crafting recipe** — the Rainbow Spear can now be crafted
  (3×3 of dyes + diamond + stick).

### 2026-01-20

- **Rainbow Spear** — a melee weapon: 10 total attack damage, ~1.1 attacks/sec,
  durability 500. Has a **double-tap dash** ability (double-tap the movement/jump
  key to dash). Originally had a throw ability; later changed to melee-only.
- **Stick of Sheep** — right-click to **rain a sheep** down from ~10 blocks above
  you (spawned at a random spot in a 10×10 area). Costs 1 durability per use;
  durability 64.
