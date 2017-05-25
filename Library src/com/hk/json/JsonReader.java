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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

class JsonReader
{
	private final JsonValue val;

	public JsonReader(String input)
	{
		input = input.trim();
		StringCharacterIterator itr = new StringCharacterIterator(input);
		val = parse(itr, input);
	}

	private JsonValue parse(StringCharacterIterator itr, String input)
	{
		JsonValue val = null;
		char c = itr.current();
		if (c == '{')
		{
			val = beginObject(itr, input);
		}
		else if (c == '[')
		{
			val = beginArray(itr, input);
		}
		else if (Character.isDigit(c))
		{
			val = beginNumber(itr, input);
		}
		else if (c == '\"')
		{
			val = beginString(itr, input);
		}
		else if (c == 't' || c == 'f')
		{
			val = beginBoolean(itr, input);
		}
		else if (c == 'n')
		{
			val = beginNull(itr, input);
		}

		return val;
	}

	private JsonNull beginNull(StringCharacterIterator itr, String input)
	{
		if (input.substring(itr.getIndex(), itr.getIndex() + 4).equals("null"))
		{
			itr.setIndex(itr.getIndex() + 4);
			return JsonNull.NULL;
		}
		return null;
	}

	private JsonBoolean beginBoolean(StringCharacterIterator itr, String input)
	{
		if (input.substring(itr.getIndex(), itr.getIndex() + 4).equals("true"))
		{
			itr.setIndex(itr.getIndex() + 4);
			return new JsonBoolean(true);
		}
		if (input.substring(itr.getIndex(), itr.getIndex() + 5).equals("false"))
		{
			itr.setIndex(itr.getIndex() + 5);
			return new JsonBoolean(false);
		}
		return null;
	}

	private JsonArray beginArray(StringCharacterIterator itr, String input)
	{
		JsonArray arr = new JsonArray();
		int amt = 1;
		boolean hasVal = true;
		char c;
		while ((c = itr.next()) != CharacterIterator.DONE)
		{
			if (Character.isWhitespace(c))
			{
				continue;
			}
			else if (c == ',' && !hasVal)
			{
				hasVal = true;
				continue;
			}
			else if (c == '[')
			{
				amt++;
			}
			else if (c == ']')
			{
				amt--;
				if (amt == 0)
				{
					break;
				}
			}
			else
			{
				arr.add(parse(itr, input));
				hasVal = false;
			}
		}
		return arr;
	}

	private JsonObject beginObject(StringCharacterIterator itr, String input)
	{
		JsonObject val = new JsonObject();
		String name = null;
		int amt = 1;

		char c;
		while ((c = itr.next()) != CharacterIterator.DONE)
		{
			if (Character.isWhitespace(c))
			{
				continue;
			}
			else if (c == ':' && name != null)
			{
				continue;
			}
			else if (c == ',' && name == null)
			{
				continue;
			}
			else if ((Character.isDigit(c) || c == '-' || c == '+') && name != null)
			{
				val.set(name, beginNumber(itr, input));
				name = null;
				continue;
			}
			else if (c == '\"')
			{
				if (name == null)
				{
					name = beginString(itr, input).getPrimitive().getString().getValue();
					continue;
				}
				else
				{
					val.set(name, beginString(itr, input));
					name = null;
					continue;
				}
			}
			else if (c == 'n')
			{
				val.set(name, beginNull(itr, input));
				name = null;
				continue;
			}
			else if (c == 't' || c == 'f')
			{
				val.set(name, beginBoolean(itr, input));
				name = null;
				continue;
			}
			else if (c == '[')
			{
				if (name != null)
				{
					val.set(name, beginArray(itr, input));
					name = null;
					continue;
				}
			}
			else if (c == '{')
			{
				if (name != null)
				{
					val.set(name, beginObject(itr, input));
					name = null;
					continue;
				}
				else
				{
					amt++;
					continue;
				}
			}
			else if (c == '}')
			{
				amt--;
				if (amt == 0)
				{
					break;
				}
			}
			throw new JsonFormatException(input, itr.getIndex());
		}
		return val;
	}

	private JsonString beginString(StringCharacterIterator itr, String input)
	{
		int start = itr.getIndex();
		int end = -1;
		while (true)
		{
			itr.next();

			if (itr.current() == '\"')
			{
				if (input.charAt(itr.getIndex() - 1) != '\\')
				{
					end = itr.getIndex();
					break;
				}
			}
		}
		if (end != -1) return new JsonString(input.substring(start + 1, end));
		return null;
	}

	private JsonPrimitive beginNumber(StringCharacterIterator itr, String input)
	{
		int begin = itr.getIndex();
		int end = begin + 1;
		boolean isLong = true;
		while (true)
		{
			itr.next();

			if (Character.isDigit(itr.current()))
			{
				end = itr.getIndex() + 1;
			}
			else if (itr.current() == '.')
			{
				if (!isLong)
				{
					end = -1;
					break;
				}
				isLong = false;
			}
			else
			{
				break;
			}
		}
		if (end != -1)
		{
			itr.previous();
			if (isLong)
			{
				return new JsonLong(Long.parseLong(input.substring(begin, end)));
			}
			else
			{
				return new JsonDouble(Double.parseDouble(input.substring(begin, end)));
			}
		}
		return null;
	}

	public JsonValue getObject()
	{
		return val;
	}
}
