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

public class Node
{
	public final int val;
	public Node left, right;
	
	public Node(int val)
	{
		this.val = val;
	}
	
	public void addNode(Node node)
	{
		if(val > node.val)
		{
			if(left == null)
			{
				left = node;
			}
			else
			{
				left.addNode(node);
			}
		}
		else if(val < node.val)
		{
			if(right == null)
			{
				right = node;
			}
			else
			{
				right.addNode(node);
			}
		}
	}
	
	public void print()
	{
		if(left != null)
		{
			left.print();
		}
		System.out.println(val);
		if(right != null)
		{
			right.print();
		}
	}
}
