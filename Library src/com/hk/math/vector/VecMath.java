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
package com.hk.math.vector;

public class VecMath
{
	public static float distanceToSegment(Vector2F v1, Vector2F v2, Vector2F point)
	{
		return closestToSegment(v1, v2, point).distance(point);
	}
	
	public static Vector2F closestToSegment(Vector2F v1, Vector2F v2, Vector2F point)
	{
		float xDelta = v2.getX() - v1.getX();
		float yDelta = v2.getY() - v1.getY();

		if ((xDelta == 0) && (yDelta == 0))
		{
		    throw new IllegalArgumentException("p1 and p2 cannot be the same point");
		}

		float u = ((point.getX() - v1.getX()) * xDelta + (point.getY() - v1.getY()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

		final Vector2F closestPoint;
		if (u < 0)
		{
		    closestPoint = v1;
		}
		else if (u > 1)
		{
		    closestPoint = v2;
		}
		else
		{
		    closestPoint = new Vector2F(v1.getX() + u * xDelta, v1.getY() + u * yDelta);
		}

		return closestPoint;
	}
	
	private VecMath()
	{}
}
