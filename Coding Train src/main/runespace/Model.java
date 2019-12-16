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
package main.runespace;

import java.io.File;

public enum Model
{
	TEMPLATE(1, 32, 72, 0x7F7F7F),
	WARRIOR_M(1, 16, 180, 0x7BD5FE),
	MAGICIAN_M(1, 64, 180, 0x7BD5FE),
	HEALER_M(1, 112, 180, 0x7BD5FE),
	NINJA_M(1, 160, 180, 0x7BD5FE),
	RANGER_M(1, 208, 180, 0x7BD5FE),
	TOWNSFOLK_M(1, 256, 180, 0x7BD5FE),
	WARRIOR_F(1, 16, 306, 0xF68BCD),
	MAGICIAN_F(1, 64, 306, 0xF68BCD),
	HEALER_F(1, 112, 306, 0xF68BCD),
	NINJA_F(1, 160, 306, 0xF68BCD),
	RANGER_F(1, 208, 306, 0xF68BCD),
	TOWNSFOLK_F(1, 256, 306, 0xF68BCD);

	public final ModelData mdlData;
	public final int background;
	
	Model(int sheet, int tx, int ty, int background)
	{
		File charaset = new File(getAssetFile(), "charaset (" + sheet + ").png");
		mdlData = new ModelData(charaset, tx, ty);
		this.background = background | 0xFF000000;
	}

	public static int size()
	{
		return values().length;
	}

	public static Model get(int i)
	{
		return values()[i];
	}

	public static File getAssetFile()
	{
//		return new File("things" + File.separatorChar + "runespace");
		return new File("runespace");
	}

	public class ModelData
	{
		final File charaset;
		final int tx, ty;

		private ModelData(File charaset, int tx, int ty)
		{
			this.charaset = charaset;
			this.tx = tx;
			this.ty = ty;
		}
	}
}
