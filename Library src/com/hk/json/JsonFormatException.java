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
package com.hk.json;

public class JsonFormatException extends RuntimeException
{
	public JsonFormatException(String input, int index)
	{
		super("Error during parsing: Unexpected char at index: " + index);
		System.out.print(input.substring(0, index));
		System.err.print(input.charAt(index));
		try
		{
			Thread.sleep(10);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println(input.substring(index + 1));
	}

	private static final long serialVersionUID = 1L;
}
