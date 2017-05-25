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
package com.hk.math.shape;

import java.io.Serializable;
import com.hk.math.vector.Vector2F;

public abstract class Shape implements Serializable, Cloneable
{
	public boolean contains(Vector2F v)
	{
		return contains(v.x, v.y);
	}

	public abstract boolean contains(float x, float y);

	public abstract boolean contains(float x, float y, float w, float h);

	@Override
	public abstract Shape clone();

	@Override
	public abstract boolean equals(Object o);

	@Override
	public abstract int hashCode();

	@Override
	public abstract String toString();

	private static final long serialVersionUID = -4363845090169684027L;
}
