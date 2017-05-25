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
package com.hk.array;

import java.lang.reflect.Array;
import java.util.Iterator;

public final class ImmutableArray<T> implements Iterable<T>
{
	private final Object array;
	public final int length;

	public ImmutableArray(Object array)
	{
		if (array.getClass().getComponentType() != null)
		{
			this.array = array;
			this.length = Array.getLength(array);
		}
		else
		{
			throw new IllegalArgumentException("array must actually be an array, " + array);
		}
	}

	@SuppressWarnings("unchecked")
	public T get(int index)
	{
		return (T) Array.get(array, index);
	}

	@SuppressWarnings("unchecked")
	public T[] toArray()
	{
		T[] arr = (T[]) Array.newInstance(array.getClass().getComponentType(), length);
		for (int i = 0; i < length; i++)
		{
			arr[i] = get(i);
		}
		return arr;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Itr();
	}

	private class Itr implements Iterator<T>
	{
		private int index;

		@Override
		public boolean hasNext()
		{
			return index < length;
		}

		@Override
		public T next()
		{
			return get(index++);
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException("This array cannot be modified.");
		}
	}
}
