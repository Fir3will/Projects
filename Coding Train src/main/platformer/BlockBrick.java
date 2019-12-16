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

public class BlockBrick extends Block
{
	private final BufferedImage[] icons;

	protected BlockBrick()
	{
		super("Brick");
		icons = new BufferedImage[16];
		
		setPrimary();
	}

	public int onAdded(World world, int x, int y, int metadata, boolean primary)
	{
		boolean top = world.get(x, y - 1, primary) == Block.BRICK;
		boolean bottom = world.get(x, y + 1, primary) == Block.BRICK;
		boolean left = world.get(x - 1, y, primary) == Block.BRICK;
		boolean right = world.get(x + 1, y, primary) == Block.BRICK;
		
		if(top && bottom)
		{
			if(right && left)
				metadata = 4;
			else if(right)
				metadata = 3;
			else if(left)
				metadata = 5;
			else
				metadata = 13;
		}
		else if(top)
		{
			if(right && left)
				metadata = 7;
			else if(right)
				metadata = 6;
			else if(left)
				metadata = 8;
			else
				metadata = 14;
		}
		else if(bottom)
		{
			if(right && left)
				metadata = 1;
			else if(right)
				metadata = 0;
			else if(left)
				metadata = 2;
			else
				metadata = 12;
		}
		else
		{
			if(right && left)
				metadata = 10;
			else if(right)
				metadata = 9;
			else if(left)
				metadata = 11;
			else
				metadata = 15;
		}
		return metadata;
	}

	public void onNeighborChange(World world, int x, int y, int dx, int dy, boolean primary)
	{
		world.setMeta(x, y, onAdded(world, x, y, 0, primary), primary);
	}
	
	public BufferedImage getIcon(int metadata)
	{
		return metadata >= 0 && metadata < icons.length ? icons[metadata] : null;
	}

	public void extractImage(BufferedImage tileset)
	{
		for(int i = 0; i < 9; i++)
		{
			icons[i] = get16x16(tileset, 3 + i % 3, 2 + i / 3);
		}
		for(int i = 0; i < 3; i++)
		{
			icons[9 + i] = get16x16(tileset, i, 6);
			icons[12 + i] = get16x16(tileset, 6, i);
		}
		icons[15] = get16x16(tileset, 6, 3);
	}
}
