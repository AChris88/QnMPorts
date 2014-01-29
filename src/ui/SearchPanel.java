package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import da.DataAccess;

/**
 * Search interface to interact with data access.
 * 
 * @author Chris Allard
 * 
 */
public class SearchPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private String selection = "";
	private DataAccess manager = null;
	/**
	 * Constructor method.
	 * 
	 * @param manager
	 */
	public SearchPanel(DataAccess manager) {
		super();
		this.manager = manager;
		initialize();
	}

	/**
	 * Initializes all essential components.
	 */
	private void initialize() {
		setLayout(new GridLayout(7, 6));
		final JButton search = new JButton("Search");
		final JTextField value = new JTextField();
		final JCheckBox check = new JCheckBox("Ranged");
		final JTextField startField = new JTextField();
		final JTextField endField = new JTextField();
		
		FocusListener focusListener = new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				JTextField field = (JTextField) arg0.getSource();
				field.selectAll();
			}

			@Override
			public void focusLost(FocusEvent arg0) {}
		};
		
		ActionListener checkListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (check.isSelected() && 
						(startField.getText().trim().equals("") || endField.getText().trim().equals("")))
					search.setEnabled(false);
				else if (!check.isSelected() && search.getText().trim().equals(""))
					search.setEnabled(true);
			}};
		
		ActionListener enterListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				search.doClick();
			}};
		
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());

		add(new JLabel());
		add(new JLabel("Search Field:"));
		final Vector<String> fields = new Vector<String>();
		fields.add("");
		fields.add("Port #");
		fields.add("Protocol");
		fields.add("Name");
		fields.add("Description");

		final JComboBox<String> combo = new JComboBox<String>(fields);
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection = ((JComboBox<?>) e.getSource()).getSelectedItem()
						.toString();
				if (selection.equals("")) {
					value.setText("");
					manager.showAllPorts();
				} else {
					value.setText("");
				}
				
				search.setEnabled(false);
			}
		});
		add(combo);

		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel("Search Value:"));
		value.addActionListener(enterListener);
		value.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {	}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!combo.getSelectedItem().equals("") && !value.getText().trim().equals(""))
					search.setEnabled(true);
				else if (startField.getText().trim().equals("") || endField.getText().trim().equals(""))
					search.setEnabled(false);
			}

			@Override
			public void keyTyped(KeyEvent arg0) { }
			});
		value.addFocusListener(focusListener);
		add(value);
		add(new JLabel());

		add(new JLabel());
		add(new JLabel());
		check.addActionListener(checkListener);
		add(check);
		
		startField.setVisible(false);

		startField.addActionListener(enterListener);
		startField.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {	}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!startField.getText().trim().equals("") && !endField.getText().trim().equals(""))
					search.setEnabled(true);
				else
					search.setEnabled(false);
			}

			@Override
			public void keyTyped(KeyEvent arg0) { }
			});
		startField.addFocusListener(focusListener);
		add(startField);
		final JLabel to = new JLabel("to");
		to.setVisible(false);
		to.setHorizontalAlignment(0);
		add(to);
		
		endField.addActionListener(enterListener);
		endField.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {	}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(!startField.getText().trim().equals("") && !endField.getText().trim().equals(""))
					search.setEnabled(true);
				else
					search.setEnabled(false);
			}

			@Override
			public void keyTyped(KeyEvent arg0) { }
			});
		endField.setVisible(false);
		endField.addFocusListener(focusListener);
		add(endField);

		add(new JLabel());
		add(new JLabel());

		add(new JLabel());
		add(new JLabel());

		add(new JLabel());
		add(new JLabel());
		search.setEnabled(false);
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean ranged = check.isSelected() ? true : false;

				switch (selection) {
				case "Port #":
					manager.getPorts(value.getText(), "port");
					break;
				case "Protocol":
					manager.getPorts(value.getText(), "protocol");
					break;
				case "Name":
					manager.getPorts(value.getText(), "name");
					break;
				case "Description":
					manager.getPorts(value.getText(), "description");
					break;
				default:
					if (!ranged)
						JOptionPane
							.showMessageDialog(
									null,
									"Error, make sure you select a field to search by!",
									"Search Error", JOptionPane.ERROR_MESSAGE);
				}

				if (ranged)
					manager.getByPortRange(startField.getText(),
							endField.getText());
			}
		});
		add(search);
		add(new JLabel());
		final JButton update = new JButton("Update");
		update.setEnabled(true);
		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				manager.updateSource();
			}
		});
		add(update);
		add(new JLabel());

		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (check.isSelected()) {
					startField.setVisible(true);
					to.setVisible(true);
					endField.setVisible(true);
					fields.remove("Port #");
					search.setEnabled(false);
				} else {
					startField.setVisible(false);
					startField.setText("");
					to.setVisible(false);
					endField.setVisible(false);
					endField.setText("");
					fields.insertElementAt("Port #", 1);
					search.setEnabled(true);
				}
			}
		});
		
		
		
		setVisible(true);
	}
}