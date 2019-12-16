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
package main.carrom;

import java.awt.Color;
import java.util.Arrays;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;
import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;
import com.hk.str.StringUtil;

public class Carrom extends GuiScreen
{
	private final Board board;
	private final BoardAI ai;
	private long last;
	private final float bx = 150;
	private final float by = 50;
//	private boolean fast = false;
	private boolean clicked, shot, madeIt;
	private int turn;
	private final int[] scores;
	private final Vector2F cpuSpot;
	
	public Carrom(Game game)
	{
		super(game);
		
		board = new Board();
		ai = new BoardAI(100, 20);
		cpuSpot = new Vector2F();
		scores = new int[4];
		reset();
	}
	
	private void reset()
	{
		board.setupBoard();
		Arrays.fill(scores, 0);
		turn = 0;
		
		last = System.currentTimeMillis();
	}
	
	@Override
	public void update(double delta)
	{
		long time = System.currentTimeMillis();
		long elapsed = time - last;
		last = time;
		double dt = elapsed / 1000D;
		
		if(!shot)
		{
			if(turn == 0)
			{
				float mx = game.handler.getX();
				float my = game.handler.getY();
				if(game.handler.isPressed())
				{
					clicked = true;
				}
				else if(clicked)
				{
					Vector2F d = new Vector2F(mx, my).subtractLocal(board.steggar.pos);
					board.steggar.pos.x -= bx;
					board.steggar.pos.y -= by;
					board.steggar.vel.set(d.multLocal(2));
					board.playShot();
					clicked = false;
					shot = true;
				}
				else
				{
					mx = MathUtil.between(bx + 90, mx, bx + bw - 90);
					my = MathUtil.between(by + bh - 90, my, by + bh - 60);
					board.steggar.pos.set(mx, my);
				}
			}
			else
			{
				if(ai.isRunning())
				{
					ai.updateAI(dt);
					
					if(!ai.isRunning())
					{
						ai.applyBest(board.steggar);
//						board.steggar.pos.subtractLocal(bx, by);
						board.playShot();
						shot = true;
					}
				}
				
				if(ai.getBest() != null && !shot)
				{
					ai.applyBest(board.steggar);
					cpuSpot.interpolateLocal(board.steggar.pos, (float) dt * 3);
					board.steggar.pos.set(cpuSpot);
					board.steggar.pos.addLocal(bx, by);
				}
			}
		}

		int a = board.updateBoard(dt);
		if(a > 0)
		{
			madeIt = true;
		}
		scores[turn] += a;
		
		if(board.canMoveOn() && shot)
		{
			shot = false;
			board.removeSteggar();
			if(!madeIt)
				turn = (turn + 1) % 2;
			if(turn == 1)
			{
				ai.setTo(board, 2);
			}
			madeIt = false;
		}
	}

	@Override
	public void paint(G2D g2d)
	{
		long time = System.currentTimeMillis() % 3000;
		Color cc = Color.getHSBColor(time / 3000F, 1F, 1F);	
		g2d.drawString("Your Points: " + StringUtil.commaFormat(scores[0]), bx, by + bh + 70);
		g2d.drawString("CPUs Points: " + StringUtil.commaFormat(scores[1]), bx, by + bh + 90);
		
		board.paintBoard(g2d, bx, by, cc);
		board.paintGotis(g2d, bx, by);
		
		if(!shot) board.steggar.paintGoti(g2d);
		
		if(turn == 1 && ai.getBest() != null && !shot)
		{
			Vector2F v = board.steggar.vel;
			cpuSpot.addLocal(bx, by);
			g2d.drawLine(cpuSpot.x, cpuSpot.y, cpuSpot.x + (v.x / 2), cpuSpot.y + (v.y / 2));
			cpuSpot.subtractLocal(bx, by);
		}
		
		if(clicked)
		{
			float mx = game.handler.getX();
			float my = game.handler.getY();
			Vector2F d = new Vector2F(mx, my).subtractLocal(board.steggar.pos);
			Vector2F v = board.steggar.pos;
			
			g2d.drawLine(v.x, v.y, v.x + d.x, v.y + d.y);
		}
	}
	
	public static void main(String[] args)
	{	
		Settings settings = new Settings();
		settings.title = "Carrom";
		settings.quality = Quality.GOOD;
		settings.width = 800;
		settings.height = 800;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new Carrom(frame.game));
		frame.launch();
	}
	
	public static final float bw = 500, bh = 500;
}
