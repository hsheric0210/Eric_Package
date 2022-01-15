package com.eric0210.core.api.command;

public class CommandRegisterationException extends Exception
{
	private static final long serialVersionUID = 8734056102591343745L;

	public CommandRegisterationException()
	{
		super("");
	}

	public CommandRegisterationException(final String msg)
	{
		super(msg);
	}

	public CommandRegisterationException(final String msg, final Throwable exception)
	{
		super(msg, exception);
	}

	public CommandRegisterationException(final Throwable exception)
	{
		super(exception);
	}
}
