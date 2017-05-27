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
package main;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.GameSettings.Quality;
import main.gui.UIGame;
import main.planetjumper.Jumper;
import main.planetjumper.Planetoid;
import main.planetjumper.Player;

public class Testing extends UIGame
{
	public Testing()
	{
		
	}
	
	public void initialize()
	{
		setCurrScreen(new TestScreen(this));
	}

	public static void main(String[] args)
	{
		System.setProperty("Main.WIDTH", String.valueOf(1200));
		System.setProperty("Main.HEIGHT", String.valueOf(900));
		Testing game = new Testing();
		
		GameSettings settings = new GameSettings();
		settings.title = "Testing";
		settings.version = "0.0.1";
		settings.quality = Quality.GOOD;
		settings.width = 1200;
		settings.height = 900;
		settings.showFPS = true;
		settings.background = Color.WHITE;
		settings.maxFPS = -1;
		Main.initialize(game, settings);
		
		genUML(Jumper.class);
		genUML(Planetoid.class);
		genUML(Player.class);
		System.exit(0);
	}
	
	private static void genUML(Class<?> cls)
	{
		String txt = "#### " + cls.getSimpleName() + ".java\n##### Fields\n";
		List<Field> fds = new ArrayList<>();
		Collections.addAll(fds, cls.getDeclaredFields());
		for(Field fd : fds)
		{
			int mod = fd.getModifiers();
			String s = getMod(mod);
			s += fd.getName() + (Modifier.isStatic(mod) ? "**" : "") + ":`" + name(fd.getType()) + "`  ";
			txt += s + '\n';
		}
		txt += "##### Methods\n";
		List<Constructor<?>> cds = new ArrayList<>();
		Collections.addAll(cds, cls.getDeclaredConstructors());
		for(Constructor<?> cd : cds)
		{
			if(cd.isSynthetic()) continue;
			int mod = cd.getModifiers();
			String s = getMod(mod);
			String pms = "";
			Class<?>[] params = cd.getParameterTypes();
			for(Class<?> param : params)
			{
				pms += "`" + name(param) + "`, ";
			}
			s += cls.getSimpleName() + (Modifier.isStatic(mod) ? "**" : "") + "(" + (pms.length() > 2 ? pms.substring(0, pms.length() - 2) : "") + ")  ";
			txt += s + '\n';
		}
		List<Method> mds = new ArrayList<>();
		Collections.addAll(mds, cls.getDeclaredMethods());
		for(Method md : mds)
		{
			if(md.isSynthetic()) continue;
			int mod = md.getModifiers();
			String s = getMod(mod);
			String pms = "";
			Class<?>[] params = md.getParameterTypes();
			for(Class<?> param : params)
			{
				pms += "`" + name(param) + "`, ";
			}
			s += md.getName() + (Modifier.isStatic(mod) ? "**" : "") + "(" + (pms.length() > 2 ? pms.substring(0, pms.length() - 2) : "") + ")"+ ":`" + name(md.getReturnType()) + "`  ";
			txt += s + '\n';
		}
		System.out.println(txt.trim() + "\n***");
	}
	
	private static String getMod(int mod)
	{
		String s = "`";
		if(Modifier.isPublic(mod))
		{
			s += "+` ";
		}
		else if(Modifier.isPrivate(mod))
		{
			s += "-` ";
		}
		else
		{
			s += "#` ";
		}
		if(Modifier.isStatic(mod))
		{
			s += "**";
		}
		return s;
	}
	
	private static String name(Class<?> cls)
	{
		if(cls.isArray())
		{
			return name(cls.getComponentType()) + "[]";
		}
		else
		{
			return cls.getSimpleName();
		}
	}
}
