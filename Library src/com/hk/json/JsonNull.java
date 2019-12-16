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

public class JsonNull extends JsonValue
{
	public static final JsonNull NULL = new JsonNull();

	public JsonNull()
	{}

	@Override
	public String toString(boolean format)
	{
		return "null";
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof JsonNull;
	}

	@Override
	public int hashCode()
	{
		return 6532;
	}
}
