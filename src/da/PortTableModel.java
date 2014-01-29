package da;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * Model to be used in displaying port data.
 * 
 * @author Chris Allard
 * 
 */
public class PortTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private Vector<String> columnNames = new Vector<String>();
	private Vector<String[]> data = new Vector<String[]>();
	private Vector<String[]> allPorts = new Vector<String[]>();

	/**
	 * Gets data from index.
	 * 
	 * @param index
	 *            to get data from.
	 * @return data from index.
	 */
	public String[] getDataAt(int index) {
		return allPorts.get(index);
	}

	/**
	 * Gets columns size.
	 */
	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	/**
	 * Gets row size.
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/**
	 * Gets a value at specific row-column intersection.
	 */
	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return data.elementAt(row)[0];
		case 1:
			return data.elementAt(row)[1];
		case 2:
			return data.elementAt(row)[2];
		case 3:
			return data.elementAt(row)[3];
		}
		throw new IllegalArgumentException(
				"Cannot retrieve from selected field");
	}

	/**
	 * Adds a record to the data collection.
	 * 
	 * @param data
	 *            to be added to the data collection.
	 */
	public void addData(String[] data) {
		this.data.add(data);
	}

	/**
	 * Loads column names to be displayed.
	 * 
	 * @return
	 */
	public void loadColumnNames() {
		columnNames.addElement("Number");
		columnNames.addElement("Protocol");
		columnNames.addElement("Name");
		columnNames.addElement("Description");
	}

	/**
	 * Loads data to display in table, and keeps a cached copy to query.
	 * 
	 * @param data
	 *            to be loaded.
	 */
	@SuppressWarnings("unchecked")
	public void loadData(ArrayList<String[]> data) {
		this.data = new Vector<String[]>();

		for (int i = 0; i < data.size(); i++) 
			this.data.add(data.get(i));

		if (allPorts.size() < this.data.size())
			allPorts = (Vector<String[]>) this.data.clone();

		fireTableDataChanged();
	}

	/**
	 * Gets the table column name.
	 */
	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	/**
	 * Gets the cached data.
	 * 
	 * @return cached data.
	 */
	public Vector<String[]> getCachedData() {
		return allPorts;
	}

	/**
	 * Pushes cached copy to table.
	 */
	@SuppressWarnings("unchecked")
	public void showAllData() {
		data = (Vector<String[]>) allPorts.clone();
		fireTableDataChanged();
	}

	/**
	 * Gets data.
	 * 
	 * @return data.
	 */
	public Vector<String[]> getData() {
		return data;
	}
}