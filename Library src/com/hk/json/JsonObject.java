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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class JsonObject extends JsonValue implements Iterable<Map.Entry<String, JsonValue>>
{
	private final Map<String, JsonValue> values;

	public JsonObject()
	{
		values = new LinkedHashMap<>();
	}

	public JsonObject set(String name, JsonValue value)
	{
		if (this == value)
			throw new IllegalArgumentException("Can't add this to this");

//		System.out.println("Put '" + name + "' to " + value);
		values.put(name, value == null ? JsonNull.NULL : value);
		return this;
	}

	public JsonObject set(String name, String value)
	{
		set(name, new JsonString(value));
		return this;
	}

	public JsonObject set(String name, long value)
	{
		set(name, new JsonLong(value));
		return this;
	}

	public JsonObject set(String name, double value)
	{
		set(name, new JsonDouble(value));
		return this;
	}

	public JsonObject set(String name, boolean value)
	{
		set(name, new JsonBoolean(value));
		return this;
	}

	public JsonObject set(String name, Object value)
	{
		set(name, Json.objectToJson(value));
		return this;
	}

	public JsonValue get(String name)
	{
		return values.get(name);
	}

	public JsonValue remove(String name)
	{
		return values.remove(name);
	}

	public void removeAll()
	{
		values.clear();
	}

	public boolean containsName(String name)
	{
		return values.containsKey(name);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String toString(boolean format)
	{
		String str = "{" + p("\n", format);
		Entry<String, JsonValue>[] set = values.entrySet().toArray(new Entry[0]);
		for (int i = 0; i < set.length; i++)
		{
			Entry<String, JsonValue> ent = set[i];
			str += p("\t", format) + Json.toString(ent.getKey(), ent.getValue(), format).replaceAll("\n", "\n\t") + (i == set.length - 1 ? "" : ",") + p("\n", format);
		}
		return str + "}";
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonObject && Objects.equals(values, ((JsonObject) o).values);
	}

	@Override
	public int hashCode()
	{
		return 34 + Objects.hashCode(values);
	}
	
	public Iterator<String> keys()
	{
		return values.keySet().iterator();
	}
	
	public Iterator<Map.Entry<String, JsonValue>> iterator()
	{
		return values.entrySet().iterator();
	}
	
	public Iterator<JsonValue> values()
	{
		return values.values().iterator();
	}
}