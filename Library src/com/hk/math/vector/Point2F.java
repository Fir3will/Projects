package com.hk.math.vector;

public class Point2F
{
	public float x, y;

	public Point2F()
	{
		x = y = 0;
	}

	public Point2F(float val)
	{
		x = y = val;
	}

	public Point2F(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Point2F(Point2F p)
	{
		x = p.x;
		y = p.y;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float[] get()
	{
		return new float[] { x, y };
	}

	public Point2F setX(float x)
	{
		this.x = x;
		return this;
	}

	public Point2F setY(float y)
	{
		this.y = y;
		return this;
	}

	public Point2F set(float val)
	{
		x = y = val;
		return this;
	}

	public Point2F set(float x, float y)
	{
		this.x = x;
		this.y = y;
		return this;
	}

	public Point2F set(Point2F p)
	{
		x = p.x;
		y = p.y;
		return this;
	}

	public Point2F set(int pos, float[] arr)
	{
		x = arr[pos];
		y = arr[pos + 1];
		return this;
	}

	public Point2F set(float[] arr)
	{
		x = arr[0];
		y = arr[1];
		return this;
	}
}
