package com.hk.json;

public class JsonFormatException extends RuntimeException
{
	public JsonFormatException(String input, int index)
	{
		super("Error during parsing: Unexpected char at index: " + index);
		System.out.print(input.substring(0, index));
		System.err.print(input.charAt(index));
		try
		{
			Thread.sleep(10);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println(input.substring(index + 1));
	}

	private static final long serialVersionUID = 1L;
}
