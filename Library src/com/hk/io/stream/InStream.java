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
package com.hk.io.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import com.hk.math.PrimitiveUtil;

public class InStream implements Stream
{
	private final boolean errorCheck;
	private boolean closed;
	private final InputStream in;

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
	public void writeBytes(byte[] arr, int off, int len) throws StreamException
	{
		throw new StreamException("Can't write to this stream");
	}

	@Override
	public boolean readBoolean() throws StreamException
	{
		byte type = read();
		if (type == TYPE_BOOLEAN)
		{
			return read() == 1;
		}
		throw new StreamException("Boolean not expected");
	}

	@Override
	public byte readByte() throws StreamException
	{
		byte type = read();
		if (type == TYPE_BYTE)
		{
			return read();
		}
		throw new StreamException("Byte not expected");
	}

	@Override
	public short readShort() throws StreamException
	{
		byte type = read();
		if (type == TYPE_SHORT)
		{
			byte a = read();
			byte b = read();
			return (short) (a | (b & 0xFF) << 8);
		}
		throw new StreamException("Short not expected");
	}

	@Override
	public int readInt() throws StreamException
	{
		checkType(TYPE_INT, "Integer");
		return PrimitiveUtil.bytesToInt(0, get(4));
	}

	@Override
	public float readFloat() throws StreamException
	{
		checkType(TYPE_FLOAT, "Float");
		return Float.intBitsToFloat(PrimitiveUtil.bytesToInt(0, get(4)));
	}

	@Override
	public char readCharacter() throws StreamException
	{
		checkType(TYPE_CHAR, "Character");
		return (char) read();
	}

	@Override
	public long readLong() throws StreamException
	{
		checkType(TYPE_LONG, "Long");
		return PrimitiveUtil.bytesToLong(0, get(8));
	}

	@Override
	public double readDouble() throws StreamException
	{
		checkType(TYPE_DOUBLE, "Double");
		return Double.longBitsToDouble(PrimitiveUtil.bytesToLong(0, get(8)));
	}

	@Override
	public String readUTFString() throws StreamException
	{
		checkType(TYPE_UTF_STRING, "UTF String");
		int length = PrimitiveUtil.bytesToInt(0, get(4));
		byte[] arr = get(length, true);
		return new String(arr, StandardCharsets.UTF_8);
	}

	@Override
	public Serializable readSerializable() throws StreamException
	{
		checkType(TYPE_SERIALIZABLE, "Serializable");
		int length = PrimitiveUtil.bytesToInt(0, get(4));
		byte[] arr = get(length, true);
		ByteArrayInputStream bais = new ByteArrayInputStream(arr);
		Serializable o = null;
		try
		{
			ObjectInputStream ois = new ObjectInputStream(bais);
			o = (Serializable) ois.readObject();
			ois.close();
		}
		catch (Exception e)
		{
			throw new StreamException(e);
		}
		return o;
	}

	@Override
	public void readBytes(byte[] arr) throws StreamException
	{
		readBytes(arr, 0, arr.length);
	}

	@Override
	public void readBytes(byte[] arr, int off, int len) throws StreamException
	{
		checkType(TYPE_BYTES, "Bytes");
		for (int i = off; i < off + len; i++)
		{
			arr[i] = read();
		}
	}
	
	private byte[] get(int amt) throws StreamException
	{
		return get(amt, false);
	}
	
	private byte[] get(int amt, boolean flip) throws StreamException
	{
		byte[] arr = new byte[amt];
		for(int i = 0; i < amt; i++)
		{
			arr[i] = (flip ? (byte) ~read() : read());
		}
		return arr;
	}
	
	private void checkType(byte type, String name) throws StreamException
	{
		if(errorCheck && read() != type) throw new StreamException(name + " not expected");
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
