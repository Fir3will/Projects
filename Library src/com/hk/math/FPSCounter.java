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
package com.hk.math;

import com.hk.util.Requirements;

public class FPSCounter
{
	private double fps;
	private final int maxFrames;
	private int frames;
	private long start = -1;

	public FPSCounter(int maxFrames)
	{
		this.maxFrames = Requirements.requireInBounds(1, maxFrames, Integer.MAX_VALUE);
	}

	public void calc()
	{
		frames++;

		long time = System.currentTimeMillis();
		if (frames == maxFrames)
		{
			long elapsed = time - start;
			start = time;

			fps = 1D / (elapsed / 1000D / maxFrames);

			frames = 0;
		}
	}

	public double getFPS()
	{
		return fps;
	}
}
