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
package com.hk.g2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class G2D
{
	private int flags;
	public final int width, height;
	private final List<AffineTransform> matrices;
	private final List<Color> colors;
	private final List<Font> fonts;
	public Graphics2D g2d;

	public G2D(int width, int height)
	{
		this.width = width;
		this.height = height;
		matrices = new ArrayList<>();
		colors = new ArrayList<>();
		fonts = new ArrayList<>();
	}

	public G2D reset()
	{
		flags = 0;
		matrices.clear();
		colors.clear();
		fonts.clear();
		return this;
	}

	public G2D enable(int bit)
	{
		flags |= bit;
		return this;
	}

	public boolean isEnabled(int bit)
	{
		return (flags & bit) != 0;
	}

	public G2D disable(int bit)
	{
		flags &= ~bit;
		return this;
	}

	public G2D pushMatrix()
	{
		matrices.add(g2d.getTransform());
		return this;
	}

	public AffineTransform getMatrix()
	{
		return g2d.getTransform();
	}

	public G2D setMatrix(AffineTransform matrix)
	{
		g2d.setTransform(matrix);
		return this;
	}

	public G2D popMatrix()
	{
		g2d.setTransform(matrices.remove(matrices.size() - 1));
		return this;
	}

	public G2D pushColor()
	{
		colors.add(getColor());
		return this;
	}

	public Color getColor()
	{
		return g2d.getColor();
	}

	public G2D setColor(int r, int g, int b)
	{
		return setColor(r / 255F, g / 255F, b / 255F, 1F);
	}

	public G2D setColor(int r, int g, int b, int a)
	{
		return setColor(r / 255F, g / 255F, b / 255F, a / 255F);
	}

	public G2D setColor(float r, float g, float b)
	{
		return setColor(r, g, b, 1F);
	}

	public G2D setColor(float r, float g, float b, float a)
	{
		return setColor(new Color(r, g, b, a));
	}

	public G2D setColor(int rgb)
	{
		return setColor(rgb, false);
	}

	public G2D setColor(int argb, boolean alpha)
	{
		return setColor(new Color(argb, alpha));
	}

	public G2D setColor(Color color, float alpha)
	{
		return setColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha);
	}

	public G2D setColor(Color color)
	{
		g2d.setColor(color);
		return this;
	}

	public G2D popColor()
	{
		setColor(colors.remove(colors.size() - 1));
		return this;
	}

	public G2D pushFont()
	{
		fonts.add(g2d.getFont());
		return this;
	}

	public Font getFont()
	{
		return g2d.getFont();
	}

	public G2D setFont(Font font)
	{
		g2d.setFont(font);
		return this;
	}

	public G2D setFontSize(float size)
	{
		setFont(getFont().deriveFont(size));
		return this;
	}

	public G2D popFont()
	{
		g2d.setFont(fonts.remove(fonts.size() - 1));
		return this;
	}

	public G2D clearRect(double x, double y, double width, double height)
	{
		g2d.clearRect((int) x, (int) y, (int) width, (int) height);
		return this;
	}

	public G2D clearRect(RectangularShape rect)
	{
		clearRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
		return this;
	}

	public G2D drawImage(ImageIcon image, double x, double y)
	{
		x = (int) getCenteredX(x, image.getIconWidth());
		y = (int) getCenteredY(y, image.getIconHeight());
		g2d.drawImage(image.getImage(), (int) x, (int) y, null);
		return this;
	}

	public G2D drawImage(Image image, double x, double y)
	{
		x = (int) getCenteredX(x, image.getWidth(null));
		y = (int) getCenteredY(y, image.getHeight(null));
		g2d.drawImage(image, (int) x, (int) y, null);
		return this;
	}

	public G2D drawImage(ImageIcon image)
	{
		return drawImage(image, 0, 0);
	}
	
	public G2D drawRect(double x, double y, double width, double height)
	{
		return drawRectangle(x, y, width, height);
	}

	public G2D drawRectangle(double x, double y, double width, double height)
	{
		rect.x = getCenteredX(x, width);
		rect.y = getCenteredY(y, height);
		rect.width = width;
		rect.height = height;
		return drawOrFillShape(rect);
	}

	public G2D drawRoundRectangle(double x, double y, double width, double height, double arcWidth, double arcHeight)
	{
		roundRect.x = getCenteredX(x, width);
		roundRect.y = getCenteredY(y, height);
		roundRect.width = width;
		roundRect.height = height;
		roundRect.arcwidth = arcWidth;
		roundRect.archeight = arcHeight;
		return drawOrFillShape(roundRect);
	}

	public G2D drawPoint(double x, double y)
	{
		return drawLine(x, y, x, y);
	}

	public G2D drawCircle(double x, double y, double radius)
	{
		return drawEllipse(x, y, radius * 2, radius * 2);
	}

	public G2D drawEllipse(double x, double y, double width, double height)
	{
		circle.x = getCenteredX(x, width);
		circle.y = getCenteredY(y, height);
		circle.width = width;
		circle.height = height;
		return drawOrFillShape(circle);
	}

	public G2D drawLine(double x1, double y1, double x2, double y2)
	{
		line.x1 = x1;
		line.x2 = x2;
		line.y1 = y1;
		line.y2 = y2;
		g2d.draw(line);
		return this;
	}

	public G2D drawString(Object s, double x, double y)
	{
		String[] sp = String.valueOf(s).split("\n");
		for(String str : sp)
		{
			Rectangle2D r = g2d.getFontMetrics().getStringBounds(str, g2d);
			float x1 = (float) getCenteredX(x, r.getWidth());
			float y1 = (float) getCenteredY(y, -r.getHeight());
			g2d.drawString(str, x1, y1);
			y += r.getHeight();
		}
		return this;
	}

