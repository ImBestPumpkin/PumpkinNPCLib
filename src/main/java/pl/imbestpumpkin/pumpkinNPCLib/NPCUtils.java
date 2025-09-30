package pl.imbestpumpkin.pumpkinNPCLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.*;

import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.apache.http.annotation.Experimental;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.imbestpumpkin.pumpkinNPCLib.Wrapper.WrappedEntityUseAction;
import pl.imbestpumpkin.pumpkinNPCLib.Wrapper.WrappedHand;
import pl.imbestpumpkin.pumpkinNPCLib.Wrapper.WrappedPose;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

public class NPCUtils {

    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private static void sendPlayerToVelocityServer(Player target, String serverName, JavaPlugin plugin) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(serverName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        target.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

    /*
     * Interactions:
     * Hand:
     * OFF_HAND
     * MAIN_HAND
     * Type:
     * ATTACK
     * INTERACT
     * INTERACT_AT
     */
    public static void npcInteraction(JavaPlugin plugin, NpcClickHandler handler) {
        ProtocolLibrary.getProtocolManager().addPacketListener(
            new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    PacketContainer packet = event.getPacket();
                    WrappedEnumEntityUseAction actionEnum = WrappedEnumEntityUseAction.fromHandle(packet.getModifier().readSafely(1));
                    WrappedHand hand = null;
                    WrappedEntityUseAction actionType = WrappedEntityUseAction.fromProtocolAction(actionEnum.getAction());
                    try {
                        hand = WrappedHand.fromProtocolHand(actionEnum.getHand());
                    } catch (Exception ignored) {}

                    handler.handle(
                            event.getPlayer(),
                            packet.getIntegers().readSafely(0),
                            actionType,
                            ((hand == null) ? WrappedHand.MAIN_HAND : hand),
                            packet.getBooleans().readSafely(0)
                    );
                }
            }
        );
    }
    /*
    * Colors:
    * BLACK
    * DARK_BLUE
    * DARK_GREEN
    * DARK_AQUA
    * DARK_RED
    * DARK_PURPLE
    * GOLD
    * GRAY
    * DARK_GRAY
    * BLUE
    * GREEN
    * AQUA
    * RED
    * LIGHT_PURPLE
    * YELLOW
    * WHITE
    *
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
    public static void sendTeamPacket(Player receiver, UUID uuid, String nameOfNpc, String nicknameVisibility, String collisions, ChatColor color) {
        PacketContainer createTeam = protocolManager.createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        createTeam.getIntegers().write(0, 0);
        createTeam.getStrings().write(0, uuid.toString());

        createTeam.getSpecificModifier(Collection.class).write(0, List.of(nameOfNpc));

        createTeam.getOptionalTeamParameters().write(0, Optional.of(
                        WrappedTeamParameters.newBuilder()
                                .displayName(WrappedChatComponent.fromLegacyText(uuid.toString()))
                                .color(EnumWrappers.ChatFormatting.fromBukkit(color))
                                .nametagVisibility(nicknameVisibility)
                                .collisionRule(collisions)
                                .prefix(WrappedChatComponent.fromLegacyText(""))
                                .suffix(WrappedChatComponent.fromLegacyText(""))
                                .build()
                )
        );
        protocolManager.sendServerPacket(receiver, createTeam);
    }

    public static void sendGlowPacket(Player receiver, int entityId) {
        PacketContainer glowEffectPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        glowEffectPacket.getIntegers().write(0, entityId);

        SynchedEntityData.DataValue dataValue = new SynchedEntityData.DataValue(0, EntityDataSerializers.BYTE, ((byte) 64));
        glowEffectPacket.getModifier().write(1, List.of(dataValue));
        protocolManager.sendServerPacket(receiver, glowEffectPacket);
    }


    public static void sendRemovePlayerInfoDataPacket(Player receiver, UUID uuid) {
        try {
            PacketContainer remove = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
            remove.getUUIDLists().write(0, Collections.singletonList(uuid));
            protocolManager.sendServerPacket(receiver, remove);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Animations for player:
     * 0 | Swing main arm
     * 2 | Leave bed
     * 3 | Swing offhand
     * 4 | Critical effect
     * 5 | Magic critical effect
     */
    public static void sendAnimationPacket(Player receiver, int entityId, int animation) {
        PacketContainer test = protocolManager.createPacket(PacketType.Play.Server.ANIMATION);
        test.getIntegers().write(0, entityId);
        test.getIntegers().write(1, animation);
        protocolManager.sendServerPacket(receiver, test);
    }
    /*
     * Poses for player:
     * STANDING
     * SLEEPING
     * SWIMMING
     * CROUCHING
     */
    public static void sendChangePosePacket(Player receiver, int entityId, WrappedPose pose) {
        PacketContainer glowEffectPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        glowEffectPacket.getIntegers().write(0, entityId);

        SynchedEntityData.DataValue dataValue = new SynchedEntityData.DataValue(6, EntityDataSerializers.POSE, pose.toVanilla());
        List<Object> dataValues = new ArrayList<>();
        dataValues.add(dataValue);
        glowEffectPacket.getModifier().write(1, dataValues);

        protocolManager.sendServerPacket(receiver, glowEffectPacket);
    }

    public static void sendUpdatePositionPacket(Player receiver, int entityId, double currentX, double currentY, double currentZ, double prevX, double prevY, double prevZ) {
        PacketContainer movePacket = new PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE);
        short deltaX = (short)((currentX * 4096) - (prevX * 4096));
        short deltaY = (short)((currentY * 4096) - (prevY * 4096));
        short deltaZ = (short)((currentZ * 4096) - (prevZ * 4096));

        movePacket.getIntegers().write(0, entityId);
        movePacket.getShorts().write(0, deltaX);
        movePacket.getShorts().write(1, deltaY);
        movePacket.getShorts().write(2, deltaZ);
        movePacket.getBooleans().write(0, true);
        protocolManager.sendServerPacket(receiver, movePacket);
    }

    public static void sendUpdatePositionAndRotationPacket(Player receiver, int entityId, int yaw, int pitch, double currentX, double currentY, double currentZ, double prevX, double prevY, double prevZ) {
        PacketContainer movePacket = new PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK);
        short deltaX = (short)((currentX * 4096) - (prevX * 4096));
        short deltaY = (short)((currentY * 4096) - (prevY * 4096));
        short deltaZ = (short)((currentZ * 4096) - (prevZ * 4096));

        movePacket.getIntegers().write(0, entityId);
        movePacket.getShorts().write(0, deltaX);
        movePacket.getShorts().write(1, deltaY);
        movePacket.getShorts().write(2, deltaZ);
        movePacket.getBytes().write(0, (byte) yaw);
        movePacket.getBytes().write(1, (byte) pitch);
        movePacket.getBooleans().write(0, true);
        protocolManager.sendServerPacket(receiver, movePacket);
    }

    @Experimental
    public static void npcWalk(Player receiver, JavaPlugin plugin, NPC npc, NpcWalkDirection direction, long startDelay, double step, int maxSteps) {
        Location startLoc = npc.getLocation();

        final double[] prevX = {startLoc.getX()};
        final double[] prevY = {startLoc.getY()};
        final double[] prevZ = {startLoc.getZ()};

        final double[] currentX = {prevX[0]};
        final double[] currentY = {prevY[0]};
        final double[] currentZ = {prevZ[0]};

        // double step = 0.10;

        double xStep = 0.0;
        double zStep = 0.0;

        switch (direction) {
            case NpcWalkDirection.NORTH -> zStep = step;
            case NpcWalkDirection.SOUTH -> zStep = -step;
            case NpcWalkDirection.SOUTH_EAST -> {
                xStep = -step;
                zStep = -step;
            }
            case NpcWalkDirection.SOUTH_WEST -> {
                xStep = step;
                zStep = -step;
            }
            case NpcWalkDirection.NORTH_EAST -> {
                xStep = -step;
                zStep = step;
            }
            case NpcWalkDirection.NORTH_WEST -> {
                xStep = step;
                zStep = step;
            }
            case NpcWalkDirection.EAST -> xStep = -step;
            case NpcWalkDirection.WEST -> xStep = step;
        }

        double finalXStep = xStep;
        double finalZStep = zStep;

        final int[] steps = {0};
        new BukkitRunnable() {

            @Override
            public void run() {
                if (steps[0] == maxSteps) {
                    this.cancel();
                    return;
                }
                prevX[0] = currentX[0];
                prevY[0] = currentY[0];
                prevZ[0] = currentZ[0];

                currentX[0] += finalXStep;
                currentZ[0] += finalZStep;

                NPCUtils.sendUpdatePositionPacket(
                        receiver,
                        npc.getEntityId(),
                        prevX[0], prevY[0], prevZ[0],
                        currentX[0], currentY[0], currentZ[0]
                );
                npc.getLocation().setX(currentX[0]);
                npc.getLocation().setY(currentY[0]);
                npc.getLocation().setZ(currentZ[0]);
                steps[0]++;
            }
            // recomended start delay 0L when npcWalk is never used before
        }.runTaskTimer(plugin, startDelay, 2L);
    }

    @FunctionalInterface
    public interface NpcClickHandler {
        void handle(Player player, int entityId, WrappedEntityUseAction actionType, WrappedHand hand, boolean shiftClick);
    }

    public enum NpcWalkDirection {
        NORTH,
        NORTH_WEST,
        NORTH_EAST,
        WEST,
        EAST,
        SOUTH_WEST,
        SOUTH_EAST,
        SOUTH
    }
}
