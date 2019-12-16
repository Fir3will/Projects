/**************************************************************************
 *
 * [2019] Fir3will, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of "Fir3will" and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to "Fir3will" and its suppliers
 * and may be covered by U.S. and Foreign Patents, patents
 * in process, and are protected by trade secret or copyright law
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from "Fir3will".
 **************************************************************************/
package com.hk.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class JsonArray extends JsonValue implements Iterable<JsonValue>
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
		values.add(value);
		return this;
	}

	public JsonArray add(String value)
	{
		add(new JsonString(value));
		return this;
	}

	public JsonArray add(double value)
	{
		add(new JsonDouble(value));
		return this;
	}

	public JsonArray add(long value)
	{
		add(new JsonLong(value));
		return this;
	}

	public JsonArray add(boolean value)
	{
		values.add(new JsonBoolean(value));
		return this;
	}

	public JsonArray add(Object value)
	{
		add(Json.objectToJson(value));
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

	@Override
	public Iterator<JsonValue> iterator()
	{
		return new Itr(values);
	}
	
	private static class Itr implements Iterator<JsonValue>
	{
		private final List<JsonValue> values;
		private int index = 0;
		
		private Itr(List<JsonValue> values)
		{
			this.values = new ArrayList<>(values);
		}
		
		@Override
		public boolean hasNext()
		{
			return index < values.size();
		}

		@Override
		public JsonValue next()
		{
			return values.get(index++);
		}
	}
}
