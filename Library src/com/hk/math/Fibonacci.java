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

import com.hk.array.ArrayUtil;
import com.hk.array.ImmutableArray;

public class Fibonacci
{
	public static final ImmutableArray<Integer> fibonacciInt = ArrayUtil.immutableArrayOf(getIntFibonnaciArray(47));
	public static final ImmutableArray<Long> fibonacciLong = ArrayUtil.immutableArrayOf(getLongFibonnaciArray(93));

	public static int[] getIntFibonnaciArray(int length)
	{
		int[] fibb = new int[length];
		fibb[0] = 0;
		fibb[1] = 1;
		for (int i = 2; i < fibb.length; i++)
		{
			fibb[i] = fibb[i - 1] + fibb[i - 2];
		}
		return fibb;
	}

	public static long[] getLongFibonnaciArray(int length)
	{
		long[] fibb = new long[length];
		fibb[0] = 0;
		fibb[1] = 1;
		for (int i = 2; i < fibb.length; i++)
		{
			fibb[i] = fibb[i - 1] + fibb[i - 2];
		}
		return fibb;
	}

	public static int getFibonacciInt(int index)
	{
		return fibonacciLong.get(index).intValue();
	}

	public static long getFibonacciLong(int index)
	{
		return fibonacciLong.get(index);
	}

	private Fibonacci()
	{}
}
