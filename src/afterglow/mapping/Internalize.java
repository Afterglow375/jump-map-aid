package afterglow.mapping;

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Internalize {
	private static JFrame frame;
	private static String pathText, vmfText;
	private static Path dir;
	private static String seperator;
	public static JTextArea log;
	public static ArrayList<String> lines, brush; // ArrayList of entire vmf
	public static ArrayList<Integer> groupIds; // Keeping track of the group ids to prevent conflicing id values
	
	// Creates the log for the output button
	public static void createLog() {
		seperator = "===========================================================\n";
		log = new JTextArea();
		log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
		frame = new JFrame();
		frame.setTitle("Output Log");
		frame.setBounds(100, 100, 700, 550);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(logScrollPane);
	}
	
	// Reads through entire vmf, writes each line to an arraylist, keeps track of where non-hidden brushes are
	public static void parse() { 
		pathText = GeneralTab.getPathText();
		vmfText = GeneralTab.getVmfText();
		dir = Paths.get(GeneralTab.getPath());
		if (Files.notExists(Paths.get(pathText))) { // Check to make sure vmf is valid
			MainWindow.popupBox("Invalid filepath.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else if (Files.notExists(dir)) {
			MainWindow.popupBox("There is no " + vmfText + ".vmf at the given filepath.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			log.setText("");
			frame.setVisible(true);
			log.append("Reading from " + dir.toString() + '\n');
			log.append(seperator);
			lines = new ArrayList<String>();
			brush = new ArrayList<String>(25);
			groupIds = new ArrayList<Integer>();
			String[] splitLine;
			int solidIndicator = 0;
			int entityIndicator = 0;
			int pointEntityIndicator = 1;
			int groupIndicator = 0;
			int id = 0;
			String idSubstring;
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(dir.toString()));
				String line = br.readLine();

				while(line != null) {
					if (line.equals("entity")) {
						groupIndicator = 0;
						entityIndicator = 1;
					}
					else if (line.equals("\tgroup") && entityIndicator == 0) {
						groupIndicator = 1;
					}
					else if (line.equals("\tsolid") && entityIndicator == 0) {
						solidIndicator = 1;
					}
					else if (line.contains("\"id\"")) { // Giving a unique id
						if (groupIndicator == 1) {
							splitLine = line.split(" ");
							idSubstring = splitLine[1].substring(1, splitLine[1].length()-1);
							groupIds.add(Integer.parseInt(idSubstring));
						}
						else {
							id = generateUniqueId(id);
							splitLine = line.split(" ");
							line = splitLine[0] + " \"" + Integer.toString(id) + '\"';
						}
					}
					
					if (solidIndicator == 1) {
						if (line.equals("}")) { // End of this brush
							solidIndicator = 0;
							lines.addAll(brush);
							lines.add(line);
							brush.clear();
						}
						else {
							brush.add(line);
						}
					}
					else if (entityIndicator == 1) {
//						if (line.equals("\tsolid")) { // Brush entity to keep track of
//							pointEntityIndicator = 0;
//						}
//						else if (line.equals("\teditor") && pointEntityIndicator == 1) { // Point entity, so stop keeping track of
//							
//						}
						if (line.equals("}")) { // End of this entity
							entityIndicator = 0;
							lines.addAll(brush);
							lines.add(line);
							brush.clear();
						}
						else {
							brush.add(line);
						}
					}
					else {
						lines.add(line);
					}
				    line = br.readLine();
				}
//				for (String linea : lines) {
//					log.append(linea + '\n');
//				}
				br.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Output button
//	private static void output() {
//		
//		for (int entityIndex : Internalize.ids) {
//			Internalize.log.append(Integer.toString(entityIndex) + '\n');
//		}
//		Internalize.log.append(Integer.toString(Internalize.generateUniqueId(7)) + '\n');
//		for (int entityIndex : Internalize.ids) {
//			Internalize.log.append(Integer.toString(entityIndex) + '\n');
//		}
//	}
	
	// Generates unique id, since all ids in a vmf must be unique
	public static int generateUniqueId(int id) {
		id++;
		while (groupIds.contains(id)) {
			id++;
		}
		return id;
	}
}
