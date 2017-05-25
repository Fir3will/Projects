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
package com.hk;

import com.hk.array.ArrayUtil;

public class Replace
{
	public static void replace(String txt, String toReplace, String... options)
	{
		System.out.println(txt);
		for (int i = 0; i < options.length; i++)
		{
			System.out.println(txt.replaceAll(toReplace, options[i]));
		}
	}

	public static void replaceNatives(String txt)
	{
		replace(txt, "<<>>", natives);
	}

	public static void replaceNativeObjs(String txt)
	{
		replace(txt, "<<>>", nativeObjs);
	}

	public static void swapNatives(String txt)
	{
		String str = txt;
		for (int i = 0; i < natives.length; i++)
		{
			str = str.replaceAll(natives[i], "<<>>");
			str = str.replaceAll(nativeObjs[i], natives[i]);
			str = str.replaceAll("<<>>", nativeObjs[i]);
		}
		System.out.println(str);
	}

	public static void swapNative(String txt, int index)
	{
		txt = txt.replaceAll(natives[index], "<<>>");
		txt = txt.replaceAll(nativeObjs[index], natives[index]);
		txt = txt.replaceAll("<<>>", nativeObjs[index]);
		System.out.println(txt);
	}

	public static String[] natives = ArrayUtil.toArray("int", "float", "double", "long", "short", "byte", "char", "boolean");
	public static String[] nativeObjs = ArrayUtil.toArray("Integer", "Float", "Double", "Long", "Short", "Byte", "Character", "Boolean");

	private Replace()
	{}
}
