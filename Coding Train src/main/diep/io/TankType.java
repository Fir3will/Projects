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
package main.diep.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TankType
{
	public final String name;
	protected final List<Barrel> barrels;
	private final List<TankType> children;
	
	private TankType(String name)
	{
		this.name = name;
		barrels = new ArrayList<>();
		children = new LinkedList<>();
	}
	
	private TankType setParent(TankType parent)
	{
		parent.children.add(this);
		return this;
	}
	
	private TankType addBarrels(Barrel... barrels)
	{
		for(Barrel barrel : barrels)
			this.barrels.add(barrel);
		return this;
	}
	
	protected Barrel[] getBarrels()
	{
		return barrels.toArray(new Barrel[barrels.size()]);
	}

	protected List<TankType> getChildren()
	{
		return Collections.unmodifiableList(children);
	}
	
	public static final TankType AI = new TankType("AI");
	public static final TankType BASIC = new TankType("Basic").addBarrels(Barrel.create(45, 25, 30, 0));
	public static final TankType TWIN = new TankType("Twin").addBarrels(Barrel.create(45, 25, 30, 0, 0, -10), Barrel.create(45, 25, 30, 0, 0, 10));
	public static final TankType TRIPLET = new TankType("Triplet").addBarrels(Barrel.create(45, 25, Vehicle.RADIUS, 0), Barrel.create(45, 25, Vehicle.RADIUS, -45), Barrel.create(45, 25, Vehicle.RADIUS, 45));
	public static final TankType QUAD_TANK = new TankType("Quad Tank").addBarrels(Barrel.create(45, 25, Vehicle.RADIUS, 0), Barrel.create(45, 25, Vehicle.RADIUS, -90), Barrel.create(45, 25, Vehicle.RADIUS, 90), Barrel.create(45, 25, Vehicle.RADIUS, 180));
}
