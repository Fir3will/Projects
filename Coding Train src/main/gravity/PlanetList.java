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
package main.gravity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class PlanetList extends JFrame implements ActionListener
{
	private final Gravity game;
	private final DefaultListModel<Planet> planetModel;
    private final JButton addButton, deleteButton;
    private final JTextField nameField;
    private final JList<Planet> planetList;
    private final JFormattedTextField posXField, posYField;
    private final JFormattedTextField velXField, velYField;
    private final JFormattedTextField accXField, accYField;
	
	public PlanetList(Gravity game)
    {
		this.game = game;
        JScrollPane sp = new JScrollPane();
        planetList = new JList<>();
        JPanel selPanel = new JPanel();
        JLabel nameLabel = new JLabel();
        nameField = new JTextField();
        JLabel positionLabel = new JLabel();
        posXField = new JFormattedTextField();
        posYField = new JFormattedTextField();
        JLabel velocityLabel = new JLabel();
        velXField = new JFormattedTextField();
        velYField = new JFormattedTextField();
        deleteButton = new JButton();
        JLabel accelerationLabel = new JLabel();
        accXField = new JFormattedTextField();
        accYField = new JFormattedTextField();
        addButton = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Celestial Body Controller");

        planetModel = new DefaultListModel<>();
        planetList.setModel(planetModel);
        planetList.addListSelectionListener(new ListSelectionListener()
        {
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				refresh();
			}
        });
        sp.setViewportView(planetList);

        selPanel.setBorder(BorderFactory.createTitledBorder("Selected Body"));

        nameLabel.setText("Name:");

        nameField.setEditable(false);
        nameField.setText("name");

        positionLabel.setText("Position:");

        NumberFormatter nf = new NumberFormatter(new DecimalFormat("#,###.##########"));
        posXField.setFormatterFactory(new DefaultFormatterFactory(nf));
        posXField.setText("0");
        posXField.addActionListener(this);
        posXField.setActionCommand("px");

        posYField.setFormatterFactory(new DefaultFormatterFactory(nf));
        posYField.setText("0");
        posYField.addActionListener(this);
        posYField.setActionCommand("py");

        velocityLabel.setText("Velocity:");

        velXField.setFormatterFactory(new DefaultFormatterFactory(nf));
        velXField.setText("0");
        velXField.addActionListener(this);
        velXField.setActionCommand("vx");

        velYField.setFormatterFactory(new DefaultFormatterFactory(nf));
        velYField.setText("0");
        velYField.addActionListener(this);
        velYField.setActionCommand("vy");

        deleteButton.setText("Delete Body");
        deleteButton.setActionCommand("Delete");
        deleteButton.addActionListener(this);

        accelerationLabel.setText("Acceleration:");

        accXField.setEditable(false);
        accXField.setFormatterFactory(new DefaultFormatterFactory(nf));
        accXField.setText("0");

        accYField.setEditable(false);
        accYField.setFormatterFactory(new DefaultFormatterFactory(nf));
        accYField.setText("0");
        
        GroupLayout selPanelLayout = new GroupLayout(selPanel);
        selPanel.setLayout(selPanelLayout);
        selPanelLayout.setHorizontalGroup(
            selPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(selPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                    .addComponent(positionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, selPanelLayout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameField))
                    .addGroup(selPanelLayout.createSequentialGroup()
                        .addComponent(posXField)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(posYField))
                    .addComponent(velocityLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(selPanelLayout.createSequentialGroup()
                        .addComponent(velXField)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(velYField))
                    .addComponent(accelerationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(selPanelLayout.createSequentialGroup()
                        .addComponent(accXField)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(accYField)))
                .addContainerGap())
        );
        selPanelLayout.setVerticalGroup(
            selPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(selPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(selPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(positionLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(posXField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(posYField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(velocityLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(velXField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(velYField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accelerationLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(selPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(accXField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(accYField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteButton)
                .addContainerGap())
        );

        addButton.setText("Add New Body");
        addButton.setActionCommand("Add");
        addButton.addActionListener(this);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sp, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(selPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sp)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }
	
	public void setCompsEnabled(boolean enabled)
	{
		nameField.setEnabled(enabled);
		posXField.setEnabled(enabled);
		posYField.setEnabled(enabled);
		velXField.setEnabled(enabled);
		velYField.setEnabled(enabled);
		accXField.setEnabled(enabled);
		accYField.setEnabled(enabled);
		deleteButton.setEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String ac = e.getActionCommand();
		if(ac != null && ac.length() == 2)
		{
			Planet sel = planetList.getSelectedValue();
			if(ac.charAt(0) == 'p')
			{
				if(ac.charAt(1) == 'x')
				{
					sel.pos.x = ((Number) posXField.getValue()).floatValue();
				}
				else
				{
					sel.pos.y = ((Number) posYField.getValue()).floatValue();
				}
			}
			else if(ac.charAt(0) == 'v')
			{
				if(ac.charAt(1) == 'x')
				{
					sel.vel.x = ((Number) velXField.getValue()).floatValue();
				}
				else
				{
					sel.vel.y = ((Number) velYField.getValue()).floatValue();
				}
			}
		}
		else if("Delete".equals(ac))
		{
			planetModel.removeElement(game.planets.remove(planetList.getSelectedIndex()));
		}
		else if("Add".equals(ac))
		{
			float mass = getInputMass();
			String planetName = JOptionPane.showInputDialog(this, "Enter the planet's name.");
			
			if(mass > 0F)
			{
				game.planets.add(new Planet(planetName, 0F, 0F, mass));
				refreshList();
			}
		}
	}
	
	public void refreshList()
	{
		int sl = planetList.getSelectedIndex();
		planetModel.clear();
		for(int i = 0; i < game.planets.size(); i++)
		{
			planetModel.addElement(game.planets.get(i));
		}
		planetList.setSelectedIndex(sl);
		refresh();
	}
	
	public void refresh()
	{
		Planet pl = planetList.getSelectedValue();
		if(pl == null)
		{
			setCompsEnabled(false);
		}
		else
		{
			setCompsEnabled(true);
			nameField.setText(pl.name);
			posXField.setValue(pl.pos.x);
			posYField.setValue(pl.pos.y);
			velXField.setValue(pl.vel.x);
			velYField.setValue(pl.vel.y);
			accXField.setValue(pl.acc.x);
			accYField.setValue(pl.acc.y);
		}
	}
	
	private float getInputMass()
	{
		String in = JOptionPane.showInputDialog(this, "Enter the planet's mass.\n(Decimals allowed)");
		while(true)
		{
			boolean wrong = false;
			float mass = -1;
			try
			{
				mass = Float.parseFloat(in);
			}
			catch(NumberFormatException ex)
			{
				wrong = true;
			}
			
			if(wrong)
			{
				in = JOptionPane.showInputDialog(this, "Enter the planet's mass.\n(Decimals allowed)\nINVALID DECIMAL NUMBER");
				continue;
			}
			
			return mass <= 0 ? -1 : mass;
		}
	}

	private static final long serialVersionUID = 1L;
}
