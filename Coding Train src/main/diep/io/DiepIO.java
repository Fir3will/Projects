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
package main.diep.io;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

import main.diep.io.VehicleFood.VehicleFoodType;

public class DiepIO extends GuiScreen
{
	public final Vehicle player;
	private final int gw, gh, scl;
	private final Vector2F cam;
	private final List<Shot> shots;
	private final List<Vehicle> vehicles;
	private final AffineTransform aft;
	private double zoom = 0.8;
	private float mx, my;

	public DiepIO(Game game)
	{
		super(game);
		
		gw = gh = 16;
		scl = 128;
		
		cam = new Vector2F();
		shots = new LinkedList<>();
		vehicles = new LinkedList<>();

		player = new Vehicle(this, (gw / 2D + 0.5) * scl, (gh / 2D + 0.5) * scl, TankType.TWIN);
		cam.x = (float) player.px;
		cam.y = (float) player.py;
		vehicles.add(player);
		
		VehicleFood ai;
		double x, y;
		int amt = Rand.nextInt(30, 50);
		while(amt > 0)
		{
			x = Rand.nextDouble(gw * scl);
			y = Rand.nextDouble(gh * scl);
			ai = new VehicleFood(this, x, y, VehicleFoodType.getOnChance());
			vehicles.add(ai);
			amt--;
		}

		aft = new AffineTransform();
	}

	@Override
	public void update(double delta)
	{
		for(Vehicle vehicle : vehicles)
		{
			vehicle.updateVehicle(delta);
		}
		
		Iterator<Shot> itr = shots.iterator();
		while(itr.hasNext())
		{
			if(itr.next().updateShot(delta))
				itr.remove();
		}

		double d = 1 - (delta * 9.9);
		cam.x = (float) (cam.x * d + player.px * (1 - d));
		cam.y = (float) (cam.y * d + player.py * (1 - d));
	}

	@Override
	public void paint(G2D g2d)
	{
		g2d.pushMatrix();
		AffineTransform aft2 = g2d.getMatrix();
		double ax = (game.width - (game.width * zoom)) / 2D;
		double ay = (game.height - (game.height * zoom)) / 2D;
		aft.setToIdentity();
		aft.translate(ax, ay);
		aft.scale(zoom, zoom);
		aft.translate(game.width / 2 - cam.x, game.height / 2 - cam.y);
		try
		{
			Point2D.Float a = new Point2D.Float(mx, game.height - my - 1);
			aft.inverseTransform(a, a);
			player.lookingAt.set(a.x, a.y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		aft2.concatenate(aft);
		g2d.setMatrix(aft2);
		
		g2d.setColor(Color.GRAY);
		for(int i = 0; i <= gh; i++)
		{
			g2d.drawLine(0, i * scl, gw * scl, i * scl);
		}
		for(int i = 0; i <= gw; i++)
		{
			g2d.drawLine(i * scl, 0, i * scl, gh * scl);
		}

		g2d.setLineWidth(4);
		for(Vehicle vehicle : vehicles)
		{
			if(vehicle == player)
				continue;
			vehicle.paintVehicle(g2d);
		}
		player.paintVehicle(g2d);
		
		g2d.setLineWidth(6);
		for(Shot shot : shots)
			shot.paintShot(g2d);
		g2d.setLineWidth(1);

		g2d.popMatrix();
	}
	
	public void shoot(Shot shot)
	{
		shots.add(shot);
	}

	public void mouse(float x, float y, boolean pressed)
	{
		player.firing = pressed;
	}
	
	public void mouseMoved(float x, float y)
	{
		this.mx = x;
		this.my = y;
	}

	public void key(int key, char keyChar, boolean pressed)
	{
		if(key == KeyEvent.VK_W)
		{
			player.move = pressed ? player.move | 8 : player.move & ~8;
		}
		else if(key == KeyEvent.VK_A)
		{
			player.move = pressed ? player.move | 4 : player.move & ~4;
		}
		else if(key == KeyEvent.VK_S)
		{
			player.move = pressed ? player.move | 2 : player.move & ~2;
		}
		else if(key == KeyEvent.VK_D)
		{
			player.move = pressed ? player.move | 1 : player.move & ~1;
		}
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "https://diep.io";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = settings.width * 9 / 16;
		settings.showFPS = true;
		settings.background = Color.LIGHT_GRAY;
		settings.maxFPS = -1;
//		settings.maxFPS = 60;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new DiepIO(frame.game));
		frame.launch();
	}
}
