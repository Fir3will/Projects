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

import java.util.Arrays;
import java.util.List;

import com.hk.math.FloatMath;
import com.hk.math.MathUtil;
import com.hk.math.Rand;
import com.hk.math.vector.Vector2F;

public class BoardAI
{
	public final Test[] tests;
	public final int gens;
	private Test best;
	public Board testBoard;
	private int stage, gen, player;
	private boolean running = false;
	
	public BoardAI(int size, int gens)
	{
		this.gens = gens;
		tests = new Test[size];
	}
	
	public void printFitness()
	{
		System.out.print("[");
		for(int i = 0; i < tests.length; i++)
		{
			System.out.print((int) tests[i].getFitness());
			System.out.print(",");
			System.out.print(tests[i].score);
			
			if(i < tests.length - 1)
			{
				System.out.print(", ");
			}
		}
		System.out.println("]");
	}
	
	public void setTo(Board b, int player)
	{
		float ang = FloatMath.toRadians(90 * (player % 4));

		testBoard = new Board(b);
		this.player = player;
		List<Goti> lst = testBoard.gotis;
		for(int j = 0; j < lst.size(); j++)
		{
			lst.get(j).pos.subtractLocal(Carrom.bw / 2, Carrom.bh / 2);
			lst.get(j).pos.rotateAround(ang, false);
			lst.get(j).pos.addLocal(Carrom.bw / 2, Carrom.bh / 2);
			lst.get(j).vel.rotateAround(ang, false);
		}
		
		running = true;
		stage = gen = 0;
		for(int i = 0; i < tests.length; i++)
		{
			tests[i] = new Test(testBoard);
		}
	}
	
	public void updateAI(double dt)
	{
		if(stage == 0)
		{
			handleShootSteggar();
		}
		else if(stage == 1)
		{
			handleBoardTicks(dt);
			boolean fg = figuredItOut();
			
			if(!fg && stage == 1)
			{
				stage--;
			}
			else if(fg)
			{
				Arrays.sort(tests);
				best = tests[0];
				if(gen < gens - 1)
				{
					nextGeneration();
					stage = -1;
				}
			}
		}
		else if(stage == 2)
		{
			running = false;
		}
		
		stage++;
	}
	
	private void handleShootSteggar()
	{
		for(int i = 0; i < tests.length; i++)
		{
			Test test = tests[i];
			Goti st = test.board.steggar;
			st.pos.set(test.pos);
			st.vel.set(test.vel);
			test.board.playShot();
		}
	}

	private void handleBoardTicks(double dt)
	{
		int tickFor = 20;
		
		for(int i = 0; i < tests.length; i++)
		{
			Test test = tests[i];
			if(test.done)
				continue;
			
			for(int t = 0; t < tickFor; t++)
			{
				test.score += test.board.updateBoard(dt);
				
				if(test.board.canMoveOn())
				{
					test.board.removeSteggar();
					test.done = true;
					test.end = test.countDist();
					test.fitness = test.getFitness();
				}
			}
		}
	}
	
	public void applyBest(Goti steggar)
	{
		float ang = FloatMath.toRadians(90 * (player % 4));
		steggar.pos.set(best.pos).subtractLocal(Carrom.bw / 2, Carrom.bh / 2);
		steggar.pos.rotateAround(ang, true);
		steggar.pos.addLocal(Carrom.bw / 2, Carrom.bh / 2);
		steggar.vel.set(best.vel).rotateAround(ang, true);
	}
	
	public Test getBest()
	{
		return best;
	}
	
	public void nextGeneration()
	{
		gen++;
		float sum = 0;
		for(int i = 0; i < tests.length; i++)
		{
			sum += tests[i].fitness;
		}
		for(int i = 0; i < tests.length; i++)
		{
			tests[i].fitness /= sum;
		}
		Test[] newGen = new Test[tests.length];
		for(int i = 0; i < tests.length; i++)
		{
			Test n = null;
			if(Rand.nextFloat() < 0.1F)
				n = new Test(testBoard);
			else
				n = new Test(testBoard, getRandomTest());
			n.pos.addLocal(Vector2F.randUnitVector());
			n.pos.x = MathUtil.between(100, n.pos.x, Carrom.bw - 100);
			n.pos.y = MathUtil.between(Carrom.bh - 90, n.pos.y, Carrom.bh - 60);
			
			n.vel.addLocal(Vector2F.randUnitVector());
			if(n.vel.y > 0)
			{
				n.vel.y = 0;
			}
			if(n.vel.length() > 1000)
			{
				n.vel.normalizeLocal().multLocal(1000);
			}
			newGen[i] = n;
		}
		System.arraycopy(newGen, 0, tests, 0, tests.length);
	}
	
	private Test getRandomTest()
	{
		float rng = Rand.nextFloat();
		int i = 0;
		for(; i < tests.length; i++)
		{
			rng -= tests[i].fitness;
			
			if(rng <= 0)
			{
				break;
			}
		}
		return tests[i];
	}
	
	private boolean figuredItOut()
	{
		for(int i = 0; i < tests.length; i++)
		{
			if(!tests[i].done)
				return false;
	
		}
		return true;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public class Test implements Comparable<Test>
	{
		public final Board board;
		public final Vector2F pos, vel;
		private boolean done = false;
		private int score = 0;
		private float start, end, fitness;
		
		private Test(Board b)
		{
			board = new Board(b);
			pos = new Vector2F();
			pos.x = Rand.nextFloat(100, Carrom.bw - 100);
			pos.y = Rand.nextFloat(100, Carrom.bh - 100);
			vel = new Vector2F(Rand.nextFloat(10, 1000), 0);
			vel.rotateAround(Rand.nextFloat(FloatMath.PI), false);
			start = countDist();
		}
		
		private Test(Board b, Test parent)
		{
			board = new Board(b);
			pos = new Vector2F(parent.pos);
			vel = new Vector2F(parent.vel);
			start = countDist();
		}
		
		private float getFitness()
		{
			float f = Math.max(1, start - end);
			f *= Math.max(1, score);
			return f;
		}
		
		public int getScore()
		{
			return score;
		}
		
		private float countDist()
		{
			float total = 0;
			List<Goti> lst = board.gotis;
			for(int i = 0; i < lst.size(); i++)
			{
				Goti g = lst.get(i);
				if(g.type != Goti.Type.STEGGAR)
					total += g.leastDist(board.holes) / g.type.reward;
			}
			return total;
		}

		@Override
		public int compareTo(Test o)
		{
			return o == null ? 1 : Float.compare(o.getFitness(), getFitness());
		}
	}
}
