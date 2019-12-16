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
package main.runespace;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GuiOverlay;

public class ModelOverlay extends GuiOverlay
{
	public final Runespace game;
	public final World world;
	private final Map<String, BufferedImage> models;
	private int step = 0, dir = 1;
	private double inc;

	ModelOverlay(Runespace game)
	{
		this.game = game;
		world =  game.world;

		models = new HashMap<>();
	}

	@Override
	public void initialize(Game game)
	{
		char[] dirs = "NESW".toCharArray();
		Map<File, BufferedImage> imgs = new HashMap<>();
		for(Model model : Model.values())
		{
			File f = model.mdlData.charaset;
			BufferedImage img = imgs.get(f);
			if(img == null)
			{
				try
				{
					imgs.put(f, img = ImageIO.read(f));
				}
				catch (IOException e)
				{
					throw new RuntimeException("Exception loading model (0x" + Integer.toHexString(model.ordinal()).toUpperCase() + ") '" + f.getAbsolutePath() + "'", e);
				}
			}
			int y = model.mdlData.ty;
			for(int i = 0; i < 4; i++)
			{
				int x = model.mdlData.tx;
				for(int j = 0; j < 3; j++)
				{
					models.put(model.name() + ":" + dirs[i] + ":" + j, get16x18(img, x, y, model.background));
					x += 16;
				}
				y += 18;
			}
		}

		for(BufferedImage img : imgs.values())
		{
			img.flush();
		}
	}

	@Override
	public void updateOverlay(double delta)
	{
		inc += delta;

		if(inc >= 0.15)
		{
			inc = 0;
			step += dir;
			if(step == 2 || step == 0)
			{
				dir = -dir;
			}
		}
	}

	@Override
	public void paintOverlay(G2D g2d)
	{
		for(Entity entity : world.getEntities())
		{
			Model mdl = entity.getModel();
			String label = mdl.name() + ":" + entity.direction.letter + ":" + (entity.moving && !entity.still ? step : 1);
			BufferedImage img = models.get(label);
			g2d.pushMatrix();
			g2d.translate((entity.xCoord + 0.5F + entity.dx * 0.05) * 54 - (img.getWidth() / 2) * 3, (entity.yCoord + 0.5F + entity.dy * 0.05) * 54 - (img.getHeight() / 2) * 3);
			g2d.scale(3, 3);
			g2d.drawImage(img, 0, 0);
			g2d.popMatrix();
		}
	}

	private static BufferedImage get16x18(BufferedImage img, int x, int y, int bck)
	{
		BufferedImage clip = new BufferedImage(16, 18, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = clip.createGraphics();
		g2d.drawImage(img.getSubimage(x, y, 16, 18), 0, 0, null);
		for(x = 0; x < 16; x++)
		{
			for(y = 0; y < 18; y++)
			{
				if(clip.getRGB(x, y) == bck)
				{
					clip.setRGB(x, y, 0);
				}
			}
		}
		g2d.dispose();
		return clip;
	}
}
