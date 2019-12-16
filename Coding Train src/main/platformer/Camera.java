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
package main.platformer;

import java.awt.geom.AffineTransform;

import com.hk.math.MathUtil;
import com.hk.math.vector.Vector2F;

public class Camera
{
	public final Platformer game;
	public final float zoom;
	public final Vector2F move;
	
	public Camera(Platformer game, float zoom)
	{
		this.game = game;
		this.zoom = MathUtil.between(1, zoom, 20);
		move = new Vector2F();
	}
	
	public void apply(AffineTransform aft)
	{
		double ax = (game.game.width - (game.game.width * zoom)) / 2D;
		double ay = (game.game.height - (game.game.height * zoom)) / 2D;
		aft.translate(ax, ay);
		aft.scale(zoom, zoom);
		aft.translate(move.x, move.y);
	}
}
