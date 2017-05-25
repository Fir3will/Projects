/**************************************************************************
 *
 * [2017] Fir3will, All Rights Reserved.
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
package main.hexgrid;

import com.hk.math.FloatMath;
import com.hk.math.MathUtil;
import com.hk.math.shape.Polygon;
import com.hk.math.vector.Vector2F;

import main.G2D;

public class HexCell
{
	public final int x, y;
	public final float rx, ry;
	private final float smallR, bigR;
	private final Polygon poly;
	
	public HexCell(int x, int y)
	{
		this.x = x;
		this.y = y;
		poly = new Polygon();
		rx = x * (size * 0.75F);
		ry = y * (cos30 * size) + (x % 2 == 0 ? 0 : (cos30 * (size / 2)));
		bigR = size / 2F;
		smallR = cos30 * bigR; 
		for (int i = 0; i < 6; i++)
		{
			float rad = FloatMath.toRadians(i * 60F);
			float x1 = FloatMath.cos(rad) * bigR + rx;
			float y1 = FloatMath.sin(rad) * bigR + ry;
			poly.addVertex(new Vector2F(x1, y1));
		}
	}
	
	public void updateCell(int ticks)
	{
		
	}
	
	public void paintCell(G2D g2d)
	{
		for(int i = 0; i < poly.vertices.size(); i++)
		{
			Vector2F a = poly.vertices.get(i);
			Vector2F b = i == poly.vertices.size() - 1 ? poly.vertices.get(0) : poly.vertices.get(i + 1);
			g2d.drawLine(a.x, a.y, b.x, b.y);
		}
	}
	
	public boolean contains(float x, float y)
	{
		float dst = MathUtil.hypot(x - rx, y - ry);
		if(dst < smallR)
		{
			return true;
		}
		if(dst > bigR)
		{
			return false;
		}
		return poly.contains(x, y);
	}

	private static final float size = 100F;
	private static final float cos30 = FloatMath.cos(FloatMath.PI / 6F);
}
