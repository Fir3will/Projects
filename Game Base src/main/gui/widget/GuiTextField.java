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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hk.math.MathUtil;

import main.G2D;
import main.gui.GuiScreen;
import main.gui.GuiWidget;
import main.gui.event.ActionPerformedEvent;

public class GuiTextField extends GuiWidget
{
	private final StringBuilder text, prevText;
	private int caretPosition, endCaretPos, horizontalAlignment;
	private Color color, textColor, highlightColor;
	private Font font;
	private final List<Rectangle2D.Double> letterBounds;
	//	private float strWidth;

	public GuiTextField(GuiScreen screen, float x, float y)
	{
		this(screen, x, y, 300, 50);
	}

	public GuiTextField(GuiScreen screen, float x, float y, float width, float height)
	{
		super(screen);
		letterBounds = new ArrayList<>();
		setBounds(x, y, width, height);
		setColor(Color.BLACK);
		setTextColor(Color.BLACK);
		text = new StringBuilder();
		prevText = new StringBuilder();
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
		highlightColor = new Color(255 - textColor.getRed(), 255 - textColor.getGreen(), 255 - textColor.getBlue());
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
		float x = 0;
		float y = 0;
		if (horizontalAlignment == HORIZ_LEFT)
		{
			x = bounds.x + 2;
			y = (float) (bounds.getCenterY() - b.getCenterY());
		}
		else if (horizontalAlignment == HORIZ_CENTER)
		{
			x = (float) (bounds.getCenterX() - b.getCenterX());
			y = (float) (bounds.getCenterY() - b.getCenterY());
		}
		else if (horizontalAlignment == HORIZ_RIGHT)
		{
			x = (float) (bounds.x + bounds.width - 2 - b.getWidth());
			y = (float) (bounds.getCenterY() - b.getCenterY());
		}
//		g2d.drawString(caretPosition + ", " + endCaretPos, 5, 15);
		if (endCaretPos == -1)
		{
			g2d.setColor(textColor);
			g2d.drawString(text, x, y);
		}
		else
		{
			int min = Math.min(caretPosition, endCaretPos);
			int max = Math.max(caretPosition, endCaretPos);
			String t1 = text.substring(0, min);
			String t2 = text.substring(min, max);
			String t3 = text.substring(max, text.length());
			Rectangle2D bt1 = g2d.getStringBounds(t1);
			Rectangle2D bt2 = g2d.getStringBounds(t2);
			g2d.setColor(textColor);
			g2d.drawString(t1, x, y);
			g2d.enable(G2D.G_FILL);
			g2d.drawRectangle(x + bt1.getWidth(), y - bt2.getHeight(), bt2.getWidth(), bt2.getHeight());
			g2d.disable(G2D.G_FILL);
			g2d.setColor(highlightColor);
			g2d.drawString(t2, x + bt1.getWidth(), y);
			g2d.setColor(textColor);
			g2d.drawString(t3, x + bt1.getWidth() + bt2.getWidth(), y);
		}
		if (isCurrentFocus() && System.currentTimeMillis() / 500 % 2 == 0)
		{
			b = g2d.getStringBounds(text.substring(0, caretPosition));
			g2d.drawRectangle(x + b.getMaxX() + 2, bounds.y + b.getHeight() / 2, 1, b.getHeight());
		}
		g2d.popColor();

		if (!text.equals(prevText))
		{
			prevText.delete(0, prevText.length());
			prevText.append(text);
			letterBounds.clear();
			double xOff = x;
			for (int i = 0; i < text.length(); i++)
			{
				Rectangle2D r = g2d.g2d.getFontMetrics().getStringBounds(text.toString(), i, i + 1, g2d.g2d);
				letterBounds.add(new Rectangle2D.Double(xOff + r.getX(), r.getY() + y, r.getWidth(), r.getHeight()));
				xOff += r.getWidth();
			}
			//			strWidth = (float) (xOff - x);
		}
	}

	@Override
	public boolean needsFocus()
	{
		return true;
	}

