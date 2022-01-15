package com.eric0210.core.utils;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardUtil
{

	public static final int getLineNumber(final Scoreboard sb, final String objectiveName, final String line)
	{
		final Objective obj = sb.getObjective(objectiveName);
		final Score s = obj.getScore(line);
		if (s != null)
			return s.getScore();
		return 0;
	}

	public static final int getLineNumber(final Objective obj, final String line)
	{
		final Score s = obj.getScore(line);
		if (s != null)
			return s.getScore();
		return 0;
	}

	public static final String getLine(final Scoreboard sb, final String objectiveName, final int linenumber)
	{
		final Objective obj = sb.getObjective(objectiveName);
		for (final String entry : sb.getEntries())
			if (obj.getScore(entry) != null)
				if (obj.getScore(entry).getScore() == linenumber)
					return entry;
		return "";
	}

	public static final String getLine(final Objective obj, final int linenumber)
	{
		for (final String entry : obj.getScoreboard().getEntries())
			if (obj.getScore(entry) != null)
				if (obj.getScore(entry).getScore() == linenumber)
					return entry;
		return "";
	}
}
