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
