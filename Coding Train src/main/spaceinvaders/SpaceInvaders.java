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
package main.spaceinvaders;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;
import com.hk.str.StringUtil;

import main.spaceinvaders.Enemy.EnemyType;

public class SpaceInvaders extends GuiScreen
{
	public int score = 0;
	private final List<Entity> entities = new ArrayList<>(), toAdd = new ArrayList<>(), toRemove = new ArrayList<>();
	public int amtOfEnemies = 0, enemyBullets;
	public long last;
	public final Player thePlayer;
	public final BufferedImage spritesheet;
	private long start = -1, lostTime = -1, enemyTick, sendTime;
	private int lvl = 0, nextFire = -1, lives = 4;
	
	public SpaceInvaders(Game game, BufferedImage spritesheet)
	{
		super(game);
		this.spritesheet = spritesheet;
		thePlayer = new Player(this);
		reset(false);
	}
	
	public void reset(boolean soft)
	{
		enemyBullets = 0;
		lostTime = -1;
		toAdd.clear();
		toRemove.clear();
		if(!soft)
		{
			entities.clear();
			for(int i = 0; i < EnemyType.values().length - 1; i++)
			{
				EnemyType type = EnemyType.values()[i];
				for(int x = 0; x < 11; x++)
				{
					Enemy e = new Enemy(this, type);
					e.pos.set(70 + x * (660 / 11) - e.width / 2F, 60 + 50 * i);
					entities.add(e);
				}
			}
			amtOfEnemies = entities.size();
			nextFire = -1;
			
			int blocks = 5;
			for(int i = 0; i < blocks; i++)
			{
				float x = game.width * ((i + 1) / (blocks + 1F));

				Block a = new Block(this, false);
				Block b = new Block(this, true);
				
				a.pos.set(x - 22, game.height - 135);
				b.pos.set(x, game.height - 135);
				
				entities.add(a);
				entities.add(b);
			}
			sendTime = System.currentTimeMillis() + Rand.nextInt(3000, 20000);
		}
		thePlayer.reset();
		
		thePlayer.pos.set(game.width / 2 - thePlayer.width / 2F, game.height - 65);
		entities.add(thePlayer);
		start = last = System.currentTimeMillis();
	}
	
	public void update(double delta)
	{
		if(amtOfEnemies > 0 && (nextFire == -1 || nextFire >= amtOfEnemies))
		{
			nextFire = Rand.nextInt(0, amtOfEnemies);
		}
		long p = (long) (amtOfEnemies / 55F * 800) + 200;
//		super.updateScreen(ticks);
		long time = System.currentTimeMillis();
		long elapsed = time - last;
		float step = elapsed / 1000F;
		if(time - enemyTick >= p && !thePlayer.isDead())
		{
			for(Entity entity : entities)
			{
				if(entity instanceof Enemy && ((Enemy) entity).type != EnemyType.BONUS)
				{
					entity.update(step);
				}
			}
			enemyTick = time;
		}
		int enemyCount = 0;
		for(Entity entity : entities)
		{
			if(entity.isDead())
			{
				if(entity.onDeath())
				{
					removeEntity(entity);
				}
			}
			else if(entity instanceof Enemy && ((Enemy) entity).type != EnemyType.BONUS)
			{
				if(enemyCount == nextFire && !thePlayer.isDead() && enemyBullets < 1)
				{
					nextFire = -1;
					((Enemy) entity).fire(1 + (lvl * 0.25F));
				}
				enemyCount++;
			}
			else
			{
				entity.update(step);
			}
		}
		last = time;
		
		int size = entities.size();
		for(int i = 0; i < size; i++)
		{
			for(int j = i + 1; j < size; j++)
			{
				Entity a = entities.get(i);
				Entity b = entities.get(j);
				
				if(!a.isDead() && !b.isDead() && a.intersects(b))
				{
					a.onCollide(b);
					b.onCollide(a);
				}
			}
		}
		
		if(sendTime != -1 && System.currentTimeMillis() - sendTime > 0)
		{
			sendTime = -1;
			Enemy e = new Enemy(this, EnemyType.BONUS);
			e.pos.set(0, 30);
			e.vel.x = 70;
			entities.add(e);
		}

		entities.addAll(toAdd);
		entities.removeAll(toRemove);
		toAdd.clear();
		toRemove.clear();

		if(!thePlayer.isDead())
		{
			boolean check = System.currentTimeMillis() - start > 100;
			if(check && handler.isKeyDown(KeyEvent.VK_A))
			{
				thePlayer.pos.x -= step * 200;
			}
			if(check && handler.isKeyDown(KeyEvent.VK_D))
			{
				thePlayer.pos.x += step * 200;
			}
			if(check && handler.isKeyDown(KeyEvent.VK_SPACE))
			{
				thePlayer.fire();
			}
			
			if(amtOfEnemies == 0)
			{
				if(lostTime == -1)
				{
					lostTime = System.currentTimeMillis();
				}
			}
		}
		else if(lostTime == -1)
		{
			lostTime = System.currentTimeMillis();
		}
		
		if(thePlayer.pos.x < 20)
		{
			thePlayer.pos.x = 20;
		}
		else if(thePlayer.pos.x > game.width - 20 - thePlayer.width)
		{
			thePlayer.pos.x = game.width - 20 - thePlayer.width;
		}
	}

