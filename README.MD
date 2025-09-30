[//]: # (# PumpkinNPCLib vBETA-1.0<br>&#40;29.09.2025 21:55 CEST&#41;)

[//]: # ()
[//]: # (## Made by plugin developer for plugin developers!<br>Easy to use and lightweight based in 90% on ProtocolLib.)

[//]: # ()
[//]: # (### To install on your computer just:)

[//]: # (    mvn clean)

[//]: # (    mvn compile)

[//]: # (    mvn package)

[//]: # (    mvn install:install-file -Dfile=Path\To\Project\PumpkinNPCLib\target\PumpkinNPCLib-BETA-1.0.jar -DgroupId=pl.imbestpumpkin -DartifactId=PumpkinNPCLib -Dversion=BETA-1.0 -Dpackaging=jar)

[//]: # (***)

[//]: # (### Start using:)

[//]: # (        <dependency>)

[//]: # (            <groupId>pl.imbestpumpkin</groupId>)

[//]: # (            <artifactId>PumpkinNPCLib</artifactId>)

[//]: # (            <version>BETA-1.0</version>)

[//]: # (            <scope>compile</scope>)

[//]: # (        </dependency>)

# PumpkinNPCLib vBETA-1.0

## I've made this because there is not a good NPC Library for 1.21+, PumpkinNPCLib should work on 1.19-1.21.8.
***
### Installation:
#### 1. Download the file from releases.
#### 2. Open IntelliJ IDEA then open your project.
#### 3. Click CTRL two times fast.
#### 4. Paste this command (DON'T FORGET TO CHANGE THE PATH):
        mvn install:install-file -Dfile=C:\Your\Path\To\The\File\PumpkinNPCLib-BETA-1.0.jar -DgroupId=pl.imbestpumpkin -DartifactId=PumpkinNPCLib -Dversion=BETA-1.0 -Dpackaging=jar
#### 5. It is installed locally on your computer, and it will be available in every project.
#### 6. Add the dependency to your project:
````xml
<dependency>
    <groupId>pl.imbestpumpkin</groupId>
    <artifactId>PumpkinNPCLib</artifactId>
    <version>BETA-1.0</version>
    <scope>compile</scope>
</dependency>
````
#### 7. If you did everything correctly, maven should reload without any problems.
***
### Start using:
#### YOU MUST HAVE INSTALLED https://github.com/dmulloy2/ProtocolLib ON YOUR SERVER, HOWEVER YOU DON'T HAVE TO ADD ProtocolLib TO YOUR PROJECT DEPENDENCIES
#### Creating NPC object:
````java
NPC npc = NPC.newNpcBuilder()
      .setName("testnpc")           // Set name for your NPC
      .setUuid(UUID.randomUUID())   // uuid should be random
      .setEntityId(-1)              // entityId must be UNIQUE, I recomend using negative numbers
      .setLocation(p.getLocation()) // NPC location
      .legacy()                     // ADD THIS IF YOU ARE USING VERSION 1.19.X
      .build();  
````
***
#### Spawn NPC for a player:
````java
npc.spawnNpc(
      player,          // Bukkit Player
      pluginInstance   // JavaPlugin instance
);
````
***
#### How you make your plugin instance accessible from other classes:
````java
public final class YourPluginMain extends JavaPlugin {
    private static JavaPlugin instance;

    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }
}
````
***
#### Interaction example:
````java
NPC npc = NPC.newNpcBuilder()
        .setName("testnpc")
        .setUuid(uuid)
        .setEntityId(-1)
        .setLocation(p.getLocation())
        .interaction((player, npcId, action, hand, shift) -> {
            if (npcId != -1) {
                return;
            }
            if (action.equals(WrappedEntityUseAction.INTERACT) && hand.equals(WrappedHand.MAIN_HAND)) {
                if (shift) {
                    player.sendMessage("This is interaction with shift!");
                } else {
                    player.sendMessage("This is interaction!");
                }
            }
        })
        .build();
````
***
#### Make it <i>GLOW</i>:
````java
NPC npc = NPC.newNpcBuilder()
        .setName("testnpc")
        .setUuid(uuid)
        .setEntityId(-1)
        .setLocation(p.getLocation())
        .glow() // Thats it
        .build();
````
***
#### Change color of the glow:
````java
NPC npc = NPC.newNpcBuilder()
        .setName("testnpc")
        .setUuid(uuid)
        .setEntityId(-1)
        .setLocation(p.getLocation())
        .glow()
        .setColor(ChatColor.RED) // Just choose from the ChatColor enum
        .build();
````
***
#### Hide nickname:
````java
NPC npc = NPC.newNpcBuilder()
        .setName("testnpc")
        .setUuid(uuid)
        .setEntityId(-1)
        .setLocation(p.getLocation())
        .hideNickname()
        .build();
````
***
#### Disable collisions:
````java
NPC npc = NPC.newNpcBuilder()
        .setName("testnpc")
        .setUuid(uuid)
        .setEntityId(-1)
        .setLocation(p.getLocation())
        .collissionless()
        .build();
````
***
#### Set skin:
````java
// I recomend using https://mineskin.org/
// Just upload your skin
// Pase here skinValue and skinSignature
NPC npc = NPC.newNpcBuilder()
        .setName("testnpc")
        .setUuid(uuid)
        .setEntityId(-1)
        .setLocation(p.getLocation())
        .setSkin(
            "skinValue here",
            "skinSignature here"
        )
        .build();
````
***
#### Set skin example:
````java
NPC npc = NPC.newNpcBuilder()
        .setName("testnpc")
        .setUuid(uuid)
        .setEntityId(-1)
        .setLocation(p.getLocation())
        .setSkin(
                "ewogICJ0aW1lc3RhbXAiIDogMTc1ODM4MTE0NzE4NCwKICAicHJvZmlsZUlkIiA6ICJhODc1ZTI3NjZjOTc0N2Y5OTM3YzBmMzNhMWQ3N2JmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJzcGlmZnRvcGlhMTAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjE5YjAwZWNlMzNkNjE3MTQzMTk5NzZhZjY0MjJiNTJlNDRjMmViNzAyY2M4NWNjZmQ3OWQyMTk4MGI4YWYyYSIKICAgIH0KICB9Cn0=",
                "AcDlvzoqqfpC3SUPLbjHA2Jd1m5q67IUgaducwZ/m4HjHkA5rc1zphSFLeeG9GUtUlr+tUmka8hvbWDPmQhcUYhySCJJ4rssYgYqVzkub1nx935whkGfJVa0kHSsxID/qk00G6tDGBj3F5O1uDaF6kMUTP1qgCd5B187wBxVRr8s9HfelCvpedr/sv3ivv5yTgQZ6D2AhCtKZf0WufKmrEW9/aS16vIYxlAo9QAntZon4T4noevShOtzvODdQhYX37APIQXqF/WSV9QYkeiNGtBPvjx6H2MstIPAnGaPvwm3h8TywBiJWdjpmmnk3g8egLzhcuPVqlENQtp2NVUqfZ7DTBDD4A+PBrEg4yVlFbsp0q3OtcjS5oB4GS/iUEX9yJVyd89UgP623iV7HrJ64D9/PSSu6+K3bzOsJq3tDqHgOC3RL3yAR2uWryAU5ymAhdWsJw+n0smyjRqmeEg0SeCJEwPGKVX5jCEmVocdiecBEvSPpk4FZcS7nKxg6Vty6cAUypclqegZMWVH21pTn2X6hGMuVrzT1NofbvHjTWQ10Ti5C2eNJNDnXyqQJFBf0ai9F5vQowZvoLuWtsGtJrQwMzPnUKkp0AhVGHzT9bRKsXCqj+usG5VqwpFJODMGAL6k7sPAQlo73UrPkrWHxC0kG+Q1683YWuE1bX4EDeg="
                )
        .build();
````
***
### NPCUtils class with static methods

#### Send player to the server connected to the proxy (only BungeeCord based proxies):
````java
NPCUtils.sendPlayerToVelocityServer(targetPlayer, serverName, yourPluginInstance);
````
***
#### NPC interaction outside of NPC builder:
````java
NPCUtils.npcInteraction(yourPluginInstance, (player, npcId, action, hand, shift) -> {
    if (npcId != -1){
        return;
    }
    if (action.equals(WrappedEntityUseAction.INTERACT) && hand.equals(WrappedHand.MAIN_HAND)) {
        if (shift) {
            player.sendMessage("This is interaction with shift!");
        } else {
            player.sendMessage("This is interaction!");
            }.runTaskLater(Testing.instance, 10L);
        }
    }
});
````
***
#### Send team packet:
````java
/*
* Nametag visibility:
* always
* never
* hideForOtherTeams
* hideForOwnTeam
*
* Collisions:
* always
* never
* pushOtherTeams
* pushOwnTeam
*/
NPCUtils.sendTeamPacket(targetPlayer, npc.getUuid(), "testnpc", "never", "never", ChatColor.RED);
````
***
#### Send glow packet:
````java
NPCUtils.sendGlowPacket(targetPlayer, npc.getEntityId());
````
***
#### Send animation packet:
````java
/*
 * Animations for player:
 * 0 | Swing main arm
 * 2 | Leave bed
 * 3 | Swing offhand
 * 4 | Critical effect
 * 5 | Magic critical effect
 */
NPCUtils.sendAnimationPacket(targetPlayer, npc.getEntityId(), 0);
````
***
#### Change pose packet:
````java
/*
 * Poses for player:
 * STANDING
 * SLEEPING
 * SWIMMING
 * CROUCHING
 */
NPCUtils.sendChangePosePacket(targetPlayer, npc.getEntityId(), WrappedPose.CROUCHING);
````
***
#### Change NPC position and rotation packet:
````java
// This will play walk animation, and can be used only to change position up to 8 blocks.
NPCUtils.sendUpdatePositionAndRotationPacket(targetPlayer, npc.getEntityId(), yaw, pitch, nextX, nextY, nextZ, nowX, nowY, nowZ);
````
***
#### EXPERIMENTAL Simulate walking:
````java
/*
* Walking directions:
* NORTH
* NORTH_WEST
* NORTH_EAST
* WEST
* EAST
* SOUTH_WEST
* SOUTH_EAST
* SOUTH
*/
NPCUtils.npcWalk(targetPlayer, yourPluginInstance, NPC npc, NpcWalkDirection.NORTH, 0, 0.30 /*Feels like kinda normal walking speed*/, 30 /*Max steps*/);
````

        
