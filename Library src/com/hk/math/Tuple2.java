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
package com.hk.math;

import java.util.Objects;

public class Tuple2<T> extends Tuple1<T>
{
	T obj2;

	public Tuple2()
	{
		super();
		obj2 = null;
	}

	public Tuple2(T[] arr)
	{
		super(arr);
		obj2 = arr[1];
	}

	public Tuple2(Tuple2<T> copy)
	{
		super(copy);
		this.obj2 = copy.obj2;
	}

	public Tuple2(T obj1, T obj2)
	{
		super(obj1);
		this.obj2 = obj2;
	}

	public Tuple2(T obj)
	{
		super(obj);
		obj2 = obj;
	}

	public Tuple1<T> setObj2(T obj2)
	{
		this.obj2 = obj2;
		return this;
	}

	public T getObj2()
	{
		return obj2;
	}

	public T getObj2Or(T def)
	{
		return obj2 == null ? def : obj2;
	}

	@Override
	public String toString()
	{
		return "{" + Objects.toString(obj1) + ", " + Objects.toString(obj2) + "}";
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj != null && Tuple2.class.equals(obj.getClass()) && Objects.deepEquals(((Tuple2<?>) obj).obj1, obj1) && Objects.deepEquals(((Tuple2<?>) obj).obj2, obj2);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(obj1, obj2);
	}

	@Override
	public Tuple2<T> copy()
	{
		return new Tuple2<T>(obj1, obj2);
	}
}
