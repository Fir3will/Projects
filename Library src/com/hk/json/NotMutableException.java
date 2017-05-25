package com.hk.json;

public class NotMutableException extends RuntimeException
{
	public NotMutableException()
	{
		super("The JsonValue isn't mutable");
	}

	private static final long serialVersionUID = 1L;
}
