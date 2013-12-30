package afterglow.mapping;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GeneralTab extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JFileChooser fc;
	private JButton pathBrowse;
	private static JTextField pathText, vmfText;
	
	// Setting up the General tab GUI...
	public GeneralTab() {
		JLabel pathLabel = new JLabel("Path:");
		pathLabel.setBounds(30, 117, 41, 21);
		pathLabel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		pathText = new JTextField();
		pathText.setBounds(76, 114, 560, 27);
		pathText.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		pathText.setColumns(10);
		
		pathBrowse = new JButton("Browse");
		pathBrowse.setBounds(545, 152, 91, 27);
		pathBrowse.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		pathBrowse.addActionListener(this);
		
		vmfText = new JTextField();
		vmfText.setBounds(76, 198, 521, 27);
		vmfText.setText("jump_");
		vmfText.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		vmfText.setColumns(10);
		
		JLabel vmfLabel = new JLabel("Map:");
		vmfLabel.setBounds(31, 201, 41, 21);
		vmfLabel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel pathDesc = new JLabel("Enter the filepath of where your vmfs are.");
		pathDesc.setBounds(30, 85, 330, 21);
		pathDesc.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel vmfDesc = new JLabel("Enter the name of your map.");
		vmfDesc.setBounds(30, 166, 278, 21);
		vmfDesc.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 60, 650, 15);
		
		JLabel desc = new JLabel("Specify the general settings.");
		desc.setBounds(30, 21, 218, 21);
		desc.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel label = new JLabel(".vmf");
		label.setBounds(602, 201, 34, 21);
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		label.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		setLayout(null);
		add(pathDesc);
		add(vmfDesc);
		add(pathLabel);
		add(pathText);
		add(vmfLabel);
		add(vmfText);
		add(label);
		add(pathBrowse);
		add(desc);
		add(separator);
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pathBrowse) {
			int returnVal = fc.showOpenDialog(this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		        File file = fc.getSelectedFile();
		        pathText.setText(file.getAbsolutePath());
		    }
		}
	}
		
	// Save settings
	public static void save(PrintWriter out) { 
		out.println(pathText.getText());
		out.println(vmfText.getText());
	}
	
	// Load settings
	public static void load(BufferedReader br) {
		try {
			pathText.setText(br.readLine());
			vmfText.setText(br.readLine());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getPath() {
		return pathText.getText() + "\\" + vmfText.getText() + ".vmf";
	}
	
	public static String getPathText() {
		return pathText.getText();
	}
	
	public static String getVmfText() {
		return vmfText.getText();
	}
	

			
///		else if (e.getSource() == vmfBrowse) {
//			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
//			dir = Paths.get(pathText.getText());
//			if (pathText.getText().equals(""))
//				MainWindow.popupBox("You must first identify the filepath where your vmfs are saved.", "Error", JOptionPane.ERROR_MESSAGE);
//			else if (Files.notExists(dir)) {
//				MainWindow.popupBox("Invalid filepath.", "Error", JOptionPane.ERROR_MESSAGE);
//			}
//			else {
//				fc.setCurrentDirectory(dir.toFile());
//				fc.setFileView(notTraversable);
//			    int returnVal = fc.showOpenDialog(this);
//			    if (returnVal == JFileChooser.APPROVE_OPTION) {
//			        File file = fc.getSelectedFile();
//			        vmfText.setText(file.getPath());
//			    }
//			}
//		}
}
