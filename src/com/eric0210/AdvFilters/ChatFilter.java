package com.eric0210.AdvFilters;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

public class ChatFilter
{
	public static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);
	private final List<Character> censorWith;
	private final List<String> filteredWords;

	public ChatFilter()
	{
		censorWith = AdvFilters.instance().getCensorWith();
		filteredWords = AdvFilters.instance().getFilteredWords();
	}

	public String check(final Player sender, final String chat)
	{
		if (chat != null && sender != null)
		{
			final String tmp = filterWords(chat);
			return tmp;
		}
		return "";
	}

	public boolean isFilteredWord(final String word)
	{
		for (final String filtered : filteredWords)
			if (filtered.equalsIgnoreCase(word))
				return true;
		return false;
	}

	public String filterWords(final String chat)
	{
		final Random random = new Random();
		final String[] words = PATTERN_ON_SPACE.split(chat);

		final StringBuilder builder = new StringBuilder();
		final int index = 0;
		for (final String word : words)
		{
			final int wordLength = word.length();

			if (isFilteredWord(word))
				for (int i = 1; i <= wordLength; i++)
					builder.append(censorWith.get(random.nextInt(censorWith.size())));
			else
			{
				final String censored = censorFilteredWords(word);
				if (!censored.equals(word))
					builder.append(censored);
				else
					builder.append(word);
			}
			builder.append(" ");
		}

		return builder.toString();
	}

	public String censorFilteredWords(final String word)
	{
		final Random random = new Random();
		final StringBuilder builder = new StringBuilder(word);

		for (int i = 0, j = word.length(); i < j; i++)
		{
			final String wordPiece = word.substring(i);
			for (final String filteredWord : filteredWords)
			{
				final int filteredWordLength = filteredWord.length();

				if (wordPiece.length() >= filteredWordLength && filteredWord.equalsIgnoreCase(wordPiece.substring(0, filteredWordLength)))
				{
					final StringBuilder censor = new StringBuilder(filteredWordLength);
					for (int k = 0; k < filteredWordLength; k++)
						censor.append(censorWith.get(random.nextInt(censorWith.size())));

					builder.replace(i, i + filteredWord.length(), censor.toString());
				}
			}
		}
		return builder.toString();
	}
}
