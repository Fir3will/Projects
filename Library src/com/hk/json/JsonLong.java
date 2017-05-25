package com.hk.json;

public class JsonLong extends JsonPrimitive
{
	private long value;

	public JsonLong(long value)
	{
		this(value, true);
	}

	public JsonLong(boolean mutable)
	{
		this(0, mutable);
	}

	public JsonLong(long value, boolean mutable)
	{
		super(mutable);
		this.value = value;
	}

	public long getValue()
	{
		return value;
	}

	public JsonLong setValue(long value)
	{
		if (!isMutable()) throw isntMutable();
		this.value = value;
		return this;
	}

	@Override
	public String toString(boolean format)
	{
		return Long.toString(value);
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonLong && value == ((JsonLong) o).value;
	}

	@Override
	public int hashCode()
	{
		return Long.hashCode(value);
	}
}
