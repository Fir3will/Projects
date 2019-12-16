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
package com.hk.ex;

public class InvalidInputException extends Exception
{	
	public InvalidInputException()
	{
	}

	public InvalidInputException(String message)
	{
		super(message);
	}

	public InvalidInputException(Throwable cause)
	{
		super(cause);
	}

	public InvalidInputException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	private static final long serialVersionUID = 8793656911329713609L;
}
