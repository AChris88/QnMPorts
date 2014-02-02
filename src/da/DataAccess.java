package da;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Data access class containing methods
 * to download, parse, and display data.
 * 
 * @author Chris Allard
 * 
 */
public class DataAccess {
	private PortTableModel model;
	private File xml = null;
	private ArrayList<String[]> data = new ArrayList<String[]>();
	private Logger logger = Logger.getLogger(getClass().getName());

	/**
	 * hurp
	 */
	public DataAccess() {
		
	}
	
	/**
	 * Instantiation method.
	 * @param model to populate with data.
	 */
	public DataAccess(PortTableModel model) {
		this.model = model;
		obtainSource();
	}

	/**
	 * Determines existence of data file.
	 * If a parsed file exists, the model is populated.
	 * If the source file exists, it populates the model
	 * and saves a parsed version to reduce file size.
	 * If neither parsed or source file exists, the complete
	 * file is obtained from the IANA website and processed.
	 */
	private void obtainSource() {
		xml = new File("ports.xml");
		if (xml.exists())
			populateTable();
		else {
			xml = new File("service-names-port-numbers.xml");
			if (!xml.exists()) 
				downloadNewPorts();
			populateTable();
			saveXMLFile(data);
		}
	}

	/**
	 * Unfinished "update" functionality.
	 */
	public void updateSource() {
		if (outdated()){
			downloadNewPorts();
			populateTable();
			saveXMLFile(data);
			JOptionPane.showMessageDialog(null, "Successfully updated source file.");
		} else
			JOptionPane.showMessageDialog(null, "Source file already up to date.");
	}

	/**
	 * Indicates if source file is out dated.
	 * @return whether the source file is out dated.
	 */
	private boolean outdated() {
		return sourceLastUpdated().compareTo(siteLastUpdated()) < 0 ? true : false;
	}

	/**
	 * Identifies when source file was last written to.
	 * @return date at which source file was last written to.
	 */
	private String sourceLastUpdated() {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String updated = "";
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder()
					.parse("service-names-port-numbers.xml");
			updated = xpath.evaluate("/registry/updated", doc);
		} catch (XPathExpressionException e) {
			logger.log(Level.SEVERE, "Invalid XPath expression!", e);
		} catch (SAXException e) {
			logger.log(Level.SEVERE,
					"Error parsing service-names-port-numbers.xml", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Error processing service-names-port-numbers.xml", e);
		} catch (ParserConfigurationException e) {
			logger.log(
					Level.SEVERE,
					"Error creating DocumentBuilder which satisfies the configuration requested.",
					e);
		} finally {
		}
		return updated;
	}

	/**
	 * Identifies when IANA's port list was last updated.
	 * @return date at which IANa's port file was last updated.
	 */
	private String siteLastUpdated() {
		String url = "http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xml";
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection con;
		long date = 0;
		try {
			con = (HttpURLConnection) new URL(url).openConnection();
			date = con.getLastModified();
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "Invalid URL to update from!", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Unable to obtain 'last-updated' info from URL!", e);
		}
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

		return fmt.format(new Date(date));
	}

	/**
	 * Populates data in table model from xml file determined in obtainSource()
	 */
	private void populateTable() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xml);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("record");
			String[] record = null;

			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) nNode;
					record = new String[4];
					record[0] = getTagValue("number", element);
					record[1] = getTagValue("protocol", element);
					record[2] = getTagValue("name", element);
					record[3] = getTagValue("description", element);

					data.add(record);
				}
			}
			model.loadData(data);
			model.loadColumnNames();
		} catch (ParserConfigurationException e) {
			logger.log(
					Level.SEVERE,
					"Error creating DocumentBuilder which satisfies the configuration requested.",
					e);
		} catch (SAXException e) {
			logger.log(Level.SEVERE,
					"Error parsing service-names-port-numbers.xml", e);
		} catch (IOException e) {
			logger.log(Level.SEVERE,
					"Error processing service-names-port-numbers.xml", e);
		}
	}

	/**
	 * Converts a collection of data into xml form.
	 * @param data to be converted into xml String.
	 * @return data in xml form.
	 */
	private String createXMLString(ArrayList<String[]> data) {
		Date today = new Date();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		String updated = fmt.format(today);

		String xml = "<?xml version='1.0' encoding='UTF-8'?>\n"
				+ "<registry xmlns='www.iana.org/assignments/port-numbers' id='ports'>\n\t"
				+ "<title>Parsed Ports</title>\n\t"
				+ "<updated>"
				+ updated
				+ "</updated>\n\t"
				+ "<parsed>"
				+ new Date()
				+ "</parsed>\n\t"
				+ "<note>Data obtained from www.iana.org/assignments/port-numbers</note>\n\t";
		StringBuilder builder = new StringBuilder(xml);
		for (int i = 0; i < data.size(); i++) {
			// Checking for ampersands and replacing
			if (data.get(i)[3] != null && data.get(i)[3].contains("&"))
				data.get(i)[3] = data.get(i)[3].substring(0,
						data.get(i)[3].indexOf('&'))
						+ "&amp;"
						+ data.get(i)[3]
								.substring(data.get(i)[3].indexOf('&') + 1);

			if (data.get(i)[2] != null && data.get(i)[2].contains("&"))
				data.get(i)[2] = data.get(i)[2].substring(0,
						data.get(i)[2].indexOf('&'))
						+ "&amp;"
						+ data.get(i)[2]
								.substring(data.get(i)[2].indexOf('&') + 1);

			builder.append("<record>\n\t\t<number>" + data.get(i)[0]
					+ "</number>\n\t\t" + "<protocol>" + data.get(i)[1]
					+ "</protocol>\n\t\t" + "<name>" + data.get(i)[2]
					+ "</name>\n\t\t" + "<description>" + data.get(i)[3]
					+ "</description>\n\t</record>");
		}
		builder.append("</registry>");
		data = null;

		return builder.toString();
	}

	/**
	 * Saves an xml String to a file.
	 * @param data to be converted and saved as xml.
	 */
	private void saveXMLFile(ArrayList<String[]> data) {
		String xml = createXMLString(data);
		File xmlfile = new File("ports.xml");
		
		// OutputStreamWriter to enable forcing UTF-8 encoding.
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(xmlfile),
					Charset.forName("UTF-8").newEncoder());
			writer.write(xml);
			writer.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error writing to ports.xml", e);
		}
	}

	/**
	 * Extracts value from element. 
	 * @param tag identifying value in element.
	 * @param element to extract value from.
	 * @return value in element referenced by tag
	 */
	private static String getTagValue(String tag, Element element) {
		NodeList nlList = null;
		if (element.getElementsByTagName(tag).item(0) == null)
			return "";

		nlList = element.getElementsByTagName(tag).item(0).getChildNodes();

		Node nValue = null;
		if (nlList.getLength() > 0)
			nValue = (Node) nlList.item(0);

		return nValue == null ? "" : nValue.getNodeValue();
	}

	/**
	 * Downloads IANA's port list.
	 */
	private void downloadNewPorts() {
		FileOutputStream fos = null;
		try {
			URL website = new URL(
					"http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xml");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			fos = new FileOutputStream("service-names-port-numbers.xml");
			
			// http://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
			fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		} catch (IOException e) {
			logger.log(
					Level.SEVERE,
					"Error obtaining data from www.iana.org/.../service-names-port-numbers.xml",
					e);
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE,
							"Unable to close FileOutputStream!", e);
				}
		}

	}

	/**
	 * Narrows search results to within an inclusive range.
	 * @param start of range.
	 * @param end of range.
	 */
	public void getByPortRange(String start, String end) {
		ArrayList<String[]> tmpData = new ArrayList<String[]>();
		Vector<String[]> data = model.getData();
		
		if (!start.trim().equals("") && !end.trim().equals("")) {
			int num1 = Integer.parseInt(start);
			int num2 = Integer.parseInt(end);
			int tmpNum1 = 0;
			int tmpNum2 = 0;
			String num;

			for (int i = 0; i < data.size(); i++) {
				num = data.get(i)[0];
				if (!num.contains("-")) {
					if(!num.equals(""))
						if (Integer.parseInt(num) >= num1 && Integer.parseInt(num) <= num2){
							tmpData.add(data.get(i));}
				} else {
					tmpNum1 = Integer.parseInt(num.substring(0, num.indexOf('-')));
					tmpNum2 = Integer.parseInt(num.substring(num.indexOf('-')));
					if (tmpNum1 >= num1 && tmpNum2 <= num2)
						if (tmpNum1 <= num2)
							tmpData.add(data.get(i));
				}
			}
			model.loadData(tmpData);
		} else
			JOptionPane.showMessageDialog(null,
					"You must enter a start and end point for range searches.");
	}

	/**
	 * Searches through port data.
	 * @param value to search for.
	 * @param type of value to search for.
	 */
	public void getPorts(String value, String type) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		Vector<String[]> target = model.getCachedData();
		int index = 0;
		
		//no port case b/c default of index is 0.
		switch (type) {
		case "protocol":
			index = 1;
			break;
		case "name":
			index = 2;
			break;
		case "description":
			index = 3;
			break;
		}
		
		for (int i = 0; i < target.size(); i++)
			if (index < 3 ? target.get(i)[index].equals(value) : target.get(i)[index].toLowerCase().contains(value))
				data.add(target.get(i));
		
		model.loadData(data);
	}

	/**
	 * Populates table with every data record.
	 */
	public void showAllPorts() {
		model.showAllData();
	}
}