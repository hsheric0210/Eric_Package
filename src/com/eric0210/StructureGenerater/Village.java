package com.eric0210.StructureGenerater;

import java.util.Random;

import com.eric0210.core.utils.GameUtils;

import net.minecraft.server.v1_7_R4.StructureBoundingBox;
import net.minecraft.server.v1_7_R4.WorldGenVillageStart;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Location;

public class Village implements IStructure
{
	@Override
	public void generate(final Location pos, final int size)
	{
		final Random random = new Random();
		final WorldServer worldServer = GameUtils.GetHandle.getWorldHandle(pos.getWorld());
		final int x = pos.getBlockX();
		final int z = pos.getBlockZ();
		final WorldGenVillageStart wgv = new WorldGenVillageStart(worldServer, new Random(), x >> 4, z >> 4, size);
		wgv.a(worldServer, random, new StructureBoundingBox(x - 1000, z - 1000, x + 1000, z + 1000));
	}
}
