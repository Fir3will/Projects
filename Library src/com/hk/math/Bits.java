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
package com.hk.math;

import java.io.Serializable;

public class Bits implements Cloneable, Serializable
{
	private int val;
	private final int size;

	public Bits(int val)
	{
		this(val, 8);
	}

	public Bits(int val, int size)
	{
		this.val = val;
		this.size = MathUtil.clamp(size, 32, 1);
	}

	public Bits setBit(int index, boolean b)
	{
		if (index < 0 || index > size)
		{
			throw new OutOfBoundsException(index, size);
		}
		int i = 1 << index;
		if (b)
		{
			val |= i;
		}
		else
		{
			val &= ~i;
		}

		return this;
	}

	public boolean getBit(int index)
	{
		if (index < 0 || index > size)
		{
			throw new OutOfBoundsException(index, size);
		}
		return (val | 1 << index) != 0;
	}

	public Bits toggleBit(int index)
	{
		return setBit(index, !getBit(index));
	}

	@Override
	public Bits clone()
	{
		return new Bits(val, size);
	}

	@Override
	public String toString()
	{
		String s = Integer.toBinaryString(val);
		while (s.length() < size)
		{
			s = '0' + s;
		}
		return s;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Bits && ((Bits) o).val == val && ((Bits) o).size == size;
	}

	@Override
	public int hashCode()
	{
		int hash = 17;
		hash = 19 + hash * val;
		hash = 19 + hash * size;
		return hash;
	}

	private static class OutOfBoundsException extends RuntimeException
	{
		private OutOfBoundsException(int index, int size)
		{
			super("The index must be between 0 and " + size + ". It is " + index + ".");
		}

		private static final long serialVersionUID = -7155366593715852887L;
	}

	private static final long serialVersionUID = -2318678507295309447L;
}
