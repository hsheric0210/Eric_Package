package com.eric0210.core.api.item;

public class ItemRegisterationException extends Exception
{
	private static final long serialVersionUID = 8734056102591343745L;

	public ItemRegisterationException()
	{
		super("");
	}

	public ItemRegisterationException(final String msg)
	{
		super(msg);
	}

	public ItemRegisterationException(final String msg, final Throwable exception)
	{
		super(msg, exception);
	}

	public ItemRegisterationException(final Throwable exception)
	{
		super(exception);
	}
}
