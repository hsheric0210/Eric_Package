package com.eric0210.aooniGame;

import java.util.Random;

import com.eric0210.aooniGame.items.SkillManager;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AooniGameMainScripter
{

	public AooniGame base;

	public enum GameStatus
	{
		NOPLAY,
		INIT,
		AOONI_WAIT,
		PLAY
	}

	public static GameStatus GameScenario = GameStatus.NOPLAY;
	public static boolean CanAooniMove = true;
	public static String aooniPlayerName;
	public String aooniCustomName;
	public String defaultAooniCustomName;
	public boolean AooniCustomNameVisible;
	public static boolean instaKill;
	public static Player[] players;
	public static AooniTimer aooniTimer;
	public SkillManager skillManager = new SkillManager(this);

	public AooniGameMainScripter(final AooniGame b)
	{
		base = b;
		aooniTimer = new AooniTimer(b);
	}

	/**
	 *
	 * @param  sender
	 *                 The command sender
	 * @param  command
	 *                 The command (without arguments)
	 * @param  args
	 *                 The command arguments
	 * @return
	 */
	public boolean handleCommand(final CommandSender sender, final String command, final String[] args)
	{
		if (args.length > 0)
		{
			final String operation = args[0];
			if (sender instanceof Player)
			{
				final Player p = (Player) sender;
				if ("start".equalsIgnoreCase(operation))
				{
					if (GameScenario != GameStatus.NOPLAY)
					{
						p.sendMessage(prefix() + ChatColor.RED + "게임이 이미 시작되었습니다.");
						return false;
					}

					int waitDuration = 45;
					int gameDuration = 3;

					if (args.length > 1)
					{
						try
						{
							waitDuration = Integer.parseInt(args[1]);
						}
						catch (final NumberFormatException nfe)
						{
							p.sendMessage(prefix() + ChatColor.RED + "매개 변수가 잘못되었습니다: " + nfe.toString());
						}

						if (args.length > 2)
							try
							{
								gameDuration = Integer.parseInt(args[2]);
							}
							catch (final NumberFormatException nfe)
							{
								p.sendMessage(prefix() + ChatColor.RED + "매개 변수가 잘못되었습니다: " + nfe.toString());
							}
					}

					startGame(p, waitDuration, gameDuration);
					return true;
				}
				else if ("stop".equalsIgnoreCase(operation))
					if (GameScenario != GameStatus.NOPLAY)
						terminateGame(p);
					else
						p.sendMessage(prefix() + ChatColor.RED + "아직 게임이 시작되지 않았습니다.");
				else if ("instakill".equalsIgnoreCase(operation))
					if (instaKill)
					{
						instaKill = false;
						p.sendMessage(prefix() + ChatColor.RED + "aooni Insta Kill Disabled.");
					}
					else
					{
						instaKill = true;
						p.sendMessage(prefix() + ChatColor.GREEN + "aooni Insta Kill Enabled.");
					}
				else
					p.sendMessage(prefix() + ChatColor.RED + "매개 변수가 잘못되었습니다: " + operation);
			}
			else
				StringUtils.Error_MustBePlayer(sender);
		}
		else
		{
			sender.sendMessage(prefix() + "* ------------------------------------------------------- *");
			sender.sendMessage(prefix() + ChatColor.GOLD + "/aooni <start|stop|instakill> [Delay] [Game duration]");
			sender.sendMessage(prefix() + ChatColor.LIGHT_PURPLE + "start: 게임을 시작합니다. [delay] 로 술래의 대기 시간(단위: 초)을 임의로 지정할 수 있고, [Game duration]으로 게임의 진행 시간(단위: 분)을 임의로 지정할 수 있습니다.");
			sender.sendMessage(prefix() + ChatColor.LIGHT_PURPLE + "stop: 게임을 강제로 종료하고, 모든 설정은 초기화시킵니다.");
			sender.sendMessage(prefix() + ChatColor.LIGHT_PURPLE + "instakill: 아오오니가 아오오니가 아닌 사람들을 한 방에 죽일 수 있도록 합니다.");
			sender.sendMessage(prefix() + "* ------------------------------------------------------- *");
		}
		return false;
	}

	public void startGame(final Player p, final int waitDuration, final int gameDuration)
	{
		int fixedWaitDuration = waitDuration;
		Bukkit.broadcastMessage(prefix() + "* ------------------------------------------------------- *");
		Bukkit.broadcastMessage(prefix() + ChatColor.LIGHT_PURPLE + "아오오니 게임이 시작되었습니다.");
		Bukkit.broadcastMessage(prefix() + ChatColor.AQUA + "아오오니 추첨중...");
		players = GameUtils.getOnlinePlayersArray();

		final Player aooni = randomAooni(players);
		GameScenario = GameStatus.AOONI_WAIT;

		aooniPlayerName = aooni.getName();

		for (final World w : Bukkit.getWorlds())
		{
			w.setGameRuleValue("keepInventory", "true");
			w.setGameRuleValue("doFireTick", "false");
			w.setGameRuleValue("doMobSpawning", "false");
			w.setGameRuleValue("doMobLoot", "false");
			w.setPVP(true);
			w.setThundering(false);
			w.setStorm(true);
			w.setDifficulty(Difficulty.PEACEFUL);
			w.setTime(350000L); // Set time to night
			w.setWeatherDuration(Integer.MAX_VALUE);
		}

		if (fixedWaitDuration > 60)
		{
			p.sendMessage(prefix() + ChatColor.RED + "(!) 아오오니 대기 타이머는 최대 1분(60초)까지 가능합니다. 1분(60초)로 설정됩니다.");
			fixedWaitDuration = 60;
		}

		Bukkit.broadcastMessage(prefix() + ChatColor.LIGHT_PURPLE + aooni.getName() + "이(가) 아오오니입니다!");
		Bukkit.broadcastMessage(prefix() + String.format("(!) 아오오니는 %s 초간 움직일 수 없습니다. 그 사이에 빨리 숨거나 도망치세요!", waitDuration));
		Bukkit.broadcastMessage(prefix() + "* ------------------------------------------------------- *");
		final long wait = fixedWaitDuration * 1000L;
		aooniTimer.start(aooni, wait, gameDuration);
	}

	private void terminateGame(final Player p)
	{
		Bukkit.broadcastMessage(prefix() + "* ------------------------------------------------------- *");
		Bukkit.broadcastMessage(prefix() + ChatColor.LIGHT_PURPLE + "(!) 게임이 " + p.getName() + "님에 의해 중지되었습니다.");
		Bukkit.broadcastMessage(prefix() + ChatColor.LIGHT_PURPLE + "모든 설정들은 초기화됩니다.");
		Bukkit.broadcastMessage(prefix() + "* ------------------------------------------------------- *");
		for (final Player pl : GameUtils.getOnlinePlayers())
			for (final ItemStack is : pl.getInventory().getContents())
				if (SkillManager.isGameItem(is))
					pl.getInventory().remove(is);
		GameScenario = GameStatus.NOPLAY;
		aooniPlayerName = "";
		aooniTimer.stop();
	}

	public void endGame()
	{
		Bukkit.broadcastMessage(prefix() + ChatColor.LIGHT_PURPLE + "타임 오버!");
		Bukkit.broadcastMessage(prefix() + ChatColor.LIGHT_PURPLE + "플레이어들의 승리!");
		for (final Player pl : GameUtils.getOnlinePlayers())
			for (final ItemStack is : pl.getInventory().getContents())
				if (SkillManager.isGameItem(is))
					pl.getInventory().remove(is);
		GameScenario = GameStatus.NOPLAY;
		aooniPlayerName = "";
		aooniTimer.stop();
	}

	private Player randomAooni(final Player[] candidates)
	{
		final Random random = new Random();
		final Player aooni = candidates[random.nextInt(candidates.length)];
		return aooni;
	}

	public static String prefix()
	{
		return StringUtils.getBracketPrefix(ChatColor.LIGHT_PURPLE, "Aooni") + ChatColor.RESET;
	}

	public static boolean isAooniWeapon(final ItemStack i)
	{
		return i.getItemMeta().getEnchantLevel(Enchantment.DURABILITY) == 10 && "아오오니 무기".equals(i.getItemMeta().getDisplayName());
	}
}
