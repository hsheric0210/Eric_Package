package com.eric0210.AdditionalFunctions;

import java.util.Locale;
import java.util.Random;

import com.eric0210.core.EricPackage;
import com.eric0210.core.api.IEventHandler;
import com.eric0210.core.utils.GameUtils;
import com.eric0210.core.utils.JavaHelper;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AdditionalDeathEffect implements IEventHandler
{
	// FIXME: Optimize and Re-code everything
	@Override
	public boolean onEvent(final Event event, final int eventDataIndex)
	{
		if (eventDataIndex != 4)
			return false; // 4 is CustomDeathEffect's registered event data ID

		final PlayerDeathEvent e = (PlayerDeathEvent) event;

		final Random random = new Random();

		final Player player = e.getEntity();
		final World world = player.getWorld();
		final Location pos = player.getLocation();
		final Location eyepos = player.getEyeLocation();

		final EntityDamageEvent damageEvent = player.getLastDamageCause() != null ? player.getLastDamageCause() : new EntityDamageEvent(player, DamageCause.CUSTOM, GameUtils.AmbigousMethods.getMaxHealthDouble(player));
		final EntityDamageEvent.DamageCause damageCause = damageEvent.getCause() != null ? damageEvent.getCause() : EntityDamageEvent.DamageCause.CUSTOM;

		// <editor-fold desc="Random death effect">
		final float effectVolume = 0.5F;
		switch (random.nextInt(6))
		{
			case 0:
				world.playSound(pos, Sound.DIG_STONE, effectVolume, (float) JavaHelper.random(0.5F, 2.0F));
				GameUtils.blockBreakEffect(pos, Material.REDSTONE_BLOCK, 100, 0.45);
				GameUtils.blockBreakEffect(eyepos, Material.REDSTONE_BLOCK, 100, 0.45);
				break;
			case 1:
				world.strikeLightningEffect(pos);
				world.playEffect(pos, Effect.EXPLOSION_HUGE, 0, 5);
				world.playSound(pos, Sound.EXPLODE, effectVolume, 1.0F);
				GameUtils.sphereEffect(pos, Effect.SMOKE, BlockFace.UP, 50, .4);
				break;
			case 2:
				// Critical effects
				world.playSound(pos, Sound.SKELETON_DEATH, effectVolume, 1.0F);
				GameUtils.sphereEffect(pos, Effect.CRIT, null, 50, .4);
				GameUtils.sphereEffect(pos, Effect.MAGIC_CRIT, null, 50, .4);
				GameUtils.sphereEffect(eyepos, Effect.CRIT, null, 50, .4);
				GameUtils.sphereEffect(eyepos, Effect.MAGIC_CRIT, null, 50, .4);
				break;
			case 3:
				world.playSound(pos, Sound.VILLAGER_DEATH, effectVolume, 1.0F);
				GameUtils.sphereEffect(pos, Effect.VILLAGER_THUNDERCLOUD, null, 100, .45);
				GameUtils.sphereEffect(eyepos, Effect.VILLAGER_THUNDERCLOUD, null, 100, .45);
				break;
			case 4:
				world.playSound(pos, Sound.ENDERDRAGON_DEATH, effectVolume, 3.8F);
				GameUtils.sphereEffect(new Location(world, pos.getX(), pos.getY() - .25, pos.getZ()), Effect.MOBSPAWNER_FLAMES, null, 15, .15);
				break;
			case 5:
				world.playSound(pos, Sound.FIZZ, effectVolume, 1.5F);
				GameUtils.blockBreakEffect(pos, Material.LAVA, 200, .5);
				GameUtils.blockBreakEffect(eyepos, Material.LAVA, 200, .5);
				break;
			case 6:
				world.playSound(pos, Sound.WOLF_HOWL, effectVolume, 1.0F);
				GameUtils.blockBreakEffect(pos, Material.COAL_BLOCK, 200, .4);
				GameUtils.blockBreakEffect(eyepos, Material.COAL_BLOCK, 200, .4);
				break;
		}
		// </editor-fold>

		final Block gravePos = world.getBlockAt(pos);
		if (gravePos.getType() == null || gravePos.getType() == Material.AIR)
		{
			final long graveRemoveAfter = 200L;

			gravePos.setType(Material.SIGN_POST);

			final Sign sign = (Sign) gravePos.getState();

			sign.setLine(0, ChatColor.BOLD + "R.I.P");
			sign.setLine(1, ChatColor.RED + player.getName());

			String deathCause = "";
			switch (damageCause != null ? damageCause : EntityDamageEvent.DamageCause.CUSTOM)
			{
				case BLOCK_EXPLOSION:
					deathCause = "TNT";
					break;
				case CONTACT:
				case ENTITY_EXPLOSION:
				case CUSTOM:
				case SUICIDE:
				case VOID:
					deathCause = "";
					break;
				case DROWNING:
					deathCause = "H2O";
					break;
				case ENTITY_ATTACK:
					final EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) player.getLastDamageCause();
					if (edbe.getDamager().getType() == EntityType.PLAYER)
						deathCause = ((AnimalTamer) edbe.getDamager()).getName();
					else
						deathCause = edbe.getDamager().getType().toString().toLowerCase(Locale.ENGLISH);
					break;
				case FALL:
					deathCause = "?????¥ç??? ???";
					break;
				case FALLING_BLOCK:
					deathCause = "????????";
					break;
				case FIRE:
					deathCause = "?“ž??";
					break;
				case FIRE_TICK:
					deathCause = "???";
					break;
				case LAVA:
					deathCause = "???";
					break;
				case LIGHTNING:
					deathCause = "???? ????";
					break;
				case MAGIC:
					deathCause = "??? 7";
					break;
				case MELTING:
					deathCause = "Melted";
					break;
				case POISON:
					deathCause = "???????";
					break;
				case PROJECTILE:
					deathCause = "killed " + player.getName() + " (+100)";
					break;
				case STARVATION:
					deathCause = "Return to wild";
					break;
				case SUFFOCATION:
					deathCause = "????";
					break;
				case THORNS:
					deathCause = "???";
					break;
				case WITHER:
					deathCause = "??";
					break;
				default:
					break;
			}
			sign.setLine(2, deathCause);

			sign.update();

			Bukkit.getScheduler().scheduleSyncDelayedTask(EricPackage.instance(), () ->
			{
				gravePos.setType(Material.AIR);
				gravePos.setType(Material.AIR);
			}, graveRemoveAfter);
		}
		return true;
	}
}
