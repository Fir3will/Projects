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
package com.hk.util;

import java.io.Serializable;
import java.util.Objects;

public class StrReader implements CharSequence, Serializable, Cloneable
{
	public final String str;
	private int position;
	
	public StrReader(String str)
	{
		this(str, 0);
	}
	
	public StrReader(String str, int position)
	{
		this.str = Requirements.requireNotNull(str);
		this.position = position;
	}
	
	public int indx()
	{
		return position;
	}
	
	public boolean hasNext()
	{
		return position < length();
	}
	
	public boolean hasNext(int amt)
	{
		return position + amt < length();
	}
	
	public boolean hasPrev()
	{
		return position > 1;
	}
	
	public boolean hasPrev(int amt)
	{
		return position - amt > 1;
	}
	
	public char next()
	{
		return inBound(position) ? str.charAt(position++) : '\u0000';
	}
	
	public char peek()
	{
		return inBound(position) ? str.charAt(position) : '\u0000';
	}
	
	public char current()
	{
		return inBound(position - 1) ? str.charAt(position - 1) : '\u0000';
	}
	
	public char prev()
	{
		return inBound(position - 2) ? str.charAt(position -= 2) : '\u0000';
	}
	
	public char poll()
	{
		return inBound(position - 2) ? str.charAt(position - 2) : '\u0000';
	}
	
	public String nextString(int amt)
	{
		String s = peekString(amt);
		position += Math.max(0, amt);
		return s;
	}
	
	public String peekString(int amt)
	{
		amt = Math.max(0, amt);
		String s = "";
		for(int i = position; i < position + amt; i++)
		{
			if(!inBound(i)) break;
			s += str.charAt(i);
		}
		return s;
	}
	
	public String prevString(int amt)
	{
		String s = pollString(amt);
		position -= Math.max(0, amt);
		return s;
	}
	
	public String pollString(int amt)
	{
		amt = Math.max(0, amt);
		String s = "";
		for(int i = position - amt - 1; i < position - 1; i++)
		{
			if(!inBound(i)) break;
			s += str.charAt(i);
		}
		return s;
	}
	
	private boolean inBound(int i)
	{
		return i >= 0 && i < str.length();
	}
	
	public StrReader skipWhitespace()
	{
		while(hasNext())
		{
			if(Character.isWhitespace(next()))
				continue;
			break;
		}
		return this;
	}
	
	public int getCurrentLine()
	{
		int line = 0;
		for(int i = 0; i < position; i++)
		{
			if(str.charAt(i) == '\n')
			{
				line++;
			}
		}
		return line;
	}

	@Override
	public char charAt(int indx)
	{
		return str.charAt(indx);
	}

	@Override
	public int length()
	{
		return str.length();
	}
	
	public int charsLeft()
	{
		return length() - position;
	}

	@Override
	public StrReader subSequence(int indx1, int indx2)
	{
		return new StrReader(str.substring(indx1, indx2));
	}
	
	public StrReader clone()
	{
		return new StrReader(str, position);
	}
	
	public String toString()
	{
		return str;
	}
	
	public int hashCode()
	{
		int hash = 19;
		hash = hash * 31 + Objects.hashCode(str);
		hash = hash * 31 + position;
		return hash;
	}
	
	public boolean equals(Object obj)
	{
		return obj instanceof StrReader && ((StrReader) obj).str.equals(str) && ((StrReader) obj).position == position;
	}

	private static final long serialVersionUID = -4642985468639148226L;
}
