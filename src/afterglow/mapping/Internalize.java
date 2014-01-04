package afterglow.mapping;

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Internalize {
	private static final String SEPARATOR = "==============================================================================================";
	private static JFrame frame;
	private static String pathText, vmfText;
	private static Path dir;
	private static ArrayList<String> lines, brush; // ArrayList of entire vmf
	private static ArrayList<Integer> groupIds, sideIndices; // Keeping track of the group ids to prevent conflicing id values
	private static int id = 0;
	public static JTextArea log;
	
	// Creates the log for the output button
	public static void createLog() {
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
	
	// Reads through entire vmf, carrying out actions based on user settings
	public static void ensureProperInput() { 
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
			parseAndOutput();
		}
	}
	
	public static void parseAndOutput() {
		log.setText("");
		frame.setVisible(true);
		log.append("Reading from " + dir.toString() + '\n');
		log.append(SEPARATOR + '\n');
		lines = new ArrayList<String>();
		brush = new ArrayList<String>(25);
		groupIds = new ArrayList<Integer>();
		sideIndices = new ArrayList<Integer>();
		String[] splitLine;
		int solidIndicator = 0;
		int entityIndicator = 0;
		int pointEntityIndicator = 1;
		int groupIndicator = 0;
		int alterTabIndicator = 0;
		int displacementIndicator = 0;
		String idSubstring, texture;
		String success = "";
		
		// Gets kinda messy here, but I valued runtime efficiency over clean code
		try {
			BufferedReader br = new BufferedReader(new FileReader(dir.toString()));
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pathText + "\\" + vmfText + "_output.vmf")));
			String line = br.readLine();
			
			// Handling the contents of the vmf...
			while (line != null) { 
				if (line.contains("\"id\"")) { // Giving a unique id
					if (groupIndicator == 1) { // Don't change the id if it's a group
						splitLine = line.split(" ");
						idSubstring = splitLine[1].substring(1, splitLine[1].length()-1);
						groupIds.add(Integer.parseInt(idSubstring));
					}
					else {
						id = generateUniqueId();
						splitLine = line.split(" ");
						line = splitLine[0] + " \"" + Integer.toString(id) + '\"';
					}
				}
				else if (line.contains("material")) {
					splitLine = line.split(" ");
					texture = splitLine[1].substring(1, splitLine[1].length()-1);
					if (AlterTab.getPlayerClipText().toUpperCase().equals(texture) || 
					AlterTab.getBrushText().toUpperCase().equals(texture) ||
					AlterTab.getTriggerTeleportText().toUpperCase().equals(texture) ||
					AlterTab.getNoGrenadesText().toUpperCase().equals(texture)) {
						alterTabIndicator = 1;
					}
				}
				else if (line.equals("\tsolid") && entityIndicator == 0) {
					solidIndicator = 1;
					displacementIndicator = 0; // Reset displacement and alter tab indicators
					alterTabIndicator = 0; 
				}
				else if (line.equals("entity")) {
					groupIndicator = 0;
					entityIndicator = 1;
				}
				else if (line.equals("\tgroup") && entityIndicator == 0) {
					groupIndicator = 1;
					addGeneratedSolidsAndGroups(out);
				}
				else if (line.equals("cameras")) {
					groupIndicator = 0;
					addGeneratedSolidsAndGroups(out);
					addGeneratedEntities(out);
				}
				
				if (solidIndicator == 1) {
					if (line.contains("side")) {
						brush.add(line);
						sideIndices.add(brush.size());
					}
					else if (line.contains("dispinfo")) {
						displacementIndicator = 1;
					}
					else if (line.equals("\t}")) { // End of this brush
						brush.add(line);
						if (alterTabIndicator == 1 && displacementIndicator == 0) { // Only change world brushes that aren't displacements
							AlterTab.changeBrush(brush, sideIndices);		// and have a texture that matches the user settings in the Alter tab
						}
						else {
							printArrayList(out, brush);
							lines.addAll(brush);
						}
						solidIndicator = 0;
						brush.clear();
						sideIndices.clear();
					}
					else {
						brush.add(line);
					}
				}
				else if (entityIndicator == 1) {
					addGeneratedSolidsAndGroups(out);
					addGeneratedEntities(out);
//					if (line.equals("\tsolid")) { // Brush entity to keep track of
//						pointEntityIndicator = 0;
//					}
//					else if (line.equals("\teditor") && pointEntityIndicator == 1) { // Point entity, so stop keeping track of
//						
//					}
					if (line.equals("}")) { // End of this entity
						brush.add(line);
						entityIndicator = 0;
						lines.addAll(brush);
						printArrayList(out, brush);
						brush.clear();
					}
					else {
						brush.add(line);
					}
				}
				else { 
					out.println(line);
					lines.add(line);
				}
			    line = br.readLine();
			}
//			for (String linea : lines) {
//				log.append(linea + '\n');
//			}
			br.close();
			out.close();
		}
		catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			success = errors.toString();
		}
		
		appendOutputDetails(success);
	}
	
	public static void appendOutputDetails(String success) {
		if (success.equals("")) {
			log.append("\nALTER...\n");
			log.append(Integer.toString(VMFObjectCreator.getPlayerClipCounter()) + " player clip/func_illusionary brushes created.\n");
			log.append(Integer.toString(VMFObjectCreator.getFuncBrushCounter()) + " func_brushes created.\n");
			log.append(Integer.toString(VMFObjectCreator.getTriggerTeleportCounter()) + " trigger_teleports created.\n");
			log.append(Integer.toString(VMFObjectCreator.getNoGrenadesCounter()) + " func_nogrenades created.\n");
			
			log.append("\n===================================== OUTPUT SUCCESSFUL =====================================\n");
		}
		else {
			log.append("\n======================================= OUTPUT FAILED =======================================\n");
			log.append(success);
		}
	}
	
	public static void printArrayList(PrintWriter out, ArrayList<String> arrayToPrint) {
		for (String line : arrayToPrint) {
			out.println(line);
		}
	}
	
	// Adds in the created solids based upon the AlterTab
	public static void addGeneratedSolidsAndGroups(PrintWriter out) {
		if (VMFObjectCreator.outputBrushesAndGroups.size() != 0) {
			printArrayList(out, VMFObjectCreator.outputBrushesAndGroups);
			lines.addAll(VMFObjectCreator.outputBrushesAndGroups);
			VMFObjectCreator.outputBrushesAndGroups.clear();
		}
	}
	
	// Adds in the created groups/entities based upon the AlterTab
	public static void addGeneratedEntities(PrintWriter out) {
		if (VMFObjectCreator.outputEntities.size() != 0) {
			printArrayList(out, VMFObjectCreator.outputEntities);
			lines.addAll(VMFObjectCreator.outputEntities);
			VMFObjectCreator.outputEntities.clear();
		}
	}
	
	// Generates unique id, since all ids in a vmf must be unique
		public static int generateUniqueId() {
			id++;
			while (groupIds.contains(id)) {
				id++;
			}
			return id;
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
}
