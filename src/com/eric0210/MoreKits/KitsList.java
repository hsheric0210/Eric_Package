package com.eric0210.MoreKits;

import java.util.ArrayList;
import java.util.List;

import com.eric0210.MoreKits.kits.VisualAbility;
import com.eric0210.core.EricPackage;

public class KitsList
{
	public static List<KitBase> ToolList = new ArrayList<>();
	public static final VisualAbility va = new VisualAbility();

	public static void InitTools()
	{
		for (final KitBase tool : ToolList)
		{
			tool.Init();
			EricPackage.info(String.format("[MoreKits] tool %s is sucessful registered.", tool.getName()));
		}
	}

	public static KitBase getTool(final String toolname)
	{
		for (final KitBase tool : ToolList)
		{
			EricPackage.info(toolname);
			EricPackage.info(tool.getName());
			if (tool.getName().equalsIgnoreCase(toolname))
				return tool;
		}
		return null;
	}
}
