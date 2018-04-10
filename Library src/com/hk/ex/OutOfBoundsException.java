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
package com.hk.ex;

public class OutOfBoundsException extends IllegalArgumentException
{
	public OutOfBoundsException()
	{
		super();
	}

	public OutOfBoundsException(String arg0)
	{
		super(arg0);
	}

	public OutOfBoundsException(Throwable arg0)
	{
		super(arg0);
	}

	public OutOfBoundsException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	private static final long serialVersionUID = -8863697427281645767L;
}
