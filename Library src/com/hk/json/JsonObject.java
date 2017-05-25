/**************************************************************************
 *
 * [2017] Fir3will, All Rights Reserved.
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class JsonObject extends JsonValue
{
	final Map<String, JsonValue> values;

	public JsonObject()
	{
		values = new HashMap<>();
	}

	public JsonObject(boolean mutable)
	{
		super(mutable);
		values = new HashMap<>();
	}

	public JsonObject set(String name, JsonValue value)
	{
		if (value == null)
		{
			value = JsonNull.NULL;
		}
		if (value == this)
		{
			throw new IllegalArgumentException("Can't add this to this");
		}
		if (!isMutable()) throw isntMutable();
		values.put(name, value);
		return this;
	}

	public JsonObject set(String name, String value)
	{
		if (!isMutable()) throw isntMutable();
		values.put(name, new JsonString(value, false));
		return this;
	}

	public JsonObject set(String name, long value)
	{
		if (!isMutable()) throw isntMutable();
		values.put(name, new JsonLong(value, false));
		return this;
	}

	public JsonObject set(String name, double value)
	{
		if (!isMutable()) throw isntMutable();
		values.put(name, new JsonDouble(value, false));
		return this;
	}

	public JsonObject set(String name, boolean value)
	{
		if (!isMutable()) throw isntMutable();
		values.put(name, new JsonBoolean(value, false));
		return this;
	}

	public JsonObject set(String name, Object value)
	{
		if (!isMutable()) throw isntMutable();
		values.put(name, Json.objectToJson(value));
		return this;
	}

	public JsonValue get(String name)
	{
		return values.get(name);
	}

	public JsonValue remove(String name)
	{
		if (!isMutable()) throw isntMutable();
		return values.remove(name);
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
}