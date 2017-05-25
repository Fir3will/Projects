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

import java.io.Closeable;
import java.io.Serializable;

public interface Stream extends Closeable
{
	public void writeBoolean(boolean o) throws StreamException;

	public void writeByte(byte o) throws StreamException;

	public void writeShort(short o) throws StreamException;

	public void writeInt(int o) throws StreamException;

	public void writeFloat(float o) throws StreamException;

	public void writeCharacter(char o) throws StreamException;

	public void writeLong(long o) throws StreamException;

	public void writeDouble(double o) throws StreamException;

	public void writeUTFString(String o) throws StreamException;

	public void writeSerializable(Serializable o) throws StreamException;

	public void writeBytes(byte[] arr) throws StreamException;

	public void writeBytes(byte[] arr, int off, int len) throws StreamException;

	public boolean readBoolean() throws StreamException;

	public byte readByte() throws StreamException;

	public short readShort() throws StreamException;

	public int readInt() throws StreamException;

	public float readFloat() throws StreamException;

	public char readCharacter() throws StreamException;

	public long readLong() throws StreamException;

	public double readDouble() throws StreamException;

	public String readUTFString() throws StreamException;

	public Serializable readSerializable() throws StreamException;

	public void readBytes(byte[] arr) throws StreamException;

	public void readBytes(byte[] arr, int off, int len) throws StreamException;

	@Override
	public void close() throws StreamException;

	static final byte TYPE_BOOLEAN = 0;
	static final byte TYPE_BYTE = 1;
	static final byte TYPE_SHORT = 2;
	static final byte TYPE_INT = 3;
	static final byte TYPE_FLOAT = 4;
	static final byte TYPE_CHAR = 5;
	static final byte TYPE_LONG = 6;
	static final byte TYPE_DOUBLE = 7;
	static final byte TYPE_UTF_STRING = 8;
	static final byte TYPE_SERIALIZABLE = 9;
	static final byte TYPE_BYTES = 10;
}
