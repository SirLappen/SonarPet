package net.techcable.sonarpet.utils.reflection;

import lombok.*;

import com.google.common.base.Preconditions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static net.techcable.sonarpet.utils.reflection.Reflection.getNmsClass;
import static net.techcable.sonarpet.utils.reflection.Reflection.getObcClass;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionConstants {
    public static final Class<?> PACKET_CLASS = getNmsClass("Packet");
    public static final Class<?> PLAYER_CONNECTION_CLASS = getNmsClass("PlayerConnection");
    public static final Class<?> ENTITY_CLASS = getNmsClass("Entity");
    public static final Class<?> ENTITY_PLAYER_CLASS = getNmsClass("EntityPlayer");
    public static final Class<?> CRAFT_ENTITY_CLASS = getObcClass("entity.CraftEntity");
    public static final SonarMethod<?> GET_HANDLE_METHOD = SonarMethod.getMethodWithReturnType(CRAFT_ENTITY_CLASS, "getHandle", ENTITY_CLASS);
    public static final SonarField<?> PLAYER_CONNECTION_FIELD = SonarField.getField(ENTITY_PLAYER_CLASS, "playerConnection", PLAYER_CONNECTION_CLASS);
    public static final SonarMethod<Void> SEND_PACKET_METHOD = SonarMethod.getMethodWithReturnType(PLAYER_CONNECTION_CLASS, "sendPacket", void.class, PACKET_CLASS);

    public static Object getHandle(Entity entity) {
        return GET_HANDLE_METHOD.invoke(entity);
    }

    public static void sendPacket(Player player, Object packet) {
        sendPacket(getHandle(player), packet);
    }

    public static void sendPacket(Object entityPlayer, Object packet) {
        Object playerConnection = PLAYER_CONNECTION_FIELD.getValue(entityPlayer);
        SEND_PACKET_METHOD.invoke(playerConnection, packet);
    }
}
