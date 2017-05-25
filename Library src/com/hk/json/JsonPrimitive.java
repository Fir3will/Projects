package com.hk.json;

abstract class JsonPrimitive extends JsonValue
{
	protected JsonPrimitive()
	{}

	protected JsonPrimitive(boolean mutable)
	{
		super(mutable);
	}

	public final boolean isLong()
	{
		return this instanceof JsonLong;
	}

	public final JsonLong getLong()
	{
		return isLong() ? (JsonLong) this : null;
	}

	public final boolean isDouble()
	{
		return this instanceof JsonDouble;
	}

	public final JsonDouble getDouble()
	{
		return isDouble() ? (JsonDouble) this : null;
	}

	public final boolean isString()
	{
		return this instanceof JsonString;
	}

	public final JsonString getString()
	{
		return isString() ? (JsonString) this : null;
	}

	public final boolean isBoolean()
	{
		return this instanceof JsonBoolean;
	}

	public final JsonBoolean getBoolean()
	{
		return isBoolean() ? (JsonBoolean) this : null;
	}
}
