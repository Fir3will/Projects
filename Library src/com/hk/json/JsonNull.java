package com.hk.json;

public class JsonNull extends JsonValue
{
	public static final JsonNull NULL = new JsonNull();

	public JsonNull()
	{
		super(false);
	}

	@Override
	public String toString(boolean format)
	{
		return "null";
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonNull;
	}

	@Override
	public int hashCode()
	{
		return 6532;
	}
}
