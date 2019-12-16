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
package main.clock;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time
{
	public final int h, m, s, ml;
	public final float hn, mn, sn, mln;
	public final String hs, ms, ss, mls;
	public final boolean pm;
	
	public Time(long time)
	{
		Calendar c = GregorianCalendar.getInstance();
		c.setTimeInMillis(time);
		
		int th = c.get(Calendar.HOUR_OF_DAY);
		pm = th > 12;

		h = pm ? th - 12 : th;
		hs = h < 10 ? "0" + h : String.valueOf(h);
		hn = h / 12F;
		
		m = c.get(Calendar.MINUTE);
		ms = m < 10 ? "0" + m : String.valueOf(m);
		mn = m / 60F;
		
		s = c.get(Calendar.SECOND);
		ss = s < 10 ? "0" + s : String.valueOf(s);
		sn = s / 60F;
		
		ml = c.get(Calendar.MILLISECOND);
		mls = ml < 10 ? "00" + ml : ml < 100 ? "0" + ml : String.valueOf(ml);
		mln = ml / 1000F;
	}
}
