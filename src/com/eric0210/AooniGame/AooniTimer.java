package com.eric0210.aooniGame;

import java.util.Timer;
import java.util.TimerTask;

import com.eric0210.aooniGame.AooniGameMainScripter.GameStatus;
import com.eric0210.aooniGame.items.SkillManager;
import com.eric0210.core.api.bossbar.BossBar;
import com.eric0210.core.utils.GameUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AooniTimer
{
	public Player aooni;
	private Timer aooniWaitTimer;
	AooniGame theGame;
	Timer timeBarUpdater;
	BossBar timeBar;

	private int gameDurationMinutes;

	public AooniTimer(final AooniGame game)
	{
		theGame = game;
	}

	public void start(final Player aooni, final long waitMilliseconds, final int gameDurationMinutes)
	{
		this.gameDurationMinutes = gameDurationMinutes;

		final long time = System.currentTimeMillis() - 1L;

		aooniWaitTimer = new Timer();
		timeBar = new BossBar(aooni.getWorld(), GameUtils.getOnlinePlayers(), 999998, 999999);

		this.aooni = aooni;
		this.aooni.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 255), false);
		this.aooni.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200, false));
		theGame.mainScripter.aooniCustomName = this.aooni.getCustomName();
		this.aooni.setCustomName("아오오니");
		theGame.mainScripter.AooniCustomNameVisible = this.aooni.isCustomNameVisible();

		this.aooni.setCustomNameVisible(true);
		AooniGameMainScripter.CanAooniMove = false;

		timeBar.show(100L);
		timeBarUpdater = new Timer("Aooni Remaining Time Bar Updater");
		timeBarUpdater.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				final int remainingSeconds = Math.round((waitMilliseconds - (System.currentTimeMillis() - time)) / 1000);
				final float healthProportion = 1.0F - (float) Math.max((double) (System.currentTimeMillis() - time) / waitMilliseconds, 0.01);
				timeBar.setText(remainingSeconds + "초 남았습니다.");
				timeBar.setHealthProportion(healthProportion);
			}
		}, 0, 200L);

		aooniWaitTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				final long time = System.currentTimeMillis();
				final long gameDurationMilliseconds = gameDurationMinutes * 60000L;

				timeBarUpdater.cancel();

				aooni.removePotionEffect(PotionEffectType.BLINDNESS);
				aooni.removePotionEffect(PotionEffectType.JUMP);
				AooniGameMainScripter.CanAooniMove = true;
				theGame.mainScripter.skillManager.onAooniChoosedHook(aooni);
				theGame.mainScripter.skillManager.onGameStartHook();
				Bukkit.broadcastMessage(AooniGameMainScripter.prefix() + ChatColor.LIGHT_PURPLE + " 아오오니가 움직이기 시작합니다! 모두 숨으세요!");
				aooni.getInventory().setHeldItemSlot(0);
				aooni.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 10, false));
				aooni.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false));
				aooni.updateInventory();
				AooniGameMainScripter.GameScenario = GameStatus.PLAY;

				timeBarUpdater.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						final int remainingSeconds = Math.round((gameDurationMilliseconds - (System.currentTimeMillis() - time)) / 1000);
						final float healthProportion = 1.0F - (float) Math.max((double) (System.currentTimeMillis() - time) / gameDurationMilliseconds, 0.01);
						timeBar.setText(remainingSeconds + "초 남았습니다.");
						timeBar.setHealthProportion(healthProportion);
					}
				}, 0, 200L);

				aooniWaitTimer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						timeBarUpdater.cancel();
						theGame.mainScripter.endGame();
					}
				}, gameDurationMilliseconds);
			}
		}, waitMilliseconds);
	}

	public void stop()
	{
		aooni.setCustomName(theGame.mainScripter.aooniCustomName);
		aooni.setCustomNameVisible(theGame.mainScripter.AooniCustomNameVisible);
		aooni.removePotionEffect(PotionEffectType.BLINDNESS);
		aooni.removePotionEffect(PotionEffectType.REGENERATION);
		aooni.getInventory().setChestplate(null);
		aooni.getInventory().setBoots(null);
		AooniGameMainScripter.CanAooniMove = true;
		for (final Player p : GameUtils.getOnlinePlayers())
			for (final ItemStack i : p.getInventory().getContents())
				if (SkillManager.isGameItem(i))
					p.getInventory().remove(i);
		aooniWaitTimer.cancel();
	}
}
