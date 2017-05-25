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
package main.gui.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import com.hk.math.MathUtil;
import main.G2D;
import main.gui.GuiScreen;
import main.gui.GuiWidget;

public class GuiTextField extends GuiWidget
{
	private final StringBuilder text;
	private int caretPosition, horizontalAlignment;
	private Color color, textColor;
	private Font font;

	public GuiTextField(GuiScreen screen, float x, float y)
	{
		this(screen, x, y, 300, 50);
	}

	public GuiTextField(GuiScreen screen, float x, float y, float width, float height)
	{
		super(screen);
		setBounds(x, y, width, height);
		setColor(Color.BLACK);
		text = new StringBuilder();
	}

	public GuiTextField setText(String text)
	{
		this.text.delete(0, text.length());
		this.text.append(text);
		return this;
	}

	public GuiTextField setColor(Color color)
	{
		this.color = color;
		return this;
	}

	public GuiTextField setTextColor(Color textColor)
	{
		this.textColor = textColor;
		return this;
	}

	public GuiTextField setFont(Font font)
	{
		this.font = font;
		return this;
	}

	public GuiTextField setHorizontalAlignment(int horizontalAlignment)
	{
		this.horizontalAlignment = MathUtil.clamp(horizontalAlignment, 2, 0);
		return this;
	}

	public String getText()
	{
		return text.toString();
	}

	public Color getColor()
	{
		return color;
	}

	public Color getTextColor()
	{
		return textColor;
	}

	public Font getFont()
	{
		return font;
	}

	public int getHorizontalAlignment()
	{
		return horizontalAlignment;
	}

	@Override
	public void paintWidget(G2D g2d, float mouseX, float mouseY)
	{
		g2d.pushColor();
		g2d.setColor(color);
		g2d.drawRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		g2d.setFont(font);
		Rectangle2D b = g2d.getStringBounds(text.toString());
		if (horizontalAlignment == HORIZ_LEFT)
		{
			g2d.drawString(caretPosition, 5, 15);
			g2d.drawString(text, bounds.x + 2, bounds.getCenterY() - b.getCenterY());
		}
		else if (horizontalAlignment == HORIZ_CENTER)
		{
			g2d.drawString(text, bounds.getCenterX() - b.getCenterX(), bounds.getCenterY() - b.getCenterY());
		}
		else if (horizontalAlignment == HORIZ_RIGHT)
		{
			g2d.drawString(text, bounds.x + bounds.width - 2 - b.getWidth(), bounds.getCenterY() - b.getCenterY());
		}
		if (isCurrentFocus() && System.currentTimeMillis() / 500 % 2 == 0)
		{
			b = g2d.getStringBounds(text.substring(0, caretPosition));
			g2d.drawRectangle(bounds.x + b.getMaxX() + 2, bounds.y + b.getHeight(), 1, b.getHeight());
		}
		g2d.popColor();
	}

	@Override
	public boolean needsFocus()
	{
		return true;
	}

	@Override
	public void onKeyRelease(int keyCode, char keyChar)
	{
		if (keyCode == KeyEvent.VK_LEFT)
		{
			caretPosition = Math.max(0, caretPosition - 1);
		}
		else if (keyCode == KeyEvent.VK_RIGHT)
		{
			caretPosition = Math.min(text.length(), caretPosition + 1);
		}
		else if (keyCode == KeyEvent.VK_BACK_SPACE)
		{
			if (text.length() > 0 && caretPosition > 0)
			{
				text.deleteCharAt(caretPosition - 1);
				caretPosition--;
			}
		}
		else if (Character.isAlphabetic(keyChar))
		{
			text.insert(caretPosition, keyChar);
			caretPosition++;
		}
		System.out.println("'" + keyChar + "' " + (int) keyChar);
	}

	public static final int HORIZ_LEFT = 0, HORIZ_CENTER = 1, HORIZ_RIGHT = 2;
}
