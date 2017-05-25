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

import main.gui.GuiScreen;
import main.gui.widget.GuiButton;
import main.gui.widget.GuiTextField;

public class TestScreen extends GuiScreen
{
	public final Testing game;
	
	public TestScreen(Testing game)
	{
		this.game = game;
	}
	
	public void initialize(Handler handler)
	{
		super.initialize(handler);
		addWidget(new GuiButton(this, 0, "Hello", 450, 100));
		addWidget(new GuiTextField(this, 100, 100).setText("Hello").setHorizontalAlignment(GuiTextField.HORIZ_LEFT));
	}
}
