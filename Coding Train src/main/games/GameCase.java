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
package main.games;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.hk.file.FileUtil;
import com.hk.g2d.G2D;
import com.hk.math.ComparatorUtil;

import main.ell.EllException;
import main.ell.cmp.EllCompiler;
import main.ell.exe.DefaultLibrary;
import main.ell.exe.EllExecutor;

public class GameCase implements Comparable<GameCase>
{
	public final File file;
	public final String name;
	public final Object Colors, Keys;
	public final Object WIDTH = 500L, HEIGHT = 500L;
	
	public GameCase(File f) throws EllException
	{
		file = f;
		name = f.getName().substring(0, f.getName().length() - 4);
		
		Map<String, Object> clrs = new HashMap<>();
		clrs.put("RED", 0xFF0000);
		clrs.put("GREEN", 0x00FF00);
		clrs.put("BLUE", 0x0000FF);
		clrs.put("PURPLE", 0xFF00FF);
		clrs.put("YELLOW", 0xFFFF00);
		clrs.put("CYAN", 0x00FFFF);
		clrs.put("WHITE", 0xFFFFFF);
		clrs.put("BLACK", 0x000000);
		clrs.put("GRAY", 0x7F7F7F);
		Colors = clrs;

		Map<String, Object> kys = new HashMap<>();
		kys.put("KY_UP", KeyEvent.VK_UP);
		kys.put("KY_DOWN", KeyEvent.VK_DOWN);
		kys.put("KY_LEFT", KeyEvent.VK_LEFT);
		kys.put("KY_RIGHT", KeyEvent.VK_RIGHT);

		for(int i = 0x30; i <= 0x39; i++)
		{
			kys.put("KY_" + ((char) i), i);
		}
		for(int i = 0x41; i <= 0x5A; i++)
		{
			kys.put("KY_" + ((char) i), i);
		}
		
		Keys = kys;
	}
	
	public GameScreen createGame() throws Exception
	{
		String contents = FileUtil.getFileContents(file);
		byte[] code = new EllCompiler(contents).compile(System.out);
		System.out.println("From " + contents.length() + " bytes to " + code.length + " bytes; " + (code.length * 100 / contents.length()) + "%");
		EllExecutor exe = new EllExecutor(code);
		exe.addLibrary(new DefaultLibrary(System.out));
		exe.addLibrary(this);
		exe.execute(System.out);
		exe.callMethod("setup", null);
		
		return new GameScreen(exe);
	}

	@Override
	public int compareTo(GameCase o)
	{
		return ComparatorUtil.compString.compare(name, o.name);
	}
	
	public static GameCase attach(File f) throws Exception
	{
		return new GameCase(f);
	}
	
	public class GameScreen
	{
		public final EllExecutor exe;
		private G2D g2d;
		public final Object G_FILL = (long) G2D.G_FILL;
		public final Object G_CENTER = (long) G2D.G_CENTER;
		
		public GameScreen(EllExecutor exe)
		{
			this.exe = exe;
		}
		
		public void update(double delta)
		{
			try
			{
				exe.callMethod("update", new Object[] { delta });
			}
			catch (EllException e)
			{
				throw new RuntimeException(e);
			}
		}

