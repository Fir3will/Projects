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
package com.hk;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		
	}

	private static byte[] encode(String str)
	{
		byte[] data = str.getBytes();

		for(int i = 0; i < data.length; i++)
		{
			data[i] = (byte) (~data[i] & 255);
		}
		for(int i = 0; i < data.length / 2; i++)
		{
			byte n = data[i];
			data[i] = data[data.length - i - 1];
			data[data.length - i - 1] = n;
		}
		byte b = data[0];
		for(int i = 1; i < data.length; i++)
		{
			int n = data[i] & 0xFF;
			int diff = n - (b & 0xFF);
			b = (byte) n;
			data[i] = (byte) diff;
		}

		return data;
	}

	private static String decode(byte[] data)
	{
		for(int i = 1; i < data.length; i++)
		{
			int n = data[i] & 0xFF;
			int sum = (data[i - 1] & 0xFF) + n;
			data[i] = (byte) sum;
		}
		for(int i = 0; i < data.length / 2; i++)
		{
			byte n = data[i];
			data[i] = data[data.length - i - 1];
			data[data.length - i - 1] = n;
		}
		for(int i = 0; i < data.length; i++)
		{
			data[i] = (byte) (~data[i] & 0xFF);
		}
		
		return new String(data);
	}
}