package com.eric0210.StructureGenerater;

import java.util.Random;

import com.eric0210.core.utils.GameUtils;

import net.minecraft.server.v1_7_R4.StructureBoundingBox;
import net.minecraft.server.v1_7_R4.WorldGenMineshaftStart;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;

public class MineShaft implements IStructure
{

	@Override
	public void generate(final Location loc, final int size)
	{
		final Random random = new Random();
		final WorldServer worldServer = GameUtils.GetHandle.getWorldHandle(loc.getWorld());
		final int x = loc.getBlockX();
		final int z = loc.getBlockZ();
		final WorldGenMineshaftStart wg = new WorldGenMineshaftStart(worldServer, random, x >> 4, z >> 4);
		wg.a(worldServer, new Random(), new StructureBoundingBox(x - 1000, z - 1000, x + 1000, z + 1000));
	}

}
