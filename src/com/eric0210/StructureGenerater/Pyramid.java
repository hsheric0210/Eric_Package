package com.eric0210.StructureGenerater;

import java.util.Random;

import com.eric0210.core.utils.GameUtils;

import net.minecraft.server.v1_7_R4.*;

import org.bukkit.Location;

public class Pyramid implements IStructure
{
	@Override
	public void generate(final Location pos, final int size)
	{
		final Random random = new Random();
		final WorldServer worldServer = GameUtils.GetHandle.getWorldHandle(pos.getWorld());
		final int x = pos.getBlockX();
		final int z = pos.getBlockZ();
		final StructureStart structure = new PyramidStart(worldServer, new Random(), x >> 4, z >> 4);
		structure.a(worldServer, random, new StructureBoundingBox(x - 1000, z - 1000, x + 1000, z + 1000));
	}

	class PyramidStart extends StructureStart
	{
		public PyramidStart(final World world, final Random random, final int x, final int z)
		{
			super(x, z);
			final WorldGenPyramidPiece pyramidPiece = new WorldGenPyramidPiece(random, x * 16, z * 16);
			a.add(pyramidPiece);
			c();
		}
	}
}
