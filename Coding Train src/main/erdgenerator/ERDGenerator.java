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

import javax.swing.JOptionPane;

import com.hk.g2d.G2D;
import com.hk.g2d.Game;
import com.hk.g2d.GameFrame;
import com.hk.g2d.GuiScreen;
import com.hk.g2d.Settings;
import com.hk.g2d.Settings.Quality;

public class ERDGenerator extends GuiScreen
{
	public final List<Entity> entities;
	public Entity selectedEntity;
	public int selected = -1;

	public ERDGenerator(Game game)
	{
		super(game);
		
		addOverlay(new SelectOverlay(this, 20, 480));
		entities = new ArrayList<>();

		Entity patient = new Entity("Student", 450, 150);
		patient.addAttribute("id").identifier = true;
		patient.addAttribute("firstName");
		patient.addAttribute("lastName");
		patient.addAttribute("dateOfBirth");
		patient.addAttribute("address").mutivalue = true;
		entities.add(patient);

		Entity professor = new Entity("Professor", 170, 310);
		professor.addAttribute("id").identifier = true;
		professor.addAttribute("firstName");
		professor.addAttribute("lastName");
		professor.addAttribute("department");
		entities.add(professor);

//		Entity random = new Entity("Random", 100, 400);
//		int r = Rand.nextInt(2, 20);
//		while(r-- > 0)
//		{
//			random.addAttribute(Rand.nextString(Rand.nextInt(5, 25)));
//		}
//		entities.add(random);
	}
	
	@Override
	public void update(double delta)
	{
		
	}

	@Override
	public void paint(G2D g2d)
	{
		for(Entity entity : entities)
		{
			if(entity != selectedEntity)
				entity.paintEntity(g2d);
		}
	}

	public void mouse(float x, float y, boolean pressed)
	{
		boolean updated = false;
		if(pressed)
		{
			if(selected == 2)
			{
				for(Entity entity : entities)
				{
					if(entity.contains(x, y))
					{
						addOverlay(new AddRelationOverlay(this, entity, x, y));
						break;
					}
				}
			}
		}
		else
		{
			switch(selected)
			{
				case 0:
				{
					String entity = JOptionPane.showInputDialog("Enter entity name:");
					if(entity != null && !entity.trim().isEmpty())
					{
						entities.add(new Entity(entity, x, y));
						updated = true;
					}
					break;
				}
				case 1:
				{
					for(Entity entity : entities)
					{
						if(entity.contains(x, y))
						{
							String attribute = JOptionPane.showInputDialog("Enter new attribute for '" + entity.name + "'.");
							entity.addAttribute(attribute);
							updated = true;
							break;
						}
					}
					break;
				}
				default:
				{
					for(Entity entity : entities)
					{
						if(entity.contains(x, y))
						{
							selectedEntity = entity;
							addOverlay(new EntityOverlay(this));
							break;
						}
					}
					break;
				}
			}
		}
		
		if(updated)
			selected = -1;
	}
	
	public static void main(String[] args)
	{
		Settings settings = new Settings();
		settings.title = "ERD Generator";
//		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1000;
		settings.height = 700;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;

		GameFrame frame = GameFrame.create(settings);
		frame.game.setCurrentScreen(new ERDGenerator(frame.game));
		frame.launch();
	}
}
