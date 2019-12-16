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

public class JsonLong extends JsonPrimitive
{
	private long value;

	public JsonLong()
	{
		this(0L);
	}

	public JsonLong(long value)
	{
		this.value = value;
	}

	public long get()
	{
		return value;
	}

	public JsonLong set(long value)
	{
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
