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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.math.shape.Rectangle;

public class World
{
	public final int width, height;
	public boolean generating;
	private final List<Entity> entities, toAdd, toRemove;
	private final Tile[][][] tiles;
	
	public World(int width, int height)
	{
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height][2];
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				for(int i = 0; i < tiles[x][y].length; i++)
				{
					tiles[x][y][i] = new Tile(this, x, y, i);
				}
			}
		}

		entities = new ArrayList<>();
		toAdd = new ArrayList<>();
		toRemove = new ArrayList<>();
	}
	
	public void updateWorld(double delta)
	{
		for(Entity entity : entities)
		{
			entity.updateEntity(delta);
		}
		
		entities.addAll(toAdd);
		entities.removeAll(toRemove);
		toAdd.clear();
		toRemove.clear();
	}
	
	public void renderWorld(G2D g2d, int sx, int ex, int sy, int ey, boolean debug)
	{
		for(int x = sx; x < ex; x++)
		{
			for(int y = sy; y < ey; y++)
			{
				if(debug)
				{
					g2d.setColor(Color.GRAY);
					g2d.drawRect(x * 16, y * 16, 16, 16);
				}

				if(!inBounds(x, y))
					continue;
				for(int i = 0; i < tiles[x][y].length; i++)
				{
					Tile tile = tiles[x][y][i];
					Block block = tile.getBlock();
					int meta = tile.getMeta();
					if(block.canRender(meta))
					{
						g2d.drawImage(block.getIcon(meta), x * 16, y * 16);
					}
				}
			}
		}
		
		g2d.pushMatrix();
		g2d.setColor(Color.PINK);
		g2d.enable(G2D.G_FILL);
		g2d.scale(16, 16);
		for(Entity entity : entities)
		{
			Rectangle r = entity.getBounds();
			g2d.drawRoundRectangle(r.x, r.y, r.width, r.height, 0.2F, 0.2F);
		}
		g2d.disable(G2D.G_FILL);
		g2d.popMatrix();
	}
	
	public Block getPrimary(int x, int y)
	{
		return get(x, y, true);
	}
	
	public Block getSecondary(int x, int y)
	{
		return get(x, y, false);
	}
	
	public Block get(int x, int y, boolean primary)
	{
		if(inBounds(x, y))
		{
			return tiles[x][y][primary ? 0 : 1].getBlock();
		}
		return Block.AIR;
	}
	
	public boolean isAir(int x, int y, boolean primary)
	{
		return get(x, y, primary) == Block.AIR;
	}
	
	public void setPrimary(int x, int y, Block block)
	{
		set(x, y, 0, block, 0);
	}
	
	public void setPrimary(int x, int y, Block block, int metadata)
	{
		set(x, y, 0, block, metadata);
	}
	
	public void setPrimaryAir(int x, int y)
	{
		setPrimary(x, y, Block.AIR);
	}
	
	public void setSecondary(int x, int y, Block block)
	{
		set(x, y, 1, block, 0);
	}
	
	public void setSecondary(int x, int y, Block block, int metadata)
	{
		set(x, y, 1, block, metadata);
	}
	
	public void setSecondaryAir(int x, int y)
	{
		setSecondary(x, y, Block.AIR);
	}
	
	public void set(int x, int y, Block block, boolean primary)
	{
		set(x, y, primary ? 0 : 1, block, -1);
	}
	
	public void set(int x, int y, Block block, int metadata, boolean primary)
	{
		set(x, y, primary ? 0 : 1, block, metadata);
	}
	
	public void setAir(int x, int y, boolean primary)
	{
		if(primary)
		{
			setPrimary(x, y, Block.AIR);
		}
		else
		{
			setSecondary(x, y, Block.AIR);
		}
	}
	
	private void set(int x, int y, int i, Block block, int metadata)
	{
		if(i == 0 && block.onlySecondary())
			return;
		if(i == 1 && block.onlyPrimary())
			return;

		if(inBounds(x, y))
		{
			tiles[x][y][i].setBlock(block, metadata);
		}
	}
	
	public int getPrimaryMeta(int x, int y, boolean primary)
	{
		return getMeta(x, y, true);
	}
	
	public int getSecondaryMeta(int x, int y, boolean primary)
	{
		return getMeta(x, y, false);
	}
	
	public int getMeta(int x, int y, boolean primary)
	{
		return inBounds(x, y) ? tiles[x][y][primary ? 0 : 1].getMeta() : -1;
	}
	
	public void setPrimaryMeta(int x, int y, int metadata, boolean primary)
	{
		setMeta(x, y, metadata, true);
	}
	
	public void setSecondaryMeta(int x, int y, int metadata, boolean primary)
	{
		setMeta(x, y, metadata, false);
	}
	
	public void setMeta(int x, int y, int metadata, boolean primary)
	{
		if(inBounds(x, y))
		{
			tiles[x][y][primary ? 0 : 1].setMeta(metadata);
		}
	}
	
	public void notifyNeighbors(int x, int y, boolean primary)
	{
		int i = primary ? 0 : 1;
		for(int x1 = -1; x1 <= 1; x1++)
		{
			for(int y1 = -1; y1 <= 1; y1++)
			{
				if(x1 == 0 && y1 == 0)
					continue;
				
				if(inBounds(x + x1, y + y1))
				{
					tiles[x + x1][y + y1][i].getBlock().onNeighborChange(this, x + x1, y + y1, -x1, -y1, primary);
				}
			}
		}
	}
	
	public void addEntity(Entity entity)
	{
		toAdd.add(entity);
	}
	
	public void removeEntity(Entity entity)
	{
		toRemove.add(entity);
	}
	
	public boolean inBounds(int x, int y)
	{
		return x >= 0 && x < width && y >= 0 && y < height;
	}
}
