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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class InStream implements Stream
{
	private final boolean errorCheck;
	private final InputStream in;
	private boolean closed;

	public InStream(InputStream in)
	{
		this(in, true);
	}
	
	public InStream(InputStream in, boolean errorCheck)
	{
		this.in = in;
		this.errorCheck = errorCheck;
	}

	@Override
	public void writeBoolean(boolean o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeByte(byte o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeShort(short o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeInt(int o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeFloat(float o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeCharacter(char o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeLong(long o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeDouble(double o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeUTFString(String o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeRawString(String o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeSerializable(Serializable o) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public void writeBytes(byte[] arr) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public boolean readBoolean() throws StreamException
	{
		checkType(TYPE_BOOLEAN, "Boolean");
		byte o = read();
		if(o == 37 || o == 66)
		{
			return o == 37;
		}
		throw new StreamException("Corrupted data");
	}

	@Override
	public byte readByte() throws StreamException
	{
		checkType(TYPE_BYTE, "Byte");
		return read();
	}

	@Override
	public short readShort() throws StreamException
	{
		checkType(TYPE_SHORT, "Short");
		short o = 0;
		o |= (read() & 0xFF) << 8;
		o |= (read() & 0xFF) << 0;
		return o;
	}

	@Override
	public int readInt() throws StreamException
	{
		checkType(TYPE_INT, "Integer");
		int o = 0;
		o |= (read() & 0xFF) << 24;
		o |= (read() & 0xFF) << 16;
		o |= (read() & 0xFF) <<  8;
		o |= (read() & 0xFF) <<  0;
		return o;
	}

	@Override
	public float readFloat() throws StreamException
	{
		checkType(TYPE_FLOAT, "Float");
		int o = 0;
		o |= (read() & 0xFF) << 24;
		o |= (read() & 0xFF) << 16;
		o |= (read() & 0xFF) <<  8;
		o |= (read() & 0xFF) <<  0;
		return Float.intBitsToFloat(o);
	}

	@Override
	public char readCharacter() throws StreamException
	{
		checkType(TYPE_CHAR, "Character");
		int o = 0;
		o |= (read() & 0xFF) <<  8;
		o |= (read() & 0xFF) <<  0;
		return (char) o;
	}

	@Override
	public long readLong() throws StreamException
	{
		checkType(TYPE_LONG, "Long");
		long o = 0;
		o |= (read() & 0xFFL) << 56;
		o |= (read() & 0xFFL) << 48;
		o |= (read() & 0xFFL) << 40;
		o |= (read() & 0xFFL) << 32;
		o |= (read() & 0xFFL) << 24;
		o |= (read() & 0xFFL) << 16;
		o |= (read() & 0xFFL) <<  8;
		o |= (read() & 0xFFL) <<  0;
		return o;
	}

	@Override
	public double readDouble() throws StreamException
	{
		checkType(TYPE_DOUBLE, "Double");
		long o = 0;
		o |= (read() & 0xFFL) << 56;
		o |= (read() & 0xFFL) << 48;
		o |= (read() & 0xFFL) << 40;
		o |= (read() & 0xFFL) << 32;
		o |= (read() & 0xFFL) << 24;
		o |= (read() & 0xFFL) << 16;
		o |= (read() & 0xFFL) <<  8;
		o |= (read() & 0xFFL) <<  0;
		return Double.longBitsToDouble(o);
	}

	@Override
	public String readUTFString() throws StreamException
	{
		checkType(TYPE_UTF_STRING, "UTF String");
		int o = 0;
		o |= (read() & 0xFF) << 24;
		o |= (read() & 0xFF) << 16;
		o |= (read() & 0xFF) <<  8;
		o |= (read() & 0xFF) <<  0;
		byte[] arr = new byte[o];
		for(int i = 0; i < o; i++)
		{
			arr[i] = (byte) (~read() & 0xFF);
		}
		return new String(arr, StandardCharsets.UTF_8);
	}

	@Override
	public String readRawString() throws StreamException
	{
		checkType(TYPE_RAW_STRING, "Raw String");
		int o = 0;
		o |= (read() & 0xFF) << 24;
		o |= (read() & 0xFF) << 16;
		o |= (read() & 0xFF) <<  8;
		o |= (read() & 0xFF) <<  0;
		char[] arr = new char[o];
		for(int i = 0; i < o; i++)
		{
			int o2 = 0;
			o2 |= (read() & 0xFF) <<  8;
			o2 |= (read() & 0xFF) <<  0;
			arr[i] = (char) o2;
		}
		return new String(arr);
	}

	@Override
	public <T> T readSerializable(Class<T> cls) throws StreamException
	{
		int len = 0;
		len |= (read() & 0xFF) << 24;
		len |= (read() & 0xFF) << 16;
		len |= (read() & 0xFF) <<  8;
		len |= (read() & 0xFF) <<  0;
		byte[] arr = new byte[len];
		for(int i = 0; i < len; i++)
		{
			arr[i] = (byte) (~read() & 0xFF);
		}
		ByteArrayInputStream bin = new ByteArrayInputStream(arr);
		T o = null;
		try
		{
			ObjectInputStream oin = new ObjectInputStream(bin);
			o = cls.cast(oin.readObject());
			oin.close();
			bin.close();
		}
		catch (IOException | ClassNotFoundException e)
		{
			throw new StreamException(e);
		}
		return o;
	}

	@Override
	public byte[] readBytes() throws StreamException
	{
		checkType(TYPE_BYTES, "Bytes");
		int o = 0;
		o |= (read() & 0xFF) << 24;
		o |= (read() & 0xFF) << 16;
		o |= (read() & 0xFF) <<  8;
		o |= (read() & 0xFF) <<  0;
		byte[] arr = new byte[o];
		for(int i = 0; i < o; i++)
		{
			arr[i] = read();
		}
		return arr;
	}
	
	private void checkType(byte type, String name) throws StreamException
	{
		byte t = errorCheck ? read() : type;
		if(t != type)
		{
			throw new StreamException(name + " not expected, got " + t);
		}
	}

	@Override
	public void close() throws StreamException
	{
		try
		{
			in.close();
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}
		closed = true;
	}

	private byte read() throws StreamException
	{
		if (closed)
		{
			throw new StreamException("Stream was Closed");
		}
		int b = -2;
		try
		{
			b = in.read();
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}
		if (b == -1)
		{
			throw new StreamException("End of Stream");
		}
		if (b == -2)
		{
			throw new StreamException();
		}
		return (byte) b;
	}
}
