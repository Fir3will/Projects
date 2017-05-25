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
