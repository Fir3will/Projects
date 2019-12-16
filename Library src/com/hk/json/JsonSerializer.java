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
package com.hk.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

class JsonSerializer
{
	private JsonSerializer()
	{}

	static JsonValue toJson(Object obj)
	{
		if (obj instanceof JsonValue)
		{
			return (JsonValue) obj;
		}
		else if (obj instanceof Iterable)
		{
			Iterator<?> itr = ((Iterable<?>) obj).iterator();
			JsonArray arr = new JsonArray();
			while (itr.hasNext())
			{
				arr.add(itr.next());
			}
			return arr;
		}
		else if (obj instanceof Iterator)
		{
			Iterator<?> itr = (Iterator<?>) obj;
			JsonArray arr = new JsonArray();
			while (itr.hasNext())
			{
				arr.add(itr.next());
			}
			return arr;
		}

		else if (obj instanceof Enumeration)
		{
			Enumeration<?> enm = (Enumeration<?>) obj;
			JsonArray arr = new JsonArray();
			while (enm.hasMoreElements())
			{
				arr.add(enm.nextElement());
			}
			return arr;
		}
		else if (obj != null)
		{
			Field[] fs = getFields(obj.getClass()).toArray(new Field[0]);
			JsonObject json = new JsonObject();
			try
			{
				for (Field f : fs)
				{
					boolean accessible = f.isAccessible();
					f.setAccessible(true);
					json.set(f.getName(), f.get(obj));
					f.setAccessible(accessible);
				}
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
			return json;
		}
		else
		{
			return JsonNull.NULL;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> Object fromJson(JsonValue value, Class<T> cls)
	{
		if (value == null || value.isNull())
		{
			return null;
		}
		else if (value.isArray())
		{
			ArrayList<T> list = new ArrayList<>();
			JsonArray arr = value.getArray();
			for (int i = 0; i < arr.size(); i++)
			{
				list.add((T) fromJson(arr.get(i), cls.getTypeParameters()[0].getGenericDeclaration()));
			}
			return list;
		}
		else if (value.isObject())
		{
			JsonObject json = value.getObject();
			try
			{
				Field[] fs = getFields(cls).toArray(new Field[0]);
				T t = cls.newInstance();
				for (Field f : fs)
				{
					Class<?> fc = f.getType();
					Object o = Json.jsonToObject(fc, json.get(f.getName()));
					if (o instanceof Number)
					{
						Number n = (Number) o;
						if (fc.equals(Double.class) || fc.equals(double.class))
						{
							o = fc.equals(Double.class) ? Double.valueOf(n.doubleValue()) : n.doubleValue();
						}
						else if (fc.equals(Float.class) || fc.equals(float.class))
						{
							o = fc.equals(Float.class) ? Float.valueOf(n.floatValue()) : n.floatValue();
						}
						else if (fc.equals(Long.class) || fc.equals(long.class))
						{
							o = fc.equals(Long.class) ? Long.valueOf(n.longValue()) : n.longValue();
						}
						else if (fc.equals(Integer.class) || fc.equals(int.class))
						{
							o = fc.equals(Integer.class) ? Integer.valueOf(n.intValue()) : n.intValue();
						}
						else if (fc.equals(Short.class) || fc.equals(short.class))
						{
							o = fc.equals(Short.class) ? Short.valueOf(n.shortValue()) : n.shortValue();
						}
						else if (fc.equals(Byte.class) || fc.equals(byte.class))
						{
							o = fc.equals(Byte.class) ? Byte.valueOf(n.byteValue()) : n.byteValue();
						}
					}
					boolean accessible = f.isAccessible();
					f.setAccessible(true);
					f.set(t, o);
					f.setAccessible(accessible);
				}
				return t;
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
		throw new IllegalArgumentException("Cannot deserialize class [" + cls.getName() + "]");
	}

	static List<Field> getFields(Class<?> cls)
	{
		if (cls.equals(Object.class))
		{
			return Collections.emptyList();
		}
		List<Field> lst = new ArrayList<>();
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs)
		{
			int md = f.getModifiers();
			if (!Modifier.isStatic(md) && !Modifier.isTransient(md))
			{
				lst.add(f);
			}
		}
		if (!cls.getSuperclass().equals(Object.class))
		{
			lst.addAll(getFields(cls.getSuperclass()));
		}
		return lst;
	}
}
