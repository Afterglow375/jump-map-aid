package afterglow.mapping;

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Internalize {
	private static JFrame frame;
	private static JTextArea log;
	private static String pathText, vmfText;
	private static Path dir;
	private static String seperator;
	public static ArrayList<String> lines; // ArrayList of entire vmf
	public static ArrayList<Integer> worldBrushes; // Indices where non-hidden world brushes are in the vmf
	public static ArrayList<Integer> entities; // Indices where non-hidden brush entities are in the vmf
	public static ArrayList<Integer> ids; // All the ids in the vmf, needed to generate unique ids
	
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
		if (Files.notExists(Paths.get(pathText))) {
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
			worldBrushes = new ArrayList<Integer>();
			entities = new ArrayList<Integer>();
			ids = new ArrayList<Integer>();
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(dir.toString()));
				String line = br.readLine();
				int entityIndicator = 0;
				while(line != null) {
					if (line.equals("entity")) {
						entities.add(lines.size());
						entityIndicator = 1;
					}
					else if (line.contains("\"id\"")) {
						log.append(line + '\n');
					}
					else if (entityIndicator == 1) {
						if (line.equals("\tsolid")) { // Brush entity to keep track of
							entityIndicator = 0;
						}
						else if (line.equals("\teditor")) { // Point entity so remove from list
							entityIndicator = 0;
							entities.remove(entities.size()-1);
						}
					}
				    lines.add(line);
				    line = br.readLine();
				}
				for (int entityIndex : entities) {
					log.append(Integer.toString(entityIndex) + '\n');
				}
				br.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
