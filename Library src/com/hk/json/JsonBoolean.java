package com.hk.json;

public class JsonBoolean extends JsonPrimitive
{
	private boolean value;

	public JsonBoolean(boolean value)
	{
		this(value, true);
	}

	public JsonBoolean(boolean value, boolean mutable)
	{
		super(mutable);
		this.value = value;
	}

	public boolean getValue()
	{
		return value;
	}

	public JsonBoolean setValue(boolean value)
	{
		if (!isMutable()) throw isntMutable();
		this.value = value;
		return this;
	}

	@Override
	public String toString(boolean format)
	{
		return Boolean.toString(value);
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonBoolean && value == ((JsonBoolean) o).value;
	}

	@Override
	public int hashCode()
	{
		return Boolean.hashCode(value);
	}
}
