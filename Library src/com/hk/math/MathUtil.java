package com.hk.math;

public class MathUtil
{
	//	private static Point2D getCross(Line2D lineA, Line2D lineB)
	//	{
	//		double x1 = lineA.getX1();
	//		double x2 = lineA.getX2();
	//		double y1 = lineA.getY1();
	//		double y2 = lineA.getY2();
	//		double x3 = lineB.getX1();
	//		double x4 = lineB.getX2();
	//		double y3 = lineB.getY1();
	//		double y4 = lineB.getY2();
	//		double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
	//		if (d != 0)
	//		{
	//			double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
	//			double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
	//			return new Point2D.Double(xi, yi);
	//		}
	//		return null;
	//	}

	public static double cube(double a)
	{
		return a * a * a;
	}

	public static float cube(float a)
	{
		return a * a * a;
	}

	public static int cube(int a)
	{
		return a * a * a;
	}

	public static long cube(long a)
	{
		return a * a * a;
	}

	public static double determinant(double m00, double m01, double m10, double m11)
	{
		return m00 * m11 - m10 * m01;
	}

	public static float determinant(float m00, float m01, float m10, float m11)
	{
		return m00 * m11 - m10 * m01;
	}

	public static int determinant(int m00, int m01, int m10, int m11)
	{
		return m00 * m11 - m10 * m01;
	}

	public static long determinant(long m00, long m01, long m10, long m11)
	{
		return m00 * m11 - m10 * m01;
	}

	public static double hypot(double a, double b)
	{
		return Math.sqrt(a * a + b * b);
	}

	public static float hypot(float a, float b)
	{
		return (float) Math.sqrt(a * a + b * b);
	}

	public static double log(double a, double base)
	{
		return Math.log10(a) / Math.log10(base);
	}

	public static float log(float a, float base)
	{
		return (float) (Math.log10(a) / Math.log10(base));
	}

	public static double square(double a)
	{
		return a * a;
	}

	public static float square(float a)
	{
		return a * a;
	}

	public static int square(int a)
	{
		return a * a;
	}

	public static long square(long a)
	{
		return a * a;
	}

	public static double sign(double a)
	{
		return a < 0D ? -1 : a > 0D ? 1 : 0D;
	}

	public static float sign(float a)
	{
		return a < 0F ? -1 : a > 0F ? 1 : 0F;
	}

	public static int sign(int a)
	{
		return a < 0 ? -1 : a > 0 ? 1 : 0;
	}

	public static long sign(long a)
	{
		return a < 0L ? -1L : a > 0L ? 1L : 0L;
	}

	public static short sign(short a)
	{
		return (short) (a < 0 ? -1 : a > 0 ? 1 : 0);
	}

	public static byte sign(byte a)
	{
		return (byte) (a < 0 ? -1 : a > 0 ? 1 : 0);
	}

	public static double clamp(double val, double max, double min)
	{
		return val > max ? max : val < min ? min : val;
	}

	public static float clamp(float val, float max, float min)
	{
		return val > max ? max : val < min ? min : val;
	}

	public static int clamp(int val, int max, int min)
	{
		return val > max ? max : val < min ? min : val;
	}

	public static long clamp(long val, long max, long min)
	{
		return val > max ? max : val < min ? min : val;
	}

	public static short clamp(short val, short max, short min)
	{
		return val > max ? max : val < min ? min : val;
	}

	public static byte clamp(byte val, byte max, byte min)
	{
		return val > max ? max : val < min ? min : val;
	}

	public static long gcd(long a, long b)
	{
		long r = a;
		a = Math.max(a, b);
		b = Math.min(r, b);

		r = b;
		while (a % b != 0)
		{
			r = a % b;
			a = b;
			b = r;
		}
		return r;
	}

	public static int gcd(int a, int b)
	{
		return (int) gcd((long) a, (long) b);
	}

	public static short gcd(short a, short b)
	{
		return (short) gcd((long) a, (long) b);
	}

	public static byte gcd(byte a, byte b)
	{
		return (byte) gcd((long) a, (long) b);
	}

	public static long lcm(long a, long b)
	{
		return a * b / gcd(a, b);
	}

	public static int lcm(int a, int b)
	{
		return (int) lcm((long) a, (long) b);
	}

	public static short lcm(short a, short b)
	{
		return (short) lcm((long) a, (long) b);
	}

	public static byte lcm(byte a, byte b)
	{
		return (byte) lcm((long) a, (long) b);
	}

	public static double lerp(double a, double b, double amt)
	{
		amt = clamp(amt, 1F, 0F);
		return a * amt + b * (1D - amt);
	}

	public static float lerp(float a, float b, float amt)
	{
		amt = clamp(amt, 1F, 0F);
		return a * amt + b * (1F - amt);
	}

	private MathUtil()
	{}
}
