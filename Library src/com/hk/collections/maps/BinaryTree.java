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
package com.hk.collections.maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BinaryTree<K extends Comparable<K>, V> implements Map<K, V>
{
	private Entry root;

	public BinaryTree()
	{

	}

	@Override
	public void clear()
	{

	}

	@Override
	public boolean containsKey(Object key)
	{
		return false;
	}

	@Override
	public boolean containsValue(Object value)
	{
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return null;
	}

	@Override
	public V get(Object key)
	{
		return null;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public Set<K> keySet()
	{
		return null;
	}

	@Override
	public V put(K key, V value)
	{
		if (root == null)
		{
			root = new Entry(key, value);
		}
		return value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{

	}

	@Override
	public V remove(Object key)
	{
		return null;
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public Collection<V> values()
	{
		return null;
	}

	private Entry lookFor(K key)
	{
		if (root != null)
		{

		}
		return null;
	}

	public class Entry implements Map.Entry<K, V>, Comparable<Entry>
	{
		private Entry left, right;
		private final K key;
		private V value;

		private Entry(K key, V value)
		{
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey()
		{
			return key;
		}

		@Override
		public V getValue()
		{
			return value;
		}

		@Override
		public V setValue(V value)
		{
			return this.value = value;
		}

		private Entry lookFor(K key)
		{
			int v = this.key == null ? this.key == key ? 0 : -1 : this.key.compareTo(key);
			if (v == 0)
			{
				return this;
			}
			if (left != null)
			{

			}
			return null;
		}

		@Override
		public int compareTo(BinaryTree<K, V>.Entry o)
		{
			if (o == this)
			{
				return 0;
			}
			return o == null ? 1 : key.compareTo(o.key);
		}
	}
}
