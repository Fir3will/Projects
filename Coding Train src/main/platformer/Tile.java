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

public class Tile
{
	public final World world;
	public final int x, y;
	public final boolean primary;
	private Block block;
	private int metadata;
	
	public Tile(World world, int x, int y, int i)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		primary = i == 0;
		block = Block.AIR;
		metadata = 0;
	}
	
	public Tile setBlock(Block block, int metadata)
	{
		if(this.block != block)
		{
			this.block.onRemoved(world, x, y, this.metadata, primary);
			this.block = block;
			this.metadata = this.block.onAdded(world, x, y, metadata, primary);
			
			if(!world.generating)
				world.notifyNeighbors(x, y, primary);
		}
		return this;
	}
	
	public Tile setMeta(int metadata)
	{
		if(this.metadata != metadata)
		{
			this.metadata = this.block.onMetaChange(world, x, y, metadata, this.metadata, primary);
		}
		return this;
	}
	
	public Block getBlock()
	{
		return block;
	}
	
	public int getMeta()
	{
		return metadata;
	}
}
