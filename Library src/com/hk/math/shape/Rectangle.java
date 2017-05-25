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
package com.hk.math.shape;

import com.hk.math.vector.Vector2F;

public class Rectangle extends Shape
{
	public float x, y, width, height;

	public Rectangle()
	{
		x = y = width = height = 0F;
	}

	public Rectangle(float width, float height)
	{
		set(width, height);
	}

	public Rectangle(float x, float y, float width, float height)
	{
		set(x, y, width, height);
	}

	public Rectangle(Rectangle rect)
	{
		set(rect);
	}

	public Rectangle set(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		return this;
	}

	public Rectangle set(float width, float height)
	{
		this.width = width;
		this.height = height;
		return this;
	}

	public Rectangle set(Rectangle rectangle)
	{
		x = rectangle.x;
		y = rectangle.y;
		width = rectangle.width;
		height = rectangle.height;
		return this;
	}

	public float getArea()
	{
		return width * height;
	}

	public float getPerimiter()
	{
		return width + width + height + height;
	}

	public Vector2F getCenter()
	{
		return new Vector2F(x + width / 2F, y + height / 2F);
	}

	public float getCenterX()
	{
		return x + width / 2F;
	}

	public float getCenterY()
	{
		return y + height / 2F;
	}

	public float getMinX()
	{
		return x;
	}

	public float getMaxX()
	{
		return x + width;
	}

	public float getMinY()
	{
		return y;
	}

	public float getMaxY()
	{
		return y + height;
	}

	public void grow(float x, float y)
	{
		this.x -= x;
		this.y -= y;
		width += x + x;
		height += y + y;
	}

	@Override
	public boolean contains(float x, float y)
	{
		return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
	}

	@Override
	public boolean contains(float x, float y, float w, float h)
	{
		return contains(x, y) && contains(x + w, y + h);
	}

	@Override
	public Rectangle clone()
	{
		return new Rectangle(x, y, width, height);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Rectangle)
		{
			Rectangle r = (Rectangle) o;
			return r.x == x && r.y == y && r.width == width && r.height == height;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int hash = 17;
		hash = hash * 31 + Float.floatToIntBits(x);
		hash = hash * 31 + Float.floatToIntBits(y);
		hash = hash * 31 + Float.floatToIntBits(width);
		hash = hash * 31 + Float.floatToIntBits(height);
		return 0;
	}

	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ", " + width + ", " + height + ")";
	}

	private static final long serialVersionUID = 3999762594357009498L;
}
