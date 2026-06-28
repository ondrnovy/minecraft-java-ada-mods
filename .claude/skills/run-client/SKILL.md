---
name: run-client
description: Launch this Fabric mod in the Minecraft client (runClient) to verify changes in-game. Use when asked to run, start, launch, or test the mod in Minecraft.
---

# Run the Minecraft client

This is a **Fabric mod for Minecraft 1.21.11**, written in Kotlin (Fabric Loom
build). "Running" it means launching the actual Minecraft client with the mod
loaded via Loom's `runClient` task, then letting the user (or you) interact with
it. The client is a GUI that stays open until closed; a clean run ends with
`Stopping!` and exit code `0`.

## Quick compile check first (optional, fast)

Before a full launch, catch API/Kotlin errors in ~20s:

```
.\gradlew.bat compileKotlin --console=plain
```

`BUILD SUCCESSFUL` = the mod code compiles. This does **not** validate
JSON resources (models, recipes, tags) — those are only checked at runtime
by the client.

## Launch the client

Run from the project root (PowerShell). It takes ~1–2 min the first time
(asset download + game boot):

```
.\gradlew.bat runClient --console=plain
```

Launch it with `run_in_background: true` (the client stays open until closed),
then watch the **task output file** the background tool reports — read that path
directly with the Read tool.

> ⚠️ Do **not** `Tee-Object` the output to a `.log` file and then grep it:
> PowerShell writes that file as **UTF-16**, so `grep`/`rg` see null bytes
> between every character and match nothing. Read the background task's own
> output file instead (it's plain text), or use
> `Get-Content file -Encoding Unicode` if you must tee.

## Confirm it actually loaded

In the boot log, verify:

- The mod list includes `template-mod 1.0.0` (Fabric loads it).
- `(Minecraft) Hello Fabric world!` — `TemplateMod.onInitialize` ran.
- No `Exception`, `Failed to load`, or missing-texture/recipe warnings for
  `template-mod:` resources.

Reaching the title screen (look for `Sound engine started` / `Backend library:
LWJGL`) means assets loaded. A clean shutdown logs `Stopping!` then
`BUILD SUCCESSFUL` with exit code `0` — that's the user closing the window, not a
crash.

## Driving it to actually see a change

`runClient` only opens the menu. To verify added content (items, recipes,
entities) you need to get into a world:

1. Create a new Creative world (Singleplayer → Create New World → Game Mode:
   Creative).
2. Open the inventory / creative search and type the item name (e.g.
   `Red Diamond`) to confirm the item, texture, and tooltip name.
3. For recipes, use a crafting table; for armor, equip it and check the worn
   texture renders on the player model.

You cannot click through the GUI from the CLI here — ask the user to do the
in-world check, or describe exactly what they should look for.

## If the launch needed extra setup

If a future run requires new env vars, JVM args, a JDK switch, or patches that
aren't captured here, update this file so the next run just works.
