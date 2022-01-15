package com.eric0210.StructureGenerater;

import java.util.Random;

import com.eric0210.core.utils.GameUtils;

import net.minecraft.server.v1_7_R4.StructureBoundingBox;
import net.minecraft.server.v1_7_R4.WorldGenNetherStart;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;

public class NetherStrongHold implements IStructure
{

	@Override
	public void generate(final Location pos, final int size)
	{
		final Random random = new Random();
		final WorldServer worldServer = GameUtils.GetHandle.getWorldHandle(pos.getWorld());
		final int x = pos.getBlockX();
		final int z = pos.getBlockZ();
		final WorldGenNetherStart nether = new WorldGenNetherStart(worldServer, random, x >> 4, z >> 4);
		nether.a(worldServer, new Random(), new StructureBoundingBox(x - 1000, z - 1000, x + 1000, z + 1000));
	}

}
