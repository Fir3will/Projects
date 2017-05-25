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
