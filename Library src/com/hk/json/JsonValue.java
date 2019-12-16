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

public abstract class JsonValue
{
	JsonValue()
	{}

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

	public final JsonPrimitive p()
	{
		return getPrimitive();
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
