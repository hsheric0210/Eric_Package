package com.eric0210.aooniGame.items;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public interface SkillBase
{
	String SKILL_IN_COOLDOWN = ChatColor.RED + "이 능력(%s)은(는) 현재 쿨다운 중입니다.";
	String SKILL_COOLDOWN_FINISHED = ChatColor.GREEN + "이제 %s을(를) 다시 사용할 수 있습니다.";

	ItemStack getItem();

	int getSlot();

	void handleEvent(Event e);

	boolean isOnlyForAooni();
}
