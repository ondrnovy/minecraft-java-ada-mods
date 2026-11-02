# minecraft-java-ada-mods

A **Fabric mod for Minecraft Java Edition 1.21.11**, written in Kotlin. My son invents the items and
mechanics, an AI coding agent (Claude Code) writes the code, and I steer and review. Built on Windows.

## What it adds

| Content | What it does |
|---------|--------------|
| **Red Diamond** | Gem crafted from a diamond and red dye. Full tool and armor set stronger than netherite. |
| **Hamster** (mob) | Animated mob with a spawn egg. Tameable and rideable; hold **H** while riding to make it eat the block in front of it and heal. |
| **Hamster** (weapon) | A second consecutive hit on the same target deals +50,000 bonus damage. |
| **Rainbow Spear** | Melee weapon with a double-tap dash. Craftable. |
| **Stick of Sheep** | Right-click to rain a sheep down from the sky. |

Full, dated list in the changelog in [`CLAUDE.md`](CLAUDE.md#changelog).

## How it is built with an agent

- [`CLAUDE.md`](CLAUDE.md) gives the agent the project map (where items, entities, networking and assets
  live) and one standing rule: every change to game content updates the changelog in the same commit.
- [`run-client` skill](.claude/skills/run-client/SKILL.md) teaches the agent to compile, launch the game
  and read the boot log to confirm the mod loaded without errors. It also records what went wrong in past
  runs (a PowerShell UTF-16 log trap) so the next run does not repeat it.
- Key-triggered abilities (dash, hamster eating) are sent from the client to the server as packets
  (`network/`); before a hamster eats a block, the server checks that the player owns it and that the
  block can be broken.
- GitHub Actions builds every push.

## Build and run

Requires JDK 21.

```
./gradlew build        # jar in build/libs/
./gradlew runClient    # start Minecraft with the mod loaded
```

On Windows use `.\gradlew.bat`.

## License

CC0 1.0, see [LICENSE](LICENSE).
