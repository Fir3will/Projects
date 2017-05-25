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
import main.G2D;
import main.gui.GuiScreen;
import main.gui.GuiWidget;
import main.gui.event.ActionEvent;

public class GuiButton extends GuiWidget
{
	public final int buttonID;
	private String text;
	private Color color, textColor, hoverColor;
	private Font font;

	public GuiButton(GuiScreen screen, int buttonID, String text, float x, float y)
	{
		this(screen, buttonID, text, x, y, 250, 75);
	}

	public GuiButton(GuiScreen screen, int buttonID, String text, float x, float y, float width, float height)
	{
		super(screen);
		this.buttonID = buttonID;
		setBounds(x, y, width, height);
		setText(text);
		setColor(Color.BLACK);
	}

	public GuiButton setText(String text)
	{
		this.text = text;
		return this;
	}

	public GuiButton setColor(Color color)
	{
		this.color = hoverColor = color;
		float avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3F;
		if (avg < 127.5F)
		{
			hoverColor = new Color(color.getRed() + 100, color.getBlue() + 100, color.getGreen() + 100);
		}
		else if (avg > 127.5F)
		{
			hoverColor = new Color(color.getRed() - 100, color.getBlue() - 100, color.getGreen() - 100);
		}
		return this;
	}

	public GuiButton setHoverColor(Color hoverColor)
	{
		this.hoverColor = hoverColor;
		return this;
	}

	public GuiButton setTextColor(Color textColor)
	{
		this.textColor = textColor;
		return this;
	}

	public GuiButton setFont(Font font)
	{
		this.font = font;
		return this;
	}

	public String getText()
	{
		return text;
	}

	public Color getColor()
	{
		return color;
	}

	public Color getHoverColor()
	{
		return hoverColor;
	}

	public Color getTextColor()
	{
		return textColor;
	}

	public Font getFont()
	{
		return font;
	}

	@Override
	public void paintWidget(G2D g2d, float mouseX, float mouseY)
	{
		g2d.pushColor();
		g2d.setColor(bounds.contains(mouseX, mouseY) ? hoverColor : color);
		g2d.drawRoundRectangle(bounds.x, bounds.y, bounds.width, bounds.height, bounds.width / 5F, bounds.width / 5F);
		g2d.enable(G2D.G_CENTER);
		if (font != null)
		{
			g2d.pushFont();
			g2d.setFont(font);
		}
		g2d.setColor(textColor);
		g2d.drawString(text, bounds.getCenterX(), bounds.getCenterY());
		if (font != null)
		{
			g2d.popFont();
		}
		g2d.disable(G2D.G_CENTER);
		g2d.popColor();
	}

	@Override
	public void onClick(float x, float y, int button)
	{
		listeners.fireActionPerformed(new ButtonEvent(this, x, y, button));
	}

	public class ButtonEvent extends ActionEvent
	{
		public final GuiButton button;
		public final float clickX, clickY;
		public final int mouseButton;

		public ButtonEvent(GuiButton button, float clickX, float clickY, int mouseButton)
		{
			this.button = button;
			this.clickX = clickX;
			this.clickY = clickY;
			this.mouseButton = mouseButton;
		}
	}
}
