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

public class BlockGem extends Block
{
	private final BufferedImage[] icons;
	
	protected BlockGem()
	{
		super("Gem");
		icons = new BufferedImage[4];
		
		setSecondary();
	}
	
	public boolean isSolid()
	{
		return false;
	}
	
	public BufferedImage getIcon(int metadata)
	{
		return metadata >= 0 && metadata < icons.length ? icons[metadata] : null;
	}

	public void extractImage(BufferedImage tileset)
	{
		for(int i = 0; i < 4; i++)
		{
			icons[i] = get16x16(tileset, 5 + i % 2, 7 + i / 2);
		}
	}
}
