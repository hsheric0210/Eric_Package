package com.eric0210.StructureGenerater;

import org.bukkit.Location;

public class Generater
{
	public enum StructureType
	{
		MINE_SHAFT(new MineShaft(), "mineshaft", "mine_shaft"),
		VILLAGE(new Village(), "village"),
		SANDBOX(new SandBox(), "sandbox"),
		NETHER_STRONGHOLD(new NetherStrongHold(), "netherstronghold", "nether_stronghold", "hellstronghold", "hell_stronghold"),

		WITCH_HUT(new WitchHut(), "witchhut", "witch_hut"),

		PYRAMID(new Pyramid(), "pyramid"),

		JUNGLE_TEMPLE(new JungleTemple(), "jungletemple", "jungle_temple");

		final IStructure structure;
		final String[] aliases;

		StructureType(final IStructure s, final String... aliases)
		{
			structure = s;
			this.aliases = aliases;
		}

		public void generate(final Location loc, final int size)
		{
			int s = size;
			if (size == 0)
				s = 1;

			if (s > 20)
				s = 20;
			structure.generate(loc, s);
		}
	}

	public Generater(final StructureGenerater sg)
	{
	}

	public void generate(final StructureType type, final Location loc, final int size)
	{
		type.generate(loc, size);
	}

	public static StructureType toStructureType(final String searchFor)
	{
		for (final StructureType type : StructureType.values())
			for (final String alias : type.aliases)
				if (alias.equalsIgnoreCase(searchFor))
					return type;

		return null;
	}
}
