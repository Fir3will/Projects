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
package main.platformer;

import java.awt.image.BufferedImage;

public class BlockCave extends Block
{
	private BufferedImage icon;
	
	protected BlockCave()
	{
		super("Cave");
		setPrimary();
	}
	
	public BufferedImage getIcon(int metadata)
	{
		return metadata == 0 ? icon : null;
	}
	
	public void extractImage(BufferedImage tileset)
	{
		icon = get16x16(tileset, 4, 1);
	}
}