	public void paint(G2D g2d)
	{
		for(Entity entity : entities)
		{
			entity.paint(g2d);
		}

		g2d.setColor(Color.WHITE);
		g2d.drawString("Score: ", 5, 15);
		g2d.drawString("Lives", g2d.width - 175, 25);
		Vector2F pos = thePlayer.pos.clone();
		g2d.pushMatrix();
		thePlayer.pos.zero();
		g2d.translate(g2d.width - 190, 5);
		for(int i = 0; i < lives; i++)
		{
			g2d.translate(50, 0);
			thePlayer.paint(g2d);
		}
		g2d.popMatrix();
		thePlayer.pos.set(pos);
		g2d.setColor(Color.GREEN);
		g2d.drawString(StringUtil.commaFormat(score), 5 + g2d.getStringBounds("Score: ").getWidth(), 15);
	
		if(lostTime != -1)
		{
			String msg = null;
			if(thePlayer.isDead())
			{
				thePlayer.paint(g2d);
				
				g2d.setColor(Color.RED);
				msg = "You Lost!";
			}
			else
			{
				g2d.setColor(Color.GREEN);
				msg = "You Won!";
			}
			g2d.enable(G2D.G_CENTER);
			g2d.setFontSize(g2d.getFont().getSize2D() * 2F);
			g2d.drawString(msg, g2d.width / 2F, g2d.height / 2F);
			if(System.currentTimeMillis() - lostTime > 1500)
			{
				g2d.setFontSize(g2d.getFont().getSize2D() / 2F);
				g2d.drawString("(Press any key to " + (thePlayer.isDead() ? "retry" : "continue") + ")", g2d.width / 2F, g2d.height * 6 / 11F);
			}
			g2d.disable(G2D.G_CENTER);
		}
	}
	
	public void key(int key, char keyChar, boolean pressed)
	{
		super.key(key, keyChar, pressed);
		
		if(lostTime != -1 && System.currentTimeMillis() - lostTime > 1500)
		{
			boolean soft = false;
			int scr = 0;
			if(thePlayer.isDead())
			{
				lives--;
				if(lives == -1)
				{
					lvl = 0;
					lives = 3;
					scr = 0;
				}
				else
				{
					soft = true;
					scr = score;
				}
			}
			else
			{
				scr = score;
				lvl++;
			}
			reset(soft);
			score = scr;
		}
	}
	
	public void addEntity(Entity e)
	{
		toAdd.add(e);
	}
	
	public void removeEntity(Entity e)
	{
		toRemove.add(e);
	}
	
	public static void main(String[] args)
	{
		BufferedImage img = null;
		try
		{
			img = ImageIO.read(SpaceInvaders.class.getResource("sheet.png"));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
		Settings settings = new Settings();
		settings.title = "Space Invaders";
		settings.quality = Quality.POOR;
		settings.width = 800;
		settings.height = 600;
		settings.showFPS = true;
		settings.background = Color.BLACK;
		settings.maxFPS = 244;

		System.setProperty("Main.WIDTH", String.valueOf(settings.width));
		System.setProperty("Main.HEIGHT", String.valueOf(settings.height));

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new SpaceInvaders(frame.game, img));
		frame.launch();
	}
}
