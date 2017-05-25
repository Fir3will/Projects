package com.hk.json;

public class JsonDouble extends JsonPrimitive
{
	private double value;

	public JsonDouble(double value)
	{
		this(value, true);
	}

	public JsonDouble(boolean mutable)
	{
		this(0, mutable);
	}

	public JsonDouble(double value, boolean mutable)
	{
		super(mutable);
		this.value = value;
	}

	public double getValue()
	{
		return value;
	}

	public JsonDouble setValue(double value)
	{
		if (!isMutable()) throw isntMutable();
		this.value = value;
		return this;
	}

	@Override
	public String toString(boolean format)
	{
		return Double.toString(value);
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonDouble && value == ((JsonDouble) o).value;
	}

	@Override
	public int hashCode()
	{
		return Double.hashCode(value);
	}
}
