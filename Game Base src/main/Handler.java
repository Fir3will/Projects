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
package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.Map.Entry;
import java.util.Set;
import com.hk.collections.maps.IndexMap;

public class Handler implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
	int w, h;
	private final Point2D.Float mouse;
	private final Game game;
	private final IndexMap<KeyPress> keyMap;
	private final IndexMap<Boolean> mouseMap;

	public Handler(Game game)
	{
		this.game = game;
		mouse = new Point2D.Float();
		keyMap = new IndexMap<KeyPress>();
		mouseMap = new IndexMap<Boolean>();
	}

	public void update()
	{
		Set<Entry<Integer, KeyPress>> ents = keyMap.entrySet();
		for (Entry<Integer, KeyPress> val : ents)
		{
			KeyPress kp = val.getValue();
			if (kp.pressed)
			{
				game.key(val.getKey(), kp.keyChar, true);
			}
		}
		Set<Entry<Integer, Boolean>> mEnts = mouseMap.entrySet();
		for (Entry<Integer, Boolean> val : mEnts)
		{
			if (val.getValue())
			{
				game.mouse(mouse.x, mouse.y, val.getValue(), val.getKey());
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		keyMap.put(e.getKeyCode(), new KeyPress(true, e.getKeyChar()));
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		game.key(e.getKeyCode(), e.getKeyChar(), false);
		keyMap.put(e.getKeyCode(), new KeyPress(false, e.getKeyChar()));
	}

	@Override
	public void keyTyped(KeyEvent e)
	{}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		project(e.getX(), e.getY());
		game.mouseWheel(e.getWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		project(e.getX(), e.getY());
		game.mouseMoved(mouse.x, mouse.y);
		//		mouseMap.put(e.getButton(), true);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		project(e.getX(), e.getY());
		game.mouseMoved(mouse.x, mouse.y);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		project(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		project(e.getX(), e.getY());
		game.mouseMoved(mouse.x, mouse.y);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{}

	@Override
	public void mousePressed(MouseEvent e)
	{
		project(e.getX(), e.getY());
		mouseMap.put(e.getButton(), true);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		project(e.getX(), e.getY());
		mouseMap.put(e.getButton(), false);
		game.mouse(mouse.x, mouse.y, false, e.getButton());
	}

	private void project(int x, int y)
	{
		mouse.x = x / (float) w * Main.WIDTH;
		mouse.y = y / (float) h * Main.HEIGHT;
	}

	public float mouseX()
	{
		return mouse.x;
	}

	public float mouseY()
	{
		return mouse.y;
	}

	public boolean isKeyDown(int keyCode)
	{
		return keyMap.containsKey(keyCode) && keyMap.get(keyCode).pressed;
	}

	public boolean isButton(int button)
	{
		return mouseMap.containsKey(button) && mouseMap.get(button);
	}

	private class KeyPress
	{
		public final boolean pressed;
		public final char keyChar;

		public KeyPress(boolean pressed, char keyChar)
		{
			this.pressed = pressed;
			this.keyChar = keyChar;
		}
	}
}
