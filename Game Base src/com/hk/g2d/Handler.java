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
package com.hk.g2d;

import com.hk.math.vector.Vector2F;

public interface Handler
{	
	public void update();
	
	public Vector2F get();
	
	public float getX();
	
	public float getY();
	
	public int getButton();

	public boolean isKeyDown(int keyCode);
	
	public boolean isPressed();
}
