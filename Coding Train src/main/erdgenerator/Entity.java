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
package main.erdgenerator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.hk.g2d.G2D;

public class Entity
{
	public final String name;
	public final float gx, gy, gw = 100, gh = 50;
	public final List<Attribute> attrs;
	
	public Entity(String name, float gx, float gy)
	{
		this.name = name;
		this.gx = gx;
		this.gy = gy;
		attrs = new ArrayList<>();
	}
	
	public void paintEntity(G2D g2d)
	{
		g2d.enable(G2D.G_CENTER | G2D.G_FILL);
		g2d.setColor(Color.WHITE);
		g2d.drawRect(gx, gy, gw, gh);
		g2d.disable(G2D.G_FILL);
		g2d.setColor(Color.BLACK);
		g2d.drawString(name, gx, gy);
		g2d.drawRect(gx, gy, gw, gh);
		g2d.disable(G2D.G_CENTER);
		
		float x = gx - gw / 2 + 5;
		g2d.pushFont();
		g2d.setFontSize(12F);
		for(int i = 0; i < attrs.size(); i++)
		{
			Attribute attr = attrs.get(i);
			String name = attr.name;
			float w = (float) (g2d.getStringBounds(name).getWidth() + 25);
			float y = gy - gh - (attrs.size() - i) * 18;
			x += w / 2 + i * 2;

			g2d.setColor(Color.BLACK);
			g2d.drawLine(gx - gw / 2, gy - gh / 2, x, y);

			g2d.enable(G2D.G_CENTER | G2D.G_FILL);
			g2d.setColor(Color.WHITE);
			g2d.drawEllipse(x, y, w, 20);
			g2d.disable(G2D.G_FILL);

			g2d.setColor(Color.BLACK);
			g2d.drawString(name, x, y - 2);
			
			if(attr.identifier)
				g2d.drawLine(x - (w - 20) / 2, y + 7, x + (w - 20) / 2, y + 7);
			if(attr.mutivalue)
				g2d.drawEllipse(x, y, w + 5, 25);
			
			g2d.drawEllipse(x, y, w, 20);
			g2d.disable(G2D.G_CENTER);
			
//			x += i > 3 ? -w : w;
		}
		g2d.popFont();
	}

	public Attribute addAttribute(String attribute)
	{
		Attribute attr = new Attribute(this, attribute);
		attrs.add(attr);
		return attr;
	}
	
	public boolean contains(float x, float y)
	{
		return x >= gx - gw / 2 && y >= gy - gh / 2 && x <= gx + gw / 2 && y <= gy + gh / 2;
	}
}
