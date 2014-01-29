package ui;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table to contain port data.
 * @author Chris Allard
 *
 */
public class PortTable extends JTable{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor method.
	 */
	public PortTable(){
		super();
	}

	/**
	 * Constructor method.
	 * @param model to populate.
	 */
	public PortTable(AbstractTableModel model){
		this();
		this.setModel(model);
		initialize();
	}

	/**
	 * Initializes all essential components. 
	 */
	private void initialize() {
		setAutoCreateColumnsFromModel(true);
		setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setPreferredScrollableViewportSize(getPreferredSize());
		setFillsViewportHeight(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment( JLabel.CENTER );
		getColumnModel().getColumn(0).setCellRenderer( rightRenderer );
		getColumnModel().getColumn(1).setCellRenderer( rightRenderer );
		getColumnModel().getColumn(2).setCellRenderer( rightRenderer );
		getColumnModel().getColumn(3).setCellRenderer( rightRenderer );
		setAutoscrolls(true);
		setVisible(true);
	}
}