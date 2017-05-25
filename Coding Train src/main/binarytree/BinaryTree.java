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
package main.binarytree;

import java.awt.Color;

import com.hk.math.Rand;

import main.G2D;
import main.Game;
import main.GameSettings;
import main.GameSettings.Quality;
import main.Main;

public class BinaryTree extends Game
{
	public final Node root;
	
	public BinaryTree()
	{
		root = new Node(0);
		
		for(int i = 0; i < 20; i++)
		{
			root.addNode(new Node(Rand.nextInt(-100, 100)));
		}
		
		root.print();
	}
	
	public void paint(G2D g2d, Node node, int x, int y, int w, int h)
	{
		g2d.drawRectangle(x, y, w, h);
		g2d.drawString(String.valueOf(node.val), x, y + h);
		
		if(node.left != null)
		{
			paint(g2d, node.left, x - w * 1 / 4, y + 50, w / 2, 50);
		}
		if(node.right != null)
		{
			paint(g2d, node.right, x + w * 1 / 4, y + 50, w / 2, 50);
		}
	}
	
	@Override
	public void update(int ticks)
	{}

	@Override
	public void paint(G2D g2d)
	{
		g2d.setFontSize(18F);
		g2d.enable(G2D.G_CENTER);
		paint(g2d, root, g2d.width / 2, -25, g2d.width, 50);
		g2d.disable(G2D.G_CENTER);
	}
	
	public static void main(String[] args)
	{
		BinaryTree game = new BinaryTree();
		
		GameSettings settings = new GameSettings();
		settings.title = "Binary Tree";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1024;
		settings.height = 768;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;
		
		Main.initialize(game, settings);
	}
}
