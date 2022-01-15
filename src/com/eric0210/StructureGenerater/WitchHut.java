package com.eric0210.StructureGenerater;

import java.util.Random;

import com.eric0210.core.utils.GameUtils;

import net.minecraft.server.v1_7_R4.StructureBoundingBox;
import net.minecraft.server.v1_7_R4.StructureStart;
import net.minecraft.server.v1_7_R4.World;
import net.minecraft.server.v1_7_R4.WorldGenWitchHut;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;

public class WitchHut implements IStructure
{
	@Override
	public void generate(final Location pos, final int size)
	{
		final Random random = new Random();
		final WorldServer worldServer = GameUtils.GetHandle.getWorldHandle(pos.getWorld());
		final int x = pos.getBlockX();
		final int z = pos.getBlockZ();
		final StructureStart structure = new WitchHutStart(worldServer, new Random(), x >> 4, z >> 4);
		structure.a(worldServer, random, new StructureBoundingBox(x - 1000, z - 1000, x + 1000, z + 1000));
	}

	class WitchHutStart extends StructureStart
	{
		public WitchHutStart(final World world, final Random random, final int x, final int z)
		{
			super(x, z);
			final WorldGenWitchHut witchHut = new WorldGenWitchHut(random, x * 16, z * 16);
			a.add(witchHut);
			c();
		}
	}
}
