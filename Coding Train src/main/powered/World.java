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
package main.powered;

import java.util.ArrayList;
import java.util.List;

public class World
{
//	private final GameScreen game;
	private final Object[][][] grid;
	public final int size;
	private final List<Object[]> updates;
	
	public World(Powered game, int size)
	{
//		this.game = game;
		this.size = size;
		updates = new ArrayList<>();
		grid = new Object[size][size][3];
		for(int x = 0; x < size; x++)
		{
			for(int y = 0; y < size; y++)
			{
				grid[x][y][0] = Piece.AIR;
				grid[x][y][1] = 0;
			}
		}
	}
	
	public void updateWorld()
	{
		Object[] arr = updates.toArray();
		updates.clear();
		
		for(int i = 0; i < arr.length; i++)
		{
			Object[] obj = (Object[]) arr[i];
			int x = (int) obj[0];
			int y = (int) obj[1];
			Side side = (Side) obj[2];
			((Piece) grid[x][y][0]).onNeighborChanged(this, x, y, side);
		}
	}
	
	public Piece getPiece(int x, int y)
	{
		if(!inBounds(x, y))
			return Piece.AIR;
		return (Piece) grid[x][y][0];
	}
	
	public int getMeta(int x, int y)
	{
		if(!inBounds(x, y))
			return 0;
		return (int) grid[x][y][1];
	}
	
	public <T> T getData(int x, int y, Class<T> cls)
	{
		if(!inBounds(x, y))
			return null;
		Object o = grid[x][y][2];
		return o == null ? null : cls.cast(o);
	}
	
	public World setPiece(int x, int y, Piece p, int meta, boolean notify)
	{
		if(inBounds(x, y))
		{
			Piece old = (Piece) grid[x][y][0];
			if(p != old)
			{
				grid[x][y][0] = p;
				grid[x][y][1] = meta;
				old.onRemove(this, x, y);
				if(p.onAdded(this, x, y) && notify)
				{
					notifyNeighbors(x, y);
				}
			}
		}
		return this;
	}
	
	public World setPiece(int x, int y, Piece p, boolean notify)
	{
		return setPiece(x, y, p, p.getDefaultMeta(), notify);
	}
	
	public World setMeta(int x, int y, int meta)
	{
		if(inBounds(x, y))
		{
			int old = (int) grid[x][y][1];
			grid[x][y][1] = meta;
			((Piece) grid[x][y][0]).onMetaChanged(this, x, y, old, meta);
		}
		return this;
	}
	
	public World setData(int x, int y, Object data)
	{
		if(inBounds(x, y))
		{
			Object old = grid[x][y][2];
			grid[x][y][2] = data;
			((Piece) grid[x][y][0]).onDataChanged(this, x, y, old, data);
		}
		return this;
	}
	
	public boolean inBounds(int x, int y)
	{
		return x >= 0 && y >= 0 && x < size && y < size;
	}
	
	public boolean isAir(int x, int y)
	{
		return getPiece(x, y) == Piece.AIR;
	}
	
	public World setToAir(int x, int y, boolean notify)
	{
		setPiece(x, y, Piece.AIR, notify);
		return this;
	}
	
	public boolean isPowered(int x, int y)
	{
		return getPowerTo(x, y) > 0;
	}
	
	public int getPowerTo(int x, int y)
	{
		int pwr = 0;
		for(int i = 0; i < Side.size(); i++)
		{
			Side side = Side.get(i);
			Piece p = getPiece(x + side.xOff, y + side.yOff);
			if(p.canTransfer(this, x + side.xOff, y + side.yOff, side.getOpposite()))
			{
				pwr = Math.max(pwr, p.powerProvided(this, x + side.xOff, y + side.yOff, side.getOpposite()));
			}
		}
		return pwr & 0xF;
	}
	
	public void notifyNeighbors(int x, int y)
	{
		if(inBounds(x + 1, y))
		{
			updates.add(new Object[] { x + 1, y, Side.WEST });
		}
		if(inBounds(x - 1, y))
		{
			updates.add(new Object[] { x - 1, y, Side.EAST });
		}
		if(inBounds(x, y + 1))
		{
			updates.add(new Object[] { x, y + 1, Side.NORTH });
		}
		if(inBounds(x, y - 1))
		{
			updates.add(new Object[] { x, y - 1, Side.SOUTH });
		}
	}
}
