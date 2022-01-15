package com.eric0210.StructureGenerater;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class SandBox implements IStructure
{
	@Override
	public void generate(final Location pos, final int size)
	{
		final World w = pos.getWorld();
		final Location l1 = pos.clone();
		final Location l2 = pos.clone();
		final int fixedSize = Math.min(size, 10);
		final int multiply = 3 * fixedSize;
		final int ymultiply1 = 5 * fixedSize;
		final int ymultiply2 = 10 * fixedSize;
		for (int j = ymultiply1; j <= ymultiply2; j++)
		{
			l2.setY(l1.getY() + j);
			for (int i = 0; i <= multiply; i++)
			{
				l2.setX(l1.getX() + i);
				if (w.getBlockAt(l2).getType() != Material.BEDROCK)
					w.getBlockAt(l2).setType(Material.SAND);
				for (int k = 0; k <= multiply; k++)
				{
					l2.setZ(l1.getZ() + k);
					if (w.getBlockAt(l2).getType() != Material.BEDROCK)
						w.getBlockAt(l2).setType(Material.SAND);
				}
			}
			for (int i = 0; i <= multiply; i++)
			{
				l2.setX(l1.getX() - i);
				if (w.getBlockAt(l2).getType() != Material.BEDROCK)
					w.getBlockAt(l2).setType(Material.SAND);
				for (int k = 0; k <= multiply; k++)
				{
					l2.setZ(l1.getZ() - k);
					if (w.getBlockAt(l2).getType() != Material.BEDROCK)
						w.getBlockAt(l2).setType(Material.SAND);
				}
			}
			for (int i = 0; i <= multiply; i++)
			{
				l2.setX(l1.getX() - i);
				if (w.getBlockAt(l2).getType() != Material.BEDROCK)
					w.getBlockAt(l2).setType(Material.SAND);
				for (int k = 0; k <= multiply; k++)
				{
					l2.setZ(l1.getZ() + k);
					if (w.getBlockAt(l2).getType() != Material.BEDROCK)
						w.getBlockAt(l2).setType(Material.SAND);
				}
			}
			for (int i = 0; i <= multiply; i++)
			{
				l2.setX(l1.getX() + i);
				if (w.getBlockAt(l2).getType() != Material.BEDROCK)
					w.getBlockAt(l2).setType(Material.SAND);
				for (int k = 0; k <= multiply; k++)
				{
					l2.setZ(l1.getZ() - k);
					if (w.getBlockAt(l2).getType() != Material.BEDROCK)
						w.getBlockAt(l2).setType(Material.SAND);
				}
			}
		}
	}

}
