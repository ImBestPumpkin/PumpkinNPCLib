# PumpkinNPCLib vBETA-1.0<br>(29.09.2025 21:55 CEST)

## I've made this because there is not a good NPC Library for 1.21+, PumpkinNPCLib should work on 1.19-1.21.8.

### Here is how you should create your NPC:
        NPC npc = NPC.newNpcBuilder()
              .setName("testnpc")
              .setUuid(uuid)
              .setEntityId(-1)
              .setLocation(p.getLocation())
              .build()
### Spawn NPC for a player:
        npc.spawnNpc(
              player,
              pluginInstance
        );
### Interaction example:
        NPC npc = NPC.newNpcBuilder()
                .setName("testnpc")
                .setUuid(uuid)
                .setEntityId(-1)
                .setLocation(p.getLocation())
                .interaction((player, npcId, action, hand, shift) -> {
                    if (npcId == -1 && action.equals(WrappedEntityUseAction.INTERACT) && hand.equals(WrappedHand.MAIN_HAND)) {
                        if (shift) {
                            player.sendMessage("This is interaction with shift!");
                        } else {
                            player.sendMessage("This is interaction!");
                            }.runTaskLater(Testing.instance, 10L);
                        }
                    }
                })
                .build();
### Make it <b>GLOW</b>!

