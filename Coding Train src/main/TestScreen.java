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
