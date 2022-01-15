package com.eric0210.core.api.barapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.eric0210.core.EricPackage;
import com.eric0210.core.utils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class SideBar
{
	public static List<SideBar> sideBars = new ArrayList<>();
	public Updater updater;
	List<Entries> entrys = new ArrayList<>();
	Player player;
	Scoreboard scoreboard;

	public SideBar(final Player p)
	{
		player = p;
		updater = new Updater(this);
	}

	public Player getPlayer()
	{
		return player;
	}

	public Entries getEntries(final DisplaySlot slot, final String displayName)
	{
		final Entries entrys = new Entries(slot, displayName);
		if (!this.entrys.contains(entrys))
			this.entrys.add(entrys);
		return entrys;
	}

	public Scoreboard toScoreboard()
	{
		return scoreboard;
	}

	public class Entries
	{
		HashMap<Integer, String> lines = new HashMap<>();
		String displayName;
		DisplaySlot slot;
		Map<String, Object> replacements;

		public Entries(final DisplaySlot slot, final String displayName)
		{
			this.slot = slot;
			this.displayName = displayName;
		}

		public void setReplacements(final Map<String, Object> replacements)
		{
			this.replacements = replacements;
		}

		public HashMap<Integer, String> getLines()
		{
			return lines;
		}

		public void setReplacements(final String replacefor, final String replaceto)
		{
			replacements.put(replacefor, replaceto);
		}

		public Map<String, Object> getReplacements()
		{
			return replacements;
		}

		public void setLine(final int line, final String msg)
		{
			lines.put(line, msg);
		}

		public String getMessageAt(final int line)
		{
			return lines.get(line);
		}

		public String getDisplayName()
		{
			return displayName;
		}

		public DisplaySlot getSlot()
		{
			return slot;
		}

		public void removeLine(final int line)
		{
			lines.remove(line);
		}
	}

	public class Updater implements Runnable
	{
		ArrayList<Runnable> externalTasks = new ArrayList<>();
		SideBar sideBar;
		private volatile boolean running = true;

		public Updater(final SideBar b)
		{
			sideBar = b;
			Bukkit.getScheduler().scheduleSyncRepeatingTask(EricPackage.instance(), this, 0L, 25L);
		}

		public void setRunning(final boolean run)
		{
			running = run;
		}

		public void addExternalTask(final Runnable r)
		{
			externalTasks.add(r);
		}

		public void removeExternalTask(final Runnable r)
		{
			final Iterator<Runnable> itr = externalTasks.iterator();
			while (itr.hasNext())
			{
				final Runnable runnable = itr.next();
				if (runnable.equals(r))
					itr.remove();
			}
		}

		@Override
		public void run()
		{
			if (!running)
				return;
			if (!EricPackage.instance().isEnabled())
				return;
			for (final Runnable r : externalTasks)
				r.run();

			final Player target = sideBar.player;
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			for (final Entries e : sideBar.entrys)
			{
				final String objID = e.getDisplayName().replaceAll(" ", "_");
				final Objective obj = scoreboard.registerNewObjective(objID, "dummy");
				final Team team = scoreboard.registerNewTeam(objID);
				team.setPrefix("  ");
				team.setSuffix("  ");
				obj.setDisplayName(e.getDisplayName());
				obj.setDisplaySlot(e.getSlot());
				for (final Integer line : e.getLines().keySet())
				{
					final String message = e.getLines().get(line);
					final String translated = new Translater(message).doTranslate(e.getReplacements());
					final StringBuilder sb = new StringBuilder(translated);
					for (int i = 0; i <= 15 - translated.length(); i++)
						sb.append(Pattern.compile(" ", Pattern.LITERAL).toString());
					String filled = sb.toString();
					if (filled.length() > 16)
						do
							if (filled.length() > 0) {
								final int pos = filled.length() - 1;
								//									System.out.println("filllength: " + filled.length() + ", substring 0, " + pos);
								if (pos > 0)
									filled = filled.substring(0, pos);
								else
									break;
							} else
								break;
						while (filled.length() > 16);
//					System.out.println("filled: " + filled);
					// team.addEntry(filled);
					team.addPlayer(new CraftPlayer((CraftServer) Bukkit.getServer(), new EntityPlayer(MinecraftServer.getServer(), GameUtils.GetHandle.getWorldHandle(Bukkit.getWorld("world")), new GameProfile(UUID.randomUUID(), filled), new PlayerInteractManager(GameUtils.GetHandle.getWorldHandle(Bukkit.getWorld("world"))))));
					obj.getScore(filled).setScore(line);
				}
			}
			if (!checkScoreboard(target.getScoreboard(), scoreboard))
				target.setScoreboard(scoreboard);
		}

		private boolean checkScoreboard(final Scoreboard sb1, final Scoreboard sb2)
		{
			if (sb1 == null)
				return false;
			if (sb2 == null)
				return false;
			for (final Objective obj : sb1.getObjectives()) // Objective üũ
			{
				if (obj == null)
					return false; // obj�� null�� ��� (��ü ����)
				final Objective obj2 = sb2.getObjective(obj.getName()); // sb2���� obj�� ���� ID�� ���� Objective �ҷ���
				if (obj2 == null)
					return false; // sb2�� obj�� �̸��� ���� Objective�� ������ ���� ���� ���
			}

			if (sb1.getEntries() == null)
				return false;
			if (sb2.getEntries() == null)
				return false;

			final String[] arr1 = sb1.getEntries().toArray(new String[0]);
			final String[] arr2 = sb2.getEntries().toArray(new String[0]);
			if (arr1.length != arr2.length)
				return false;
			for (int i = 0; i <= sb1.getEntries().size() - 1; i++)
				if (!arr1[i].equals(arr2[i]))
					return false;
			return true;
		}
	}

	public class Translater
	{
		String target;

		public Translater(final String t)
		{
			target = t;
		}

		public String doTranslate(final String replacefor, final Object replaceto)
		{
			if (replaceto == null)
				return target;
			return target.replaceAll(replacefor, String.valueOf(replaceto));
		}

		public String doTranslate(final Map<String, Object> replacementMap)
		{
			if (replacementMap == null)
				return target;
			for (final Entry<String, Object> entry : replacementMap.entrySet())
				target = target.replaceAll(entry.getKey(), String.valueOf(entry.getValue()));
			return target;

		}
	}
}
