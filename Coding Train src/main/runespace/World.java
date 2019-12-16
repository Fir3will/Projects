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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class World
{
	public final Runespace game;
	private final List<Entity> entities, toAdd, toRemove;

	World(Runespace game)
	{
		this.game = game;
		entities = new ArrayList<>();
		toAdd = new ArrayList<>();
		toRemove = new ArrayList<>();
	}

	public void updateWorld(double delta)
	{
		entities.addAll(toAdd);
		entities.removeAll(toRemove);
		toAdd.clear();
		toRemove.clear();

		Iterator<Entity> itr = entities.iterator();
		while(itr.hasNext())
		{
			Entity entity = itr.next();
			entity.updateEntity(delta);

			if(entity.toRemove())
				itr.remove();
		}
	}

	public void addEntity(Entity entity)
	{
		toAdd.add(entity);
	}

	public void removeEntity(Entity entity)
	{
		toRemove.add(entity);
	}

	public List<Entity> getEntities()
	{
		return entities;
	}
}
