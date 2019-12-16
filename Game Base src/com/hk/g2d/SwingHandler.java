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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.hk.math.vector.Vector2F;

class SwingHandler implements Handler, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
	public int frameWidth, frameHeight;
	Game game;
	private int button;
	private final Vector2F mouse;
	private boolean pressed;
	private final Map<Integer, Character> keyMap;
	
	SwingHandler()
	{
		keyMap = new HashMap<>();
		mouse = new Vector2F();
	}
	
	public void update()
	{
		button = -1;
		Set<Entry<Integer, Character>> ents = keyMap.entrySet();
		for (Entry<Integer, Character> val : ents)
		{
			game.key(val.getKey(), val.getValue(), true);
		}
		
		if(pressed)
		{
			game.mouse(mouse.x, mouse.y, pressed);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		game.key(e.getKeyCode(), e.getKeyChar(), true);
		keyMap.put(e.getKeyCode(), e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		game.key(e.getKeyCode(), e.getKeyChar(), false);
		keyMap.remove(e.getKeyCode());
	}

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
	public void mousePressed(MouseEvent e)
	{
		project(e.getX(), e.getY());
		button = e.getButton();
		game.mouse(mouse.x, mouse.y, pressed = true);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		project(e.getX(), e.getY());
		button = e.getButton();
		game.mouse(mouse.x, mouse.y, pressed = false);
		button = -1;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		project(e.getX(), e.getY());
	}

	@Override
	public void keyTyped(KeyEvent e)
	{}

	@Override
	public void mouseExited(MouseEvent e)
	{}

	private void project(int x, int y)
	{
		mouse.x = x * 1F / frameWidth * game.width;
		mouse.y = y * 1F / frameHeight * game.height;
	}
	
	public Vector2F get()
	{
		return mouse.clone();
	}
	
	public float getX()
	{
		return mouse.x;
	}
	
	public float getY()
	{
		return mouse.y;
	}
	
	public int getButton()
	{
		return button;
	}

	public boolean isKeyDown(int keyCode)
	{
		return keyMap.containsKey(keyCode);
	}
	
	public boolean isPressed()
	{
		return pressed;
	}
}
