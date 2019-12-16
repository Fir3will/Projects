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

public class JsonBoolean extends JsonPrimitive
{
	private boolean value;

	public JsonBoolean()
	{
		this(false);
	}

	public JsonBoolean(boolean value)
	{
		this.value = value;
	}

	public boolean get()
	{
		return value;
	}

	public JsonBoolean set(boolean value)
	{
		this.value = value;
		return this;
	}

	@Override
	public String toString(boolean format)
	{
		return Boolean.toString(value);
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonBoolean && value == ((JsonBoolean) o).value;
	}

	@Override
	public int hashCode()
	{
		return Boolean.hashCode(value);
	}
}