		public void paint(G2D g2d)
		{
			try
			{
				exe.addLibrary(this);
				this.g2d = g2d;
				
				exe.callMethod("paint", null);
				
				this.g2d = null;
				exe.removeLibrary(this);				
			}
			catch (EllException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		public void mouse(float x, float y, boolean pressed, int button)
		{
			if(exe.hasMethod("mouse"))
			{
				try
				{
					exe.callMethod("mouse", new Object[] { x, y, pressed, button });
				}
				catch (EllException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		
		public void key(int keyCode, char keyChar, boolean pressed)
		{
			if(exe.hasMethod("key"))
			{
				try
				{
					exe.callMethod("key", new Object[] { keyCode, String.valueOf(keyChar), pressed });
				}
				catch (EllException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		
		public Object drawRect(Object[] args)
		{
			if(args.length != 4)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number && args[2] instanceof Number && args[3] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			double w = ((Number) args[2]).doubleValue();
			double h = ((Number) args[3]).doubleValue();
			g2d.drawRect(x, y, w, h);
			
			return null;
		}
		
		public Object drawRoundRect(Object[] args)
		{
			if(args.length != 6)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number &&
					args[2] instanceof Number && args[3] instanceof Number &&
					args[4] instanceof Number && args[5] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			double w = ((Number) args[2]).doubleValue();
			double h = ((Number) args[3]).doubleValue();
			double wa = ((Number) args[4]).doubleValue();
			double ha = ((Number) args[5]).doubleValue();
			g2d.drawRoundRectangle(x, y, w, h, wa, ha);
			
			return null;
		}
		
		public Object drawCircle(Object[] args)
		{
			if(args.length != 3)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number && args[2] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			double r = ((Number) args[2]).doubleValue();
			g2d.drawCircle(x, y, r);
			
			return null;
		}
		
		public Object drawEllipse(Object[] args)
		{
			if(args.length != 4)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number && args[2] instanceof Number && args[3] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			double w = ((Number) args[2]).doubleValue();
			double h = ((Number) args[3]).doubleValue();
			g2d.drawEllipse(x, y, w, h);
			
			return null;
		}
		
		public Object drawLine(Object[] args)
		{
			if(args.length != 4)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number && args[2] instanceof Number && args[3] instanceof Number))
				throw new RuntimeException();
			double x1 = ((Number) args[0]).doubleValue();
			double y1 = ((Number) args[1]).doubleValue();
			double x2 = ((Number) args[2]).doubleValue();
			double y2 = ((Number) args[3]).doubleValue();
			g2d.drawEllipse(x1, y1, x2, y2);
			
			return null;
		}
		
		public Object drawPoint(Object[] args)
		{
			if(args.length != 2)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			g2d.drawPoint(x, y);
			
			return null;
		}
		
		public Object drawString(Object[] args)
		{
			if(args.length != 3)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			String s = EllExecutor.toStr(args[2]);
			g2d.drawString(s, x, y);
			
			return null;
		}
		
		public Object clearRect(Object[] args)
		{
			if(args.length != 4)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number && args[2] instanceof Number && args[3] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			double w = ((Number) args[2]).doubleValue();
			double h = ((Number) args[3]).doubleValue();
			g2d.clearRect(x, y, w, h);
			
			return null;
		}
		
		public Object enable(Object[] args)
		{
			if(args.length != 1)
				throw new RuntimeException();
			if(!(args[0] instanceof Number))
				throw new RuntimeException();
			int v = ((Number) args[0]).intValue();
			g2d.enable(v);
			
			return null;
		}
		
		public Object isEnable(Object[] args)
		{
			if(args.length != 1)
				throw new RuntimeException();
			if(!(args[0] instanceof Number))
				throw new RuntimeException();
			int v = ((Number) args[0]).intValue();
			
			return g2d.isEnabled(v);
		}
		
		public Object disable(Object[] args)
		{
			if(args.length != 1)
				throw new RuntimeException();
			if(!(args[0] instanceof Number))
				throw new RuntimeException();
			int v = ((Number) args[0]).intValue();
			g2d.disable(v);
			
			return null;
		}
		
		public Object setColor(Object[] args)
		{
			if(args.length == 3)
			{
				if(!(args[0] instanceof Number && args[1] instanceof Number && args[2] instanceof Number))
					throw new RuntimeException();
				float r = ((Number) args[0]).floatValue();
				float g = ((Number) args[1]).floatValue();
				float b = ((Number) args[2]).floatValue();
				g2d.setColor(r, g, b);
			}
			else if(args.length == 1)
			{
				if(args[0] instanceof Number)
				{
					g2d.setColor(((Number) args[0]).intValue());
				}
				else if(args[0] instanceof Map)
				{
					@SuppressWarnings("unchecked")
					Map<String, Object> obj = ((Map<String, Object>) args[0]);
					float r = ((Number) obj.get("r")).floatValue();
					float g = ((Number) obj.get("g")).floatValue();
					float b = ((Number) obj.get("b")).floatValue();
					g2d.setColor(r, g, b);
				}
				else if(args[0] instanceof Object[])
				{
					Object[] arr = ((Object[]) args[0]);
					float r = ((Number) arr[0]).floatValue();
					float g = ((Number) arr[1]).floatValue();
					float b = ((Number) arr[2]).floatValue();
					g2d.setColor(r, g, b);
				}
				else throw new RuntimeException();
			}
			else throw new RuntimeException();
			
			return null;
		}
		
		public Object getColor(Object[] args)
		{
			if(args.length != 0)
				throw new RuntimeException();
			Color color = g2d.getColor();
			Map<String, Object> clr = new HashMap<>(3);
			clr.put("r", color.getRed() / 255D);
			clr.put("g", color.getGreen() / 255D);
			clr.put("b", color.getBlue() / 255D);
			return clr;
		}
		
		public Object pushMatrix(Object[] args)
		{
			if(args.length != 0)
				throw new RuntimeException();
			g2d.pushMatrix();
			return null;
		}
		
		public Object popMatrix(Object[] args)
		{
			if(args.length != 0)
				throw new RuntimeException();
			g2d.popMatrix();
			return null;
		}
		
		public Object getBounds(Object[] args)
		{
			if(args.length != 1)
				throw new RuntimeException();
			String s = EllExecutor.toStr(args[0]);
			Rectangle2D r2d = g2d.getStringBounds(s);
			Map<String, Object> clr = new HashMap<>(3);
			clr.put("x", r2d.getX());
			clr.put("y", r2d.getY());
			clr.put("width", r2d.getWidth());
			clr.put("height", r2d.getHeight());
			return clr;
		}
		
		public Object translate(Object[] args)
		{
			if(args.length != 2)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			g2d.translate(x, y);
			
			return null;
		}
		
		public Object scale(Object[] args)
		{
			if(args.length != 2)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			g2d.scale(x, y);
			
			return null;
		}
		
		public Object shear(Object[] args)
		{
			if(args.length != 2)
				throw new RuntimeException();
			if(!(args[0] instanceof Number && args[1] instanceof Number))
				throw new RuntimeException();
			double x = ((Number) args[0]).doubleValue();
			double y = ((Number) args[1]).doubleValue();
			g2d.shear(x, y);
			
			return null;
		}
		
		public Object rotate(Object[] args)
		{
			if(args.length != 1 || args.length != 3)
				throw new RuntimeException();
			if(!(args[0] instanceof Number))
				throw new RuntimeException();
			double deg = ((Number) args[0]).doubleValue();
			double x = args.length == 3 ? ((Number) args[1]).doubleValue() : 0;
			double y = args.length == 3 ? ((Number) args[2]).doubleValue() : 0;
			g2d.rotate(deg, x, y);
			
			return null;
		}
	}
}