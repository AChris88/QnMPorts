package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import da.DataAccess;
import da.PortTableModel;

/**
 * Container for application interface.
 * @author Chris Allard
 *
 */
public class ClientFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	private PortTable table = null;
	private PortTableModel model = null;
	private DataAccess manager = null;

	/**
	 * Constructor method.
	 */
	public ClientFrame(){
		super("QnMPorts");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initialize();
	}

	/**
	 * Initializes all essential components. 
	 */
	private void initialize() {
		model = new PortTableModel();
		manager = new DataAccess(model);
		table=new PortTable(model);

		PortTablePanel panel = new PortTablePanel(table);

		setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		SearchPanel search = new SearchPanel(manager);

		c.gridx=0;
		c.gridy=0;
		c.gridheight=1;
		c.gridwidth=3;
		c.weightx=.3;
		c.weighty=1.0;
		c.fill=GridBagConstraints.BOTH;
		add(search,BorderLayout.NORTH);
		c.gridy=1;
		c.gridheight=2;
		c.weightx=.7;
		add(panel,BorderLayout.CENTER);
		setMinimumSize(new Dimension(550,625));
		setVisible(true);
	}
}