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
package com.hk.io.stream;

import java.io.IOException;

public class StreamException extends IOException
{
	public StreamException()
	{
		super();
	}

	public StreamException(String message)
	{
		super(message);
	}

	public StreamException(Throwable e)
	{
		super(e);
	}

	public StreamException(String message, Throwable e)
	{
		super(message, e);
	}

	private static final long serialVersionUID = -449520458559123989L;
}
