package com.hk.json;

import java.util.Objects;

public class JsonString extends JsonPrimitive
{
	private String value;

	public JsonString(String value)
	{
		this(value, true);
	}

	public JsonString(boolean mutable)
	{
		this("", mutable);
	}

	public JsonString(String value, boolean mutable)
	{
		super(mutable);
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

	public JsonString setValue(String value)
	{
		if (!isMutable()) throw isntMutable();
		this.value = value;
		return this;
	}

	@Override
	public String toString(boolean format)
	{
		return "\"" + value + "\"";
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonString && value == ((JsonString) o).value;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(value);
	}
}
