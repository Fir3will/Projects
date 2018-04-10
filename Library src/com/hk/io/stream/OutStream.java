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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.hk.math.PrimitiveUtil;

public class OutStream implements Stream
{
	private final boolean errorCheck;
	private boolean closed;
	private final OutputStream out;

	public OutStream(OutputStream out)
	{
		this(out, true);
	}

	public OutStream(OutputStream out, boolean errorCheck)
	{
		this.out = out;
		this.errorCheck = errorCheck;
	}

	@Override
	public void writeBoolean(boolean o) throws StreamException
	{
		byte b = (byte) (o ? 1 : 0);

		if(errorCheck) write(TYPE_BOOLEAN);
		write(b);
	}

	@Override
	public void writeByte(byte o) throws StreamException
	{
		if(errorCheck) write(TYPE_BYTE);
		write(o);
	}

	@Override
	public void writeShort(short o) throws StreamException
	{
		if(errorCheck) write(TYPE_SHORT);
		set(PrimitiveUtil.shortToBytes(o));
	}

	@Override
	public void writeInt(int o) throws StreamException
	{
		if(errorCheck) write(TYPE_INT);
		set(PrimitiveUtil.intToBytes(o));
	}

	@Override
	public void writeFloat(float o) throws StreamException
	{
		if(errorCheck) write(TYPE_FLOAT);
		int i = Float.floatToIntBits(o);
		set(PrimitiveUtil.intToBytes(i));
	}

	@Override
	public void writeCharacter(char o) throws StreamException
	{
		if(errorCheck) write(TYPE_CHAR);
		write((byte) o);
	}

	@Override
	public void writeLong(long o) throws StreamException
	{
		if(errorCheck) write(TYPE_LONG);
		set(PrimitiveUtil.longToBytes(o));
	}

	@Override
	public void writeDouble(double o) throws StreamException
	{
		if(errorCheck) write(TYPE_DOUBLE);
		long l = Double.doubleToLongBits(o);
		set(PrimitiveUtil.longToBytes(l));
	}

	@Override
	public void writeUTFString(String o) throws StreamException
	{
		if(errorCheck) write(TYPE_UTF_STRING);
		set(PrimitiveUtil.intToBytes(o.length()));
		byte[] bs = o.getBytes(StandardCharsets.UTF_8);
		set(bs, true);
	}

	@Override
	public void writeSerializable(Serializable o) throws StreamException
	{
		if(errorCheck) write(TYPE_SERIALIZABLE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}

		set(baos.toByteArray(), true);
	}

	@Override
	public void writeBytes(byte[] arr) throws StreamException
	{
		writeBytes(arr, 0, arr.length);
	}

	@Override
	public void writeBytes(byte[] arr, int off, int len) throws StreamException
	{
		if(errorCheck) write(TYPE_BYTES);
		set(Arrays.copyOfRange(arr, off, off + len));
	}

	@Override
	public boolean readBoolean() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public byte readByte() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public short readShort() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public int readInt() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public float readFloat() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public char readCharacter() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public long readLong() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public double readDouble() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public String readUTFString() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public Serializable readSerializable() throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public void readBytes(byte[] arr) throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}

	@Override
	public void readBytes(byte[] arr, int off, int len) throws StreamException
	{
		throw new StreamException("Can't read from this stream");
	}
	
	private void set(byte[] arr) throws StreamException
	{
		set(arr, false);
	}
	
	private void set(byte[] arr, boolean flip) throws StreamException
	{
		for(int i = 0; i < arr.length; i++)
		{
			write(flip ? (byte) ~arr[i] : arr[i]);
		}
	}

	@Override
	public void close() throws StreamException
	{
		try
		{
			out.close();
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}
		closed = true;
	}

	private void write(byte b) throws StreamException
	{
		if (closed)
		{
			throw new StreamException("Stream was Closed");
		}
		try
		{
			out.write(b);
		}
		catch (IOException e)
		{
			throw new StreamException(e);
		}
	}
}