//	public G2D drawStrings(Object s, double x, double y)
//	{
//		String[] sp = String.valueOf(s).split("\n");
//		for(String str : sp)
//		{
//			Rectangle2D r = g2d.getFontMetrics().getStringBounds(str, g2d);
//			float x1 = (float) getCenteredX(x, r.getWidth());
//			float y1 = (float) getCenteredY(y, -r.getHeight());
//			g2d.drawString(str, x1, y1);
//		}
//		return this;
//	}

	public G2D drawShadowedString(Object s, double x, double y, Color shadowColor)
	{
		pushColor();
		setColor(shadowColor);
		drawString(s, x - 1, y);
		drawString(s, x + 1, y);
		drawString(s, x, y - 1);
		drawString(s, x, y + 1);
		drawString(s, x - 1, y - 1);
		drawString(s, x + 1, y - 1);
		drawString(s, x - 1, y + 1);
		drawString(s, x + 1, y + 1);
		popColor();
		drawString(s, x, y);
		return this;
	}

	public G2D drawPolygon(int[] xPoints, int[] yPoints)
	{
		g2d.drawPolygon(xPoints, yPoints, Math.min(xPoints.length, yPoints.length));
		return this;
	}

	public G2D drawPolyline(int[] xPoints, int[] yPoints)
	{
		g2d.drawPolyline(xPoints, yPoints, Math.min(xPoints.length, yPoints.length));
		return this;
	}

	public G2D drawShape(Shape shape)
	{
		if (isEnabled(G_CENTER) && shape instanceof RectangularShape)
		{
			RectangularShape rect = (RectangularShape) shape;
			shape = new Rectangle2D.Double(rect.getX() - rect.getWidth() / 2, rect.getY() - rect.getHeight() / 2, rect.getWidth(), rect.getHeight());
		}
		return drawOrFillShape(shape);
	}

	public G2D translate(double x, double y)
	{
		g2d.translate(x, y);
		return this;
	}

	public G2D scale(double x, double y)
	{
		g2d.scale(x, y);
		return this;
	}

	public G2D shear(double x, double y)
	{
		g2d.shear(x, y);
		return this;
	}

	public G2D rotate(double degrees, double x, double y)
	{
		g2d.rotate(Math.toRadians(degrees), x, y);
		return this;
	}

	public G2D rotate(double degrees)
	{
		g2d.rotate(Math.toRadians(degrees));
		return this;
	}

	public G2D rotateR(double radians, double x, double y)
	{
		g2d.rotate(radians, x, y);
		return this;
	}

	public G2D rotateR(double radians)
	{
		g2d.rotate(radians);
		return this;
	}

	public Rectangle2D getStringBounds(String s)
	{
		return g2d.getFontMetrics().getStringBounds(s, g2d);
	}
	
	public float getLineWidth()
	{
		return ((BasicStroke) g2d.getStroke()).getLineWidth();
	}
	
	public G2D setLineWidth(float lw)
	{
		g2d.setStroke(new BasicStroke(lw));
		return this;
	}

	private G2D drawOrFillShape(Shape shape)
	{
		if (isEnabled(G_FILL))
		{
			g2d.fill(shape);
		}
		else
		{
			g2d.draw(shape);
		}
		return this;
	}

	private double getCenteredX(double x, double width)
	{
		return isEnabled(G_CENTER) ? x - width / 2 : x;
	}

	private double getCenteredY(double y, double height)
	{
		return isEnabled(G_CENTER) ? y - height / 2 : y;
	}

	public Graphics2D getGraphics()
	{
		return g2d;
	}

	private final Rectangle2D.Double rect = new Rectangle2D.Double();
	private final Ellipse2D.Double circle = new Ellipse2D.Double();
	private final Line2D.Double line = new Line2D.Double();
	private final RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double();

	public static final int G_FILL = 1 << 0;
	public static final int G_CENTER = 1 << 1;
}
