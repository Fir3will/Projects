package com.hk.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonArray extends JsonValue
{
	final List<JsonValue> values;

	public JsonArray()
	{
		values = new ArrayList<>();
	}

	public JsonArray add(JsonValue value)
	{
		if (value == null)
		{
			value = JsonNull.NULL;
		}
		if (!isMutable()) throw isntMutable();
		values.add(value);
		return this;
	}

	public JsonArray add(String value)
	{
		if (!isMutable()) throw isntMutable();
		values.add(new JsonString(value));
		return this;
	}

	public JsonArray add(double value)
	{
		if (!isMutable()) throw isntMutable();
		values.add(new JsonDouble(value));
		return this;
	}

	public JsonArray add(long value)
	{
		if (!isMutable()) throw isntMutable();
		values.add(new JsonLong(value));
		return this;
	}

	public JsonArray add(boolean value)
	{
		if (!isMutable()) throw isntMutable();
		values.add(new JsonBoolean(value));
		return this;
	}

	public JsonArray add(Object value)
	{
		if (!isMutable()) throw isntMutable();
		values.add(Json.objectToJson(value));
		return this;
	}

	public int size()
	{
		return values.size();
	}

	public JsonValue get(int i)
	{
		return values.get(i);
	}

	public JsonValue remove(int i)
	{
		if (!isMutable()) throw isntMutable();
		return values.remove(i);
	}

	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	public boolean contains(JsonValue value)
	{
		return values.contains(value);
	}

	@Override
	public String toString(boolean format)
	{
		String str = "[" + p("\n", format);
		for (int i = 0; i < values.size(); i++)
		{
			JsonValue val = values.get(i);
			str += p("\t", format) + val.toString(format).replaceAll("\n", "\n\t") + (i == values.size() - 1 ? "" : ",") + p("\n", format);
		}
		return str + "]";
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonArray && Objects.equals(values, ((JsonArray) o).values);
	}

	@Override
	public int hashCode()
	{
		return 53 + Objects.hashCode(values);
	}
}
