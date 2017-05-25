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

import java.util.Arrays;
import java.util.Objects;
import com.hk.array.ArrayUtil;

public class TupleN<T>
{
	T[] arr;

	public TupleN()
	{
		arr = null;
	}

	public TupleN(int size)
	{
		arr = ArrayUtil.toArray((T) null);
		arr = ArrayUtil.growArrayBy(arr, size - 1);
	}

	public TupleN(T[] arr)
	{
		this.arr = arr;
	}

	@SuppressWarnings("unchecked")
	public TupleN(T obj1, T... arr)
	{
		this(arr.length + 1);
		for (int i = 0; i <= arr.length; i++)
		{
			if (i == 0) this.arr[0] = obj1;
			else
			{
				this.arr[i] = arr[i - 1];
			}
		}
	}

	public int size()
	{
		return arr.length;
	}

	public T getObj(int index)
	{
		return arr[index];
	}

	public T getObjOr(int index, T def)
	{
		return getObj(index) == null ? def : getObj(index);
	}

	public TupleN<T> setObj(int index, T obj)
	{
		arr[index] = obj;
		return this;
	}

	@Override
	public String toString()
	{
		String str = Arrays.toString(arr);
		return "{" + str.substring(1, str.length() - 2) + "}";
	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(arr);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj != null && TupleN.class.equals(obj.getClass()) && Objects.deepEquals(this.arr, ((TupleN<?>) obj).arr);
	}
}
