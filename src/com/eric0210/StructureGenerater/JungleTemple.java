package com.eric0210.StructureGenerater;

import java.util.Random;

import com.eric0210.core.utils.GameUtils;

import net.minecraft.server.v1_7_R4.StructureBoundingBox;
import net.minecraft.server.v1_7_R4.StructureStart;
import net.minecraft.server.v1_7_R4.WorldGenJungleTemple;

import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.Location;

public class JungleTemple implements IStructure
{
	@Override
	public void generate(final Location pos, final int size)
	{
		final Random random = new Random();
		final WorldServer worldServer = GameUtils.GetHandle.getWorldHandle(pos.getWorld());
		final int x = pos.getBlockX();
		final int z = pos.getBlockZ();
		final StructureStart structure = new JungleTempleStart(worldServer, random, x >> 4, z >> 4);
		structure.a(worldServer, new Random(), new StructureBoundingBox(x - 1000, z - 1000, x + 1000, z + 1000));
	}

	class JungleTempleStart extends StructureStart
	{
		public JungleTempleStart(final net.minecraft.server.v1_7_R4.World world, final Random random, final int x, final int z)
		{
			super(x, z);
			final WorldGenJungleTemple jungleTemple = new WorldGenJungleTemple(random, x * 16, z * 16);
			a.add(jungleTemple);
			c();
		}
	}
}
