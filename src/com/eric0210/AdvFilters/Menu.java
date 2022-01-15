package com.eric0210.AdvFilters;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eric0210.core.api.menu.ChestMenu;
import com.eric0210.core.api.menu.ChestMenu.Run;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.ItemUtils;
import com.eric0210.core.utils.PacketUtils;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Menu
{
	public static class PlayerMenu
	{
		public static void openMenuTo(final Player viewer, final Player target)
		{
			final ChestMenu menu = new ChestMenu(target.getName());
			menu.setButton(new ItemStack(Material.REDSTONE), "������", 26, null, new Run()
			{

				@Override
				public void run(final Inventory inv, final Player p)
				{
					menu.close(p);
				}
			});

			menu.setButton(new ItemStack(Material.BOOK, 1), ChatColor.GOLD + ChatColor.BOLD.toString() + "���� ����", 10, Arrays.asList(ChatColor.LIGHT_PURPLE + "�ش� �÷��̾��� ���� ��� ������ �� �� �ֽ��ϴ�."), Buttons.informationsButton(viewer, target, menu));
			menu.open(viewer);
		}
	}

	public static class Buttons
	{
		public static final ChestMenu.Run informationsButton(final Player p, final Player target, final ChestMenu opener)
		{
			return new Run()
			{
				@Override
				public void run(final Inventory inv, final Player p)
				{
					final ChestMenu info = new ChestMenu(target.getName() + "�� ����", 18);
					info.setItem(1, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "������ ����", Material.SKULL_ITEM, 1, Updater.update(target, DataType.PROFILE)));

					info.setItem(2, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "��ġ ����", Material.COMPASS, 1, Updater.update(target, DataType.LOCATION)));

					info.setItem(3, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "���� ����", Material.EXP_BOTTLE, 1, Updater.update(target, DataType.STATISTICS)));

					info.setItem(4, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "���� ����", Material.COMMAND, 1, Updater.update(target, DataType.CONNECTION)));

					info.setItem(5, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "���� ����", Material.GRASS, 1, Updater.update(target, DataType.WORLD)));
					info.setButton(new ItemStack(Material.BOOK, 1), ChatColor.GREEN + ChatColor.BOLD.toString() + "NameMC���� �˻�", 7, Arrays.asList("�÷��̾��� ������ NameMC.com���� �˻��մϴ�."), new Run()
					{
						@Override
						public void run(final Inventory inv, final Player p)
						{
							PacketUtils.sendRawJSONMessage(p, String.format("{text:\"Click here to go to %s's NameMC \",color:\"green\",clickEvent:{action:open_url,value:\"https://ko.namemc.com/search?q=%s\"}}", target.getName(), target.getName()));
						}
					});

					info.setButton(new ItemStack(Material.SLIME_BALL, 1), "Re-load Informations", 8, Arrays.asList("�������� �ٽ� �ε��մϴ�."), new ChestMenu.Run()
					{

						@Override
						public void run(final Inventory inv, final Player p)
						{
							info.close();
							info.setItem(1, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "������ ����", Material.SKULL_ITEM, 1, Updater.update(target, DataType.PROFILE)));

							info.setItem(2, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "��ġ ����", Material.COMPASS, 1, Updater.update(target, DataType.LOCATION)));

							info.setItem(3, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "���� ����", Material.EXP_BOTTLE, 1, Updater.update(target, DataType.STATISTICS)));

							info.setItem(4, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "���� ����", Material.COMMAND, 1, Updater.update(target, DataType.CONNECTION)));

							info.setItem(5, ItemUtils.createItemLores(ChatColor.GOLD + ChatColor.BOLD.toString() + "���� ����", Material.GRASS, 1, Updater.update(target, DataType.WORLD)));
							info.setButton(new ItemStack(Material.BOOK, 1), ChatColor.GREEN + ChatColor.BOLD.toString() + "NameMC���� �˻�", 7, Arrays.asList("�÷��̾��� ������ NameMC.com���� �˻��մϴ�."), new Run()
							{
								@Override
								public void run(final Inventory inv, final Player p)
								{
									PacketUtils.sendRawJSONMessage(p, String.format("{text:\"Click here to go to %s's NameMC \",color:\"green\",clickEvent:{action:open_url,value:\"https://ko.namemc.com/search?q=%s\"}}", target.getName(), target.getName()));
								}
							});
							info.open(p);
						}
					});

					info.setButton(new ItemStack(Material.ARROW, 1), "�ڷΰ���", 13, null, new ChestMenu.Run()
					{

						@Override
						public void run(final Inventory inv, final Player p)
						{
							info.close(p);
							opener.open(p);
						}
					});

					info.open(p);
				}
			};
		}
	}

	enum DataType
	{
		PROFILE,
		LOCATION,
		STATISTICS,
		CONNECTION,
		WORLD
	}

	static class Updater
	{

		private static void add(final List<String> l, final String s)
		{
			String s2 = s;
			if (ChatColor.stripColor(s2) == "true")
				s2 = ChatColor.GREEN + s;
			else if (ChatColor.stripColor(s2) == "false")
				s2 = ChatColor.RED + s;
			else
				s2 = ChatColor.LIGHT_PURPLE + s;
			l.add(s2);
		}

		public static List<String> update(final Player target, final DataType dt)
		{
			final ArrayList<String> db = new ArrayList<>();
			switch (dt)
			{
				case PROFILE:
					final GameProfile profile = GameUtils.GetHandle.getPlayerHandle(target).getProfile();
					add(db, "Name: " + ChatColor.GREEN + profile.getName());
					add(db, "Profile UUID: " + ChatColor.GREEN + profile.getId());
					add(db, "UUID: " + ChatColor.GREEN + target.getUniqueId());
					add(db, "Display Name: " + ChatColor.RESET + target.getDisplayName());
					add(db, "Custom Name: " + ChatColor.RESET + target.getCustomName());
					break;
				case LOCATION:
					final Location loc = target.getLocation();
					add(db, "X: " + ChatColor.GREEN + loc.getX());
					add(db, "Y: " + ChatColor.GREEN + loc.getY());
					add(db, "Z: " + ChatColor.GREEN + loc.getZ());
					add(db, "Yaw: " + ChatColor.GREEN + loc.getYaw());
					add(db, "Pitch: " + ChatColor.GREEN + loc.getPitch());
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Direction X: " + ChatColor.RESET + loc.getDirection().getX());
					add(db, "Direction Y: " + ChatColor.RESET + loc.getDirection().getY());
					add(db, "Direction Z: " + ChatColor.RESET + loc.getDirection().getZ());
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "World Name: " + ChatColor.RESET + loc.getWorld().getName());
					add(db, "World UUID: " + ChatColor.RESET + loc.getWorld().getUID());
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Chunk X: " + ChatColor.RESET + loc.getChunk().getX());
					add(db, "Chunk Z: " + ChatColor.RESET + loc.getChunk().getZ());
					break;
				case STATISTICS:
					final EntityPlayer nms = ((CraftPlayer) target).getHandle();
					add(db, "Health: " + ChatColor.GREEN + GameUtils.AmbigousMethods.getHealthDouble(target));
					add(db, "Food: " + ChatColor.GREEN + target.getFoodLevel());
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Active Effects - ");
					for (final PotionEffect pe : target.getActivePotionEffects())
						db.add(String.format("  %s(id %d) duration:%s ampifier:%s", pe.getType().getName(), pe.getType().getId(), pe.getDuration(), pe.getAmplifier()));
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Gamemode: " + ChatColor.RESET + target.getGameMode().toString());
					add(db, "Flying: " + ChatColor.RESET + target.isFlying());
					add(db, "Exp: " + ChatColor.RESET + target.getExp());
					add(db, "Level: " + ChatColor.RESET + target.getLevel());
					add(db, "Total Exp: " + ChatColor.RESET + target.getTotalExperience());
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Walk Speed: " + ChatColor.RESET + target.getWalkSpeed());
					add(db, "Flight Speed: " + ChatColor.RESET + target.getFlySpeed());
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Ping: " + ChatColor.RESET + nms.ping);
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Motion X:" + ChatColor.RESET + nms.motX);
					add(db, "Motion Y:" + ChatColor.RESET + nms.motY);
					add(db, "Motion Z:" + ChatColor.RESET + nms.motZ);
					break;
				case CONNECTION:
					final InetSocketAddress sockaddr = target.getAddress();
					final InetAddress addr = sockaddr.getAddress();
					add(db, "Connection IP: " + ChatColor.GREEN + addr.getHostAddress());
					add(db, "Socket Port: " + ChatColor.GREEN + sockaddr.getPort());
					add(db, "Caniocal Host Name: " + ChatColor.RESET + addr.getCanonicalHostName());
					add(db, "Host String: " + ChatColor.RESET + sockaddr.getHostString());
					add(db, "Host Name: " + ChatColor.RESET + sockaddr.getHostName());
					break;
				case WORLD:
					final World w = target.getWorld();
					add(db, "World Name: " + ChatColor.GREEN + w.getName());
					add(db, "World UUID: " + ChatColor.GREEN + w.getUID());
					add(db, "World Difficulty: " + ChatColor.RESET + w.getDifficulty());
					add(db, "World Environment: " + ChatColor.RESET + w.getEnvironment().toString());
					add(db, "World Seed: " + ChatColor.RESET + w.getSeed());
					add(db, "World Time: " + ChatColor.RESET + w.getFullTime());
					add(db, "World Generation Type: " + ChatColor.RESET + w.getWorldType().toString());
					add(db, "World Max Height: " + ChatColor.RESET + w.getMaxHeight());
					db.add(ChatColor.WHITE + "----------------------------------------------------");
					add(db, "Gamerule Values - ");
					for (final String gr : w.getGameRules())
						db.add((w.getGameRuleValue(gr) != "false" ? ChatColor.GREEN : ChatColor.RED) + "  " + ChatColor.RESET + gr + "=" + w.getGameRuleValue(gr));
					break;
			}
			return db;
		}
	}
}