	@Override
	public void onKeyRelease(int keyCode, char keyChar)
	{
		if (screen.getHandler().isKeyDown(KeyEvent.VK_CONTROL) || screen.getHandler().isKeyDown(KeyEvent.VK_META))
		{
			Clipboard sc = Toolkit.getDefaultToolkit().getSystemClipboard();
			if (keyCode == KeyEvent.VK_C && endCaretPos != -1)
			{
				int min = Math.min(caretPosition, endCaretPos);
				int max = Math.max(caretPosition, endCaretPos);
				String s = text.substring(min, max);
				if (!s.isEmpty())
				{
					StringSelection st = new StringSelection(s);
					sc.setContents(st, st);
				}
			}
			else if (keyCode == KeyEvent.VK_V)
			{
				String contents = "";
				try
				{
					Transferable tf = sc.getContents(null);
					if (tf.isDataFlavorSupported(DataFlavor.stringFlavor))
					{
						contents = (String) tf.getTransferData(DataFlavor.stringFlavor);
					}
				}
				catch (UnsupportedFlavorException | IOException e)
				{
					e.printStackTrace();
				}
				if (endCaretPos != -1)
				{
					int min = Math.min(caretPosition, endCaretPos);
					int max = Math.max(caretPosition, endCaretPos);
					text.delete(min, max);
					endCaretPos = -1;
				}
				text.insert(caretPosition, contents);
				caretPosition += contents.length();
			}
			return;
		}
		if(keyCode == KeyEvent.VK_ENTER)
		{
			this.listeners.fireActionPerformed(new ActionPerformedEvent(this));
		}
		else if (keyCode == KeyEvent.VK_LEFT)
		{
			if (screen.getHandler().isKeyDown(KeyEvent.VK_SHIFT))
			{
				if (endCaretPos == -1)
				{
					endCaretPos = caretPosition;
					caretPosition = Math.max(0, caretPosition - 1);
				}
				else
				{
					caretPosition = Math.max(0, caretPosition - 1);
				}
			}
			else
			{
				endCaretPos = -1;
				caretPosition = Math.max(0, caretPosition - 1);
			}
		}
		else if (keyCode == KeyEvent.VK_RIGHT)
		{
			if (screen.getHandler().isKeyDown(KeyEvent.VK_SHIFT))
			{
				if (endCaretPos == -1)
				{
					endCaretPos = caretPosition;
					caretPosition = Math.min(text.length(), caretPosition + 1);
				}
				else
				{
					caretPosition = Math.min(text.length(), caretPosition + 1);
				}
			}
			else
			{
				endCaretPos = -1;
				caretPosition = Math.min(text.length(), caretPosition + 1);
			}
		}
		else if (keyCode == KeyEvent.VK_BACK_SPACE)
		{
			if (text.length() > 0 && caretPosition > 0)
			{
				int min = endCaretPos == -1 ? caretPosition - 1 : Math.min(caretPosition, endCaretPos);
				int max = endCaretPos == -1 ? caretPosition : Math.max(caretPosition, endCaretPos);
				text.delete(min, max);
				if(caretPosition > endCaretPos)
				{
					caretPosition -= max - min;
				}
				endCaretPos = -1;
			}
		}
		//		else if (Character.isAlphabetic(keyChar))
		//		{
		//			if (endCaretPos != -1)
		//			{
		//				int min = Math.min(caretPosition, endCaretPos);
		//				int max = Math.max(caretPosition, endCaretPos);
		//				text.delete(min, max);
		//				endCaretPos = -1;
		//			}
		//			text.insert(caretPosition, keyChar);
		//			caretPosition++;
		//		}
		//		else if (Character.isLetterOrDigit(keyChar))
		//		{
		//			if (endCaretPos != -1)
		//			{
		//				int min = Math.min(caretPosition, endCaretPos);
		//				int max = Math.max(caretPosition, endCaretPos);
		//				text.delete(min, max);
		//				endCaretPos = -1;
		//			}
		//			text.insert(caretPosition, keyChar);
		//			caretPosition++;
		//		}
		else if (Character.isDefined(keyChar))
		{
			if (endCaretPos != -1)
			{
				int min = Math.min(caretPosition, endCaretPos);
				int max = Math.max(caretPosition, endCaretPos);
				text.delete(min, max);
				endCaretPos = -1;
			}
			text.insert(caretPosition, keyChar);
			caretPosition++;
		}
	}

	@Override
	public void onClick(float x, float y, int button)
	{
		endCaretPos = -1;
		if (letterBounds.size() > 0)
		{
			if (letterBounds.get(0).getMinX() > x)
			{
				caretPosition = 0;
				return;
			}
			else if (letterBounds.get(letterBounds.size() - 1).getMaxX() < x)
			{
				caretPosition = text.length();
				return;
			}
		}
		for (int i = 0; i < letterBounds.size(); i++)
		{
			if (letterBounds.get(i).contains(x, y))
			{
				caretPosition = i;
				break;
			}
		}
	}

	public static final int HORIZ_LEFT = 0, HORIZ_CENTER = 1, HORIZ_RIGHT = 2;
}
