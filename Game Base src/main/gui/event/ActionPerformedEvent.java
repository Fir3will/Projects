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
package main.gui.event;

import main.gui.widget.GuiTextField;

public class ActionPerformedEvent extends BasicEvent
{
	public GuiTextField field;
	
	public ActionPerformedEvent(GuiTextField field)
	{
		super(field);
		this.field = field;
	}
}