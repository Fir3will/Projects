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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.ell.EllException;

public class Loader
{
	private final GameCase[] games;
	
	public Loader(File ellFolder)
	{
		List<GameCase> ellGames = new ArrayList<>();
		if(ellFolder.exists())
		{
			File[] fs = ellFolder.listFiles();
			for(File f : fs)
			{
				if(f.getName().endsWith(".ell"))
				{
					try
					{
						ellGames.add(GameCase.attach(f));
					}
					catch (EllException e)
					{
						e.printStackTrace();
					}
					catch(Exception e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}
		
		games = new GameCase[ellGames.size()];
		ellGames.toArray(games);
		Arrays.sort(games);
	}
	
	public GameCase getGame(int i)
	{
		return games[i];
	}
	
	public int size()
	{
		return games.length;
	}
}
