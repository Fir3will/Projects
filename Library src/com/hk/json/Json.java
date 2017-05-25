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
			writeObject(json, new FileOutputStream(file));
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
		return null;
	}

	public static Object jsonToObject(JsonValue value)
	{
		if (value instanceof JsonNull || value == null)
		{
			return null;
		}
		else if (value instanceof JsonLong)
		{
			return ((JsonLong) value).getValue();
		}
		else if (value instanceof JsonDouble)
		{
			return ((JsonDouble) value).getValue();
		}
		else if (value instanceof JsonBoolean)
		{
			return ((JsonBoolean) value).getValue();
		}
		else if (value instanceof JsonString)
		{
			return ((JsonString) value).getValue();
		}
		return value;
	}

	private Json()
	{}
}
