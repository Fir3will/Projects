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

import com.hk.g2d.G2D;

public class Piece
{
	public final String name;
	
	protected Piece(String name)
	{
		this.name = name;
	}
	
	public void paintPiece(G2D g2d, World world, int x, int y, int xc, int yc)
	{}
	
	public int powerProvided(World world, int x, int y, Side to)
	{
		return 0;
	}
	
	public boolean canTransfer(World world, int x, int y, Side to)
	{
		return false;
	}
	
	public void onNeighborChanged(World world, int x, int y, Side side)
	{
		
	}
	
	public boolean onAdded(World world, int x, int y)
	{
		return true;
	}
	
	public void onMetaChanged(World world, int x, int y, int om, int nm)
	{
	}
	
	public void onDataChanged(World world, int x, int y, Object od, Object nd)
	{
	}
	
	public boolean isAir()
	{
		return this == AIR;
	}
	
	public void onRemove(World world, int x, int y)
	{}

	public void onInteract(World world, int x, int y)
	{}
	
	public int getDefaultMeta()
	{
		return 0;
	}

	public static final Piece[] all;
	public static final Piece AIR = new Piece("Air");
	public static final Piece WIRE = new WirePiece();
	public static final Piece SWITCH = new SwitchPiece();
	public static final Piece AND = new LogicalPiece(true);
	public static final Piece OR = new LogicalPiece(false);
	public static final Piece LIGHT = new LightPiece();
	
	static
	{
		List<Piece> pcs = new ArrayList<>();
		
		pcs.add(AIR);
		pcs.add(WIRE);
		pcs.add(SWITCH);
		pcs.add(AND);
		pcs.add(OR);
		pcs.add(LIGHT);
		
		all = new Piece[pcs.size()];
		pcs.toArray(all);
	}
}
