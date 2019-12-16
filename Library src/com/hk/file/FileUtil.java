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
package com.hk.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.hk.io.IOUtil;

public class FileUtil
{
	public static boolean createFile(File file)
	{
		try
		{
			return file.createNewFile();
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static boolean createFile(String file)
	{
		return createFile(new File(file));
	}

	public static boolean createDirectory(File file)
	{
		return file.mkdirs();
	}

	public static boolean createDirectory(String file)
	{
		return createDirectory(new File(file));
	}

	public static boolean deleteFile(File file)
	{
		if (file.isDirectory())
		{
			return deleteDirectory(file);
		}
		else
		{
			return file.delete();
		}
	}

	public static boolean deleteFile(String file)
	{
		return deleteFile(new File(file));
	}

	public static boolean deleteDirectory(File file)
	{
		File[] list = file.listFiles();
		boolean done = true;
		for (File f : list)
		{
			done &= deleteFile(f);
		}
		if(done) file.delete();
		return done;
	}

	public static boolean deleteDirectory(String file)
	{
		return deleteDirectory(new File(file));
	}

	public static void resetFile(File file)
	{
		try
		{
			file.delete();
			file.createNewFile();
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static void resetFile(String file)
	{
		resetFile(new File(file));
	}

	public static void resetFile(File file, String... lines)
	{
		resetFile(file);
		for (String s : lines)
		{
			writeToFile(file, s);
		}
	}
	
	public static void resetFile(String file, byte[] data)
	{
		resetFile(file);
		writeToFile(file, data);
	}
	
	public static void resetFile(File file, byte[] data)
	{
		resetFile(file);
		writeToFile(file, data);
	}

	public static void resetFile(String file, String... lines)
	{
		resetFile(new File(file));
	}

	public static String getFileContents(File file)
	{
		if(!file.exists()) return null;
		try
		{
			FileInputStream in = new FileInputStream(file);
			String s = IOUtil.readString(in);
			in.close();
			return s;
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static String getFileContents(String file)
	{
		return getFileContents(new File(file));
	}

	public static String[] getFileLines(File file)
	{
		return getFileContents(file).split("\n");
	}

	public static String[] getFileLines(String file)
	{
		return getFileContents(file).split("\n");
	}

	public static void writeToFile(File file, byte[] bytes)
	{
		try
		{
			FileOutputStream fout = new FileOutputStream(file, true);
			fout.write(bytes);
			fout.close();
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static void writeToFile(String file, byte[] bytes)
	{
		writeToFile(new File(file), bytes);
	}

	public static void writeToFile(File file, String line)
	{
		writeToFile(file, line.getBytes());
	}

	public static void writeToFile(String file, String line)
	{
		writeToFile(file, line.getBytes());
	}

	public static File getUserDirectory()
	{
		return new File(System.getProperty("user.dir"));
	}

	public static File getHomeDirectory()
	{
		return new File(System.getProperty("user.home"));
	}

	public static List<File> getAllFiles(String dir)
	{
		return getAllFiles(new File(dir));
	}

	public static List<File> getAllFiles(File dir)
	{
		if (!dir.isDirectory())
		{
			throw new IllegalArgumentException("dir isn't a directory");
		}
		List<File> files = new ArrayList<>();

		for (File f : dir.listFiles())
		{
			if (f.isDirectory())
			{
				files.addAll(getAllFiles(f));
			}
			else
			{
				files.add(f);
			}
		}
		return files;
	}

	public static String getFileExtension(String file)
	{
		return getFileExtension(new File(file));
	}

	public static String getFileExtension(File file)
	{
		String s = file.toString();
		return s.substring(s.lastIndexOf(".") + 1, s.length());
	}

	public static byte[] readAll(String file)
	{
		return readAll(new File(file));
	}

	public static byte[] readAll(File file)
	{
		try
		{
			FileInputStream fin = new FileInputStream(file);
			byte[] arr = IOUtil.readAll(fin);
			fin.close();
			return arr;
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static DataTag loadFrom(File file)
	{
		try
		{
			return new DataTag().load(file);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static DataTag loadFromStream(InputStream stream)
	{
		try
		{
			return new DataTag().load(stream);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static void saveTo(File file, DataTag tag)
	{
		try
		{
			FileUtil.deleteFile(file);
			tag.save(file);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	public static void saveToStream(OutputStream file, DataTag tag)
	{
		try
		{
			tag.save(file);
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private FileUtil()
	{}
}
