package com.hk.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public final class IOUtil
{
	public static void copyTo(InputStream in, OutputStream out)
	{
		try
		{
			byte[] arr = new byte[1024];
			int read;

			while ((read = in.read(arr)) != -1)
			{
				out.write(arr, 0, read);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static byte[] readAll(InputStream in)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copyTo(in, baos);
		return baos.toByteArray();
	}

	public static String readString(InputStream in)
	{
		return new String(readAll(in));
	}

	public static String readString(InputStream in, String charset)
	{
		try
		{
			return new String(readAll(in), charset);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static String readString(InputStream in, Charset charset)
	{
		return new String(readAll(in), charset);
	}

	private IOUtil()
	{}
}
