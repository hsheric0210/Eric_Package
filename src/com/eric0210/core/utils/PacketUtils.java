package com.eric0210.core.utils;

import com.eric0210.core.EricPackage;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

import org.bukkit.entity.Player;

public class PacketUtils
{
	public static final void sendPacket(final Player p, final Packet packet)
	{
		GameUtils.GetHandle.getPlayerHandle(p).playerConnection.sendPacket(packet);
	}

	public static final void sendRawJSONMessage(final Player p, final String jsonMessage)
	{
		EricPackage.info("sent json message " + jsonMessage + " to " + p.getName());
		sendPacket(p, new PacketPlayOutChat(ChatSerializer.a(jsonMessage)));
	}
}
