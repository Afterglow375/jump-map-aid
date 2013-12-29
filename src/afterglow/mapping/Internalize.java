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
	
	// Creates the log for the output button
	public static void createLog() {
		log = new JTextArea();
		log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
		frame = new JFrame();
		frame.setTitle("Log");
		frame.setBounds(100, 100, 700, 550);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(logScrollPane);
	}
	
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
			try {
				BufferedReader br = new BufferedReader(new FileReader(GeneralTab.getPath()));
				ArrayList<String> lines = new ArrayList<String>(); 
				String line = br.readLine(); 
				while(line != null){
					log.append(line + '\n');
				    lines.add(line);
				    line = br.readLine();
				}
				frame.setVisible(true);
				br.close();
			}
			catch (IOException e) {
				e.printStackTrace();	
			}
		}
	}
}
