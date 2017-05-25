package com.hk.json;

public abstract class JsonValue
{
	private boolean mutable;

	protected JsonValue()
	{
		this(true);
	}

	protected JsonValue(boolean mutable)
	{
		this.mutable = true;
	}

	protected final NotMutableException isntMutable()
	{
		return new NotMutableException();
	}

	public final boolean isMutable()
	{
		return mutable;
	}

	public final JsonValue setMutable()
	{
		mutable = true;
		return this;
	}

	public final boolean isArray()
	{
		return this instanceof JsonArray;
	}

	public final JsonArray getArray()
	{
		return isArray() ? (JsonArray) this : null;
	}

	public final boolean isPrimitive()
	{
		return this instanceof JsonPrimitive;
	}

	public final JsonPrimitive getPrimitive()
	{
		return isPrimitive() ? (JsonPrimitive) this : null;
	}

	public final boolean isObject()
	{
		return this instanceof JsonObject;
	}

	public final JsonObject getObject()
	{
		return isObject() ? (JsonObject) this : null;
	}

	public final boolean isNull()
	{
		return this instanceof JsonNull;
	}

	public final JsonNull getNull()
	{
		return isNull() ? (JsonNull) this : null;
	}

	public abstract String toString(boolean format);

	@Override
	public String toString()
	{
		return toString(false);
	}

	@Override
	public abstract boolean equals(Object o);

	@Override
	public abstract int hashCode();

	protected final String p(String str, boolean format)
	{
		return format ? str : "";
	}
}
