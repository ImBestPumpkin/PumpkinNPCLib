package pl.imbestpumpkin.pumpkinNPCLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class NPC {
    private final String name;
    private final UUID uuid;
    private final String skinValue;
    private final String skinSignature;
    private final int entityId;
    private final Location location;
    private final boolean legacy;
    private final boolean hiddenNick;
    private final boolean collisions;
    private final ChatColor color;
    private final boolean glow;
    private final NPCUtils.NpcClickHandler handler;

    private final ProtocolManager protocolManager;

    private NPC(NPCBuilder builder) {
        this.name = builder.name;
        this.uuid = builder.uuid;
        this.skinValue = builder.skinValue;
        this.skinSignature = builder.skinSignature;
        this.entityId = builder.entityId;
        this.location = builder.location;
        this.legacy = builder.legacy;
        this.hiddenNick = builder.hiddenNick;
        this.collisions = builder.collisions;
        this.color = builder.color;
        this.glow = builder.glow;
        this.handler = builder.handler;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void spawnNpc(Player player, JavaPlugin plugin) {
        PacketContainer npc = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        Set<EnumWrappers.PlayerInfoAction> playerInfoActionSet = new HashSet<>();

        WrappedGameProfile wrappedGameProfile = new WrappedGameProfile(uuid, name);

        WrappedSignedProperty property = new WrappedSignedProperty("textures", skinValue, skinSignature);

        wrappedGameProfile.getProperties().clear();
        wrappedGameProfile.getProperties()
                .put("textures", property);

        PlayerInfoData playerInfoData = new PlayerInfoData(
                wrappedGameProfile,
                0,
                EnumWrappers.NativeGameMode.CREATIVE,
                WrappedChatComponent.fromText("name"));


        List<PlayerInfoData> playerInfoDataList = Arrays.asList(playerInfoData);

        playerInfoActionSet.add(EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        npc.getPlayerInfoActions()
                .write(0, playerInfoActionSet);

        npc.getPlayerInfoDataLists().write(1, playerInfoDataList);
        protocolManager.sendServerPacket(player, npc);

        PacketContainer npcPacket = protocolManager.createPacket(legacy ? PacketType.Play.Server.NAMED_ENTITY_SPAWN : PacketType.Play.Server.SPAWN_ENTITY);

        npcPacket.getIntegers()
                .write(0, entityId)
                .writeSafely(1, 122);

        npcPacket.getUUIDs()
                .write(0, uuid);

        npcPacket.getEntityTypeModifier()
                .writeSafely(0, EntityType.PLAYER);

        npcPacket.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        npcPacket.getBytes()
                .write(0, (byte) (location.getPitch() * (256.0F / 360.0F)))
                .write(1, (byte) (location.getYaw() * (256.0F / 360.0F)))
                .write(2, (byte) (location.getYaw() * (256.0F / 360.0F)));

        protocolManager.sendServerPacket(player, npcPacket);

        new BukkitRunnable() {
            @Override
            public void run() {
                NPCUtils.sendRemovePlayerInfoDataPacket(player, uuid);
            }
        }.runTaskLater(plugin, 5L);
        NPCUtils.sendTeamPacket(player, uuid, name, hiddenNick ? "never" : "always", collisions ? "never" : "always", color);
        if (glow) {
            NPCUtils.sendGlowPacket(player, entityId);
        }
        if (handler != null) {
            NPCUtils.npcInteraction(plugin, handler);
        }
    }

    public static NPCBuilder newNpcBuilder() {
        return new NPCBuilder();
    }

    public static class NPCBuilder {
        private String name;
        private UUID uuid;
        private String skinValue;
        private String skinSignature;
        private Integer entityId;
        private Location location;
        private boolean legacy;
        private boolean hiddenNick;
        private boolean collisions;
        private ChatColor color;
        private boolean glow;
        private NPCUtils.NpcClickHandler handler;

        public NPCBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public NPCBuilder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public NPCBuilder setSkin(String skinValue, String skinSignature) {
            this.skinValue = skinValue;
            this.skinSignature = skinSignature;
            return this;
        }

        public NPCBuilder setEntityId(int entityId) {
            this.entityId = entityId;
            return this;
        }

        public NPCBuilder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public NPCBuilder setLegacy() {
            legacy = true;
            return this;
        }

        public NPCBuilder hideNickname() {
            hiddenNick = true;
            return this;
        }

        public NPCBuilder collissionless() {
            collisions = true;
            return this;
        }

        public NPCBuilder setColor(ChatColor color) {
            this.color = color;
            return this;
        }

        public NPCBuilder glow() {
            glow = true;
            return this;
        }

        public NPCBuilder interaction(NPCUtils.NpcClickHandler handler) {
            this.handler = handler;
            return this;
        }

        public NPC build() {
            if (name == null) {
                name = String.valueOf((int) ((Math.random() * (99999999)) + 0));
                Bukkit.getLogger().severe("NPC name was not set, generating a random one...");
            }
            if (uuid == null) {
                uuid = UUID.randomUUID();
                Bukkit.getLogger().severe("NPC uuid was not set, generating a random one...");
            }
            if (entityId == null) {
                entityId = (int) (Math.random() * (-1 - (-9999999) + 1)) + (-9999999);
                Bukkit.getLogger().severe("NPC entityId was not set, generating a random one...");
            }
            if (location == null) {
                World w = Bukkit.getWorlds().getFirst();
                location = new Location(w, 0.0, 0.0, 0.0);
                Bukkit.getLogger().severe("NPC location was not set, spawning at " + w.getName() + " x: 0.0 y: 0.0 z: 0.0...");
            }
            if (color == null) {
                color = ChatColor.WHITE;
            }

            return new NPC(this);
        }
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getSkinValue() {
        return skinValue;
    }

    public String getSkinSignature() {
        return skinSignature;
    }

    public int getEntityId() {
        return entityId;
    }

    public Location getLocation() {
        return location;
    }
}
