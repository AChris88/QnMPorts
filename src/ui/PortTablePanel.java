package ui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel to contain the port table.
 * @author Chris Allard
 *
 */
public class PortTablePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private PortTable table = null;
	
	/**
	 * Constructor method.
	 */
	public PortTablePanel(){
		super();
	}

	/**
	 * Constructor method.
	 * @param table which panel will contain.
	 */
	public PortTablePanel(PortTable table){
		this();
		this.table=table;
		initialize();
	}

	/**
	 * Initializes all essential components. 
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(table);
		setLayout(new BorderLayout());
		add(scroll,BorderLayout.CENTER);
		setVisible(true);
	}
}