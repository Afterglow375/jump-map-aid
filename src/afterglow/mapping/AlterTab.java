package afterglow.mapping;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AlterTab extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JTextField playerClipText, triggerTeleportHeight, triggerTeleportText,
	noGrenadesText, noGrenadesHeight, brushText;
	
	// Setting up the Alter tab GUI...
	public AlterTab() {
		JLabel label1 = new JLabel("Player clip and func_illusionary:");
		label1.setBounds(30, 88, 238, 21);
		label1.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		
		playerClipText = new JTextField();
		playerClipText.setBounds(273, 85, 363, 27);
		playerClipText.setText("metal/metalfence007a");
		playerClipText.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		playerClipText.setColumns(10);
		
		triggerTeleportHeight = new JTextField();
		triggerTeleportHeight.setBounds(91, 222, 51, 27);
		triggerTeleportHeight.setHorizontalAlignment(SwingConstants.LEFT);
		triggerTeleportHeight.setText("1");
		triggerTeleportHeight.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		triggerTeleportHeight.setColumns(10);
		
		JLabel label2 = new JLabel("Height:");
		label2.setBounds(30, 225, 56, 21);
		label2.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		
		JLabel desc = new JLabel("Specify the textures for which brushes you want to alter.");
		desc.setBounds(30, 21, 433, 21);
		desc.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 60, 650, 15);
		
		JLabel label = new JLabel("trigger_teleport:");
		label.setBounds(30, 192, 127, 21);
		label.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		
		triggerTeleportText = new JTextField();
		triggerTeleportText.setBounds(162, 189, 474, 27);
		triggerTeleportText.setText("dev/reflectivity_50");
		triggerTeleportText.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		triggerTeleportText.setColumns(10);
		
		JLabel lblFuncnogrenades = new JLabel("func_nogrenades:");
		lblFuncnogrenades.setBounds(30, 277, 141, 21);
		lblFuncnogrenades.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		
		noGrenadesText = new JTextField();
		noGrenadesText.setBounds(176, 274, 460, 27);
		noGrenadesText.setText("dev/dev_blendmeasure");
		noGrenadesText.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		noGrenadesText.setColumns(10);
		
		noGrenadesHeight = new JTextField();
		noGrenadesHeight.setBounds(91, 307, 51, 27);
		noGrenadesHeight.setText("1");
		noGrenadesHeight.setHorizontalAlignment(SwingConstants.LEFT);
		noGrenadesHeight.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		noGrenadesHeight.setColumns(10);
		
		JLabel label_2 = new JLabel("Height:");
		label_2.setBounds(30, 310, 56, 21);
		label_2.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		setLayout(null);
		add(separator);
		add(label2);
		add(triggerTeleportHeight);
		add(label_2);
		add(noGrenadesHeight);
		add(label1);
		add(playerClipText);
		add(label);
		add(triggerTeleportText);
		add(lblFuncnogrenades);
		add(noGrenadesText);
		add(desc);
		
		JLabel lblFuncbrush = new JLabel("func_brush:");
		lblFuncbrush.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		lblFuncbrush.setBounds(30, 140, 89, 21);
		add(lblFuncbrush);
		
		brushText = new JTextField();
		brushText.setText("glass/combineglass001a");
		brushText.setFont(new Font(MainWindow.FONT, Font.PLAIN, 16));
		brushText.setColumns(10);
		brushText.setBounds(124, 137, 512, 27);
		add(brushText);
	}
	
	// Save settings
	public static void save(PrintWriter out) { 
		out.println(playerClipText.getText());
		out.println(brushText.getText());
		out.println(triggerTeleportText.getText());
		out.println(triggerTeleportHeight.getText());
		out.println(noGrenadesText.getText());
		out.println(noGrenadesHeight.getText());
	}
	
	// Load settings
	public static void load(BufferedReader br) {
		try {
			playerClipText.setText(br.readLine());
			brushText.setText(br.readLine());
			triggerTeleportText.setText(br.readLine());
			triggerTeleportHeight.setText(br.readLine());
			noGrenadesText.setText(br.readLine());
			noGrenadesHeight.setText(br.readLine());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> changeBrush(ArrayList<String> brush, ArrayList<Integer> indices) {
		VMFObjectCreator creator = new VMFObjectCreator(brush, indices);
		String[] splitLine;
		String texture;
		int indicator = 0; // Want playerClip/funcBrush creators to be called only once per brush
		
		for (int i = 0; i < indices.size(); i++) {
			splitLine = brush.get(indices.get(i) + 3).split(" ");
			texture = splitLine[1].substring(1, splitLine[1].length()-1);
			if (indicator == 0 && texture.equals(playerClipText.getText().toUpperCase())) { 
				creator.createPlayerClip(); // Turn this into a playerclip block and func_illusionary, grouped
				indicator = 1;
			}
			else if (indicator == 0 && texture.equals(brushText.getText().toUpperCase())) { 
				creator.createFuncBrush(); // Turn this into a func_brush
				indicator = 1;
			}
			
			// Turn all sides that have a given texture to have teleports above them
			if (brush.get(indices.get(i) + 3).contains(triggerTeleportText.getText().toUpperCase())) {
			}
			// Turn all sides that have a given texture to have func_nogrenades above them
			if (brush.get(indices.get(i) + 3).contains(noGrenadesText.getText().toUpperCase())) {
			}
		}
		return brush;
	}
	
	// Getters
	public static String getPlayerClipText() {
		return playerClipText.getText();
	}
	
	public static String getBrushText() {
		return brushText.getText();
	}
	
	public static String getTriggerTeleportText() {
		return triggerTeleportText.getText();
	}
	
	public static String getTriggerTeleportHeight() {
		return triggerTeleportHeight.getText();
	}
	
	public static String getNoGrenadesText() {
		return noGrenadesText.getText();
	}
	
	public static String getNoGrenadesHeight() {
		return noGrenadesHeight.getText();
	}
}
