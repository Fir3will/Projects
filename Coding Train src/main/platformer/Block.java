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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Block
{
	public final String name;
	private boolean onlyPrimary, onlySecondary;
	
	protected Block(String name)
	{
		this.name = name;
	}
	
	public boolean isSolid()
	{
		return true;
	}
	
	public boolean isAir()
	{
		return false;
	}
	
	protected Block setPrimary()
	{
		onlyPrimary = true;
		onlySecondary = false;
		return this;
	}
	
	protected Block setSecondary()
	{
		onlySecondary = true;
		onlyPrimary = false;
		return this;
	}
	
	public boolean onlyPrimary()
	{
		return onlyPrimary;
	}
	
	public boolean onlySecondary()
	{
		return onlySecondary;
	}
	
	public boolean canRender(int metadata)
	{
		return true;
	}

	public void onNeighborChange(World world, int x, int y, int dx, int dy, boolean primary)
	{
		
	}

	public void onRemoved(World world, int x, int y, int metadata, boolean primary)
	{
		
	}

	public int onAdded(World world, int x, int y, int metadata, boolean primary)
	{
		return metadata;
	}

	public int onMetaChange(World world, int x, int y, int metadata, int old, boolean primary)
	{
		return metadata;
	}
	
	public BufferedImage getIcon(int metadata)
	{
		return null;
	}
	
	public void extractImage(BufferedImage tileset)
	{
		
	}
	
	public static BufferedImage get16x16(BufferedImage tileset, int x, int y)
	{
//		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2d = img.createGraphics();
//		g2d.drawImage(tileset.getSubimage(x * 16, y * 16, 16, 16), 0, 0, null);
//		g2d.dispose();
//		return img;
		return tileset.getSubimage(x * 16, y * 16, 16, 16);
	}

	public static final Block AIR;
	public static final Block GRASS;
	public static final Block BRICK;
	public static final Block TILE;
	public static final Block CAVE;
	public static final Block GEM;
	public static final Block[] blocks;
	
	static
	{
		try
		{
			List<Block> lst = new ArrayList<>();
			
			lst.add(AIR = new BlockAir());
			lst.add(GRASS = new BlockGrass());
			lst.add(BRICK = new BlockBrick());
			lst.add(TILE = new BlockTile());
			lst.add(CAVE = new BlockCave());
			lst.add(GEM = new BlockGem());
			
			blocks = lst.toArray(new Block[lst.size()]);
		}
		catch(Exception e)
		{
			throw new RuntimeException("Caught error loading blocks", e);
		}
		
		try
		{
			BufferedImage img = ImageIO.read(new File("tileset.png"));
			for(Block block : blocks)
			{
				block.extractImage(img);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException("Caught error loading textures", e);
		}
	}
}
