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
package com.hk.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import com.hk.io.IOUtil;

public class Json
{
	public static JsonValue readObject(File file)
	{
		try
		{
			InputStream in = new FileInputStream(file);
			JsonValue jv = readObject(in);
			in.close();
			return jv;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static JsonValue readObject(InputStream is)
	{
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			IOUtil.copyTo(is, out);
			return readObject(out.toString());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static JsonValue readObject(Reader is)
	{
		try
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int b = -1;

			while ((b = is.read()) != -1)
			{
				out.write(b);
			}
			return readObject(out.toString());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static JsonValue readObject(String str)
	{
		return new JsonReader(str).getObject();
	}

	public static void writeObject(JsonValue json, File file)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(file);
			writeObject(json, fos);
			fos.close();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void writeObject(JsonValue json, OutputStream os)
	{
		try
		{
			os.write(json.toString().getBytes());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void writeObject(JsonValue json, Writer os)
	{
		try
		{
			os.write(json.toString());
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	static String toString(String name, JsonValue value, boolean format)
	{
		return "\"" + name + "\":" + (format ? " " : "") + value.toString(format);
	}

	public static JsonValue objectToJson(Object value)
	{
		if (value == null)
		{
			return JsonNull.NULL;
		}
		else if (value instanceof Number)
		{
			Number n = (Number) value;
			if ((long) n.doubleValue() == n.doubleValue())
			{
				return new JsonLong(n.longValue());
			}
			else
			{
				return new JsonDouble(n.doubleValue());
			}
		}
		else if (value instanceof Boolean)
		{
			return new JsonBoolean((boolean) value);
		}
		else if (value instanceof JsonValue)
		{
			return (JsonValue) value;
		}
		else if (value instanceof String || value instanceof Character)
		{
			return new JsonString(String.valueOf(value));
		}
		return JsonSerializer.toJson(value);
	}

	public static Object jsonToObject(Class<?> cls, JsonValue value)
	{
		if (value == null || value.isNull())
		{
			return null;
		}
		else if (value instanceof JsonLong)
		{
			return ((JsonLong) value).get();
		}
		else if (value instanceof JsonDouble)
		{
			return ((JsonDouble) value).get();
		}
		else if (value instanceof JsonBoolean)
		{
			return ((JsonBoolean) value).get();
		}
		else if (value instanceof JsonString)
		{
			return ((JsonString) value).get();
		}
		return JsonSerializer.fromJson(value, cls);
	}

	private Json()
	{}
}
