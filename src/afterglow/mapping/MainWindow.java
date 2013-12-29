package afterglow.mapping;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import afterglow.mapping.Toast.Style;

public class MainWindow implements ActionListener {
	private JFrame frmJumpMapAid;
	private JButton output, merge, settings;
	public static String vmfDirectory, vmfName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmJumpMapAid.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJumpMapAid = new JFrame();
		frmJumpMapAid.getContentPane().setFont(new Font("Century Gothic", Font.PLAIN, 13));
		frmJumpMapAid.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		frmJumpMapAid.getContentPane().setForeground(new Color(0, 0, 0));
		frmJumpMapAid.getContentPane().setBackground(new Color(240, 248, 255));
		frmJumpMapAid.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 0, 672, 464);
		
		frmJumpMapAid.getContentPane().add(tabbedPane);
		tabbedPane.addTab("General", new GeneralTab());
		tabbedPane.addTab("Alter", new AlterTab());
		
		settings = new JButton("Save Settings");
		settings.setToolTipText("Saves the current settings");
		settings.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		settings.setBounds(10, 475, 214, 37);
		settings.addActionListener(this);
		frmJumpMapAid.getContentPane().add(settings);
		
		output = new JButton("Output");
		output.setToolTipText("Outputs a new vmf with the given settings to <input>_output.vmf");
		output.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		output.setBounds(239, 475, 214, 37);
		output.addActionListener(this);
		frmJumpMapAid.getContentPane().add(output);
		
	    merge = new JButton("Merge");
		merge.setToolTipText("Merges changes into original vmf");
		merge.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		merge.setBounds(468, 475, 214, 37);
		merge.addActionListener(this);
		frmJumpMapAid.getContentPane().add(merge);
		
		frmJumpMapAid.setTitle("Jump Map Aid 0.90");
		frmJumpMapAid.setBounds(100, 100, 700, 550);
		frmJumpMapAid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Internalize.createLog();
		
		// Populate fields based on settings.txt, if it exists
		File data = new File(System.getProperty("user.dir") + "\\settings.txt");
		if (data.isFile()) {
			load(data);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == settings) { // Write settings to memory
			save();
		}
		else if (e.getSource() == output) { // Copy input vmf, alter it based on user settings, output new vmf
			Internalize.parse();
		}
		else if (e.getSource() == merge) { // Overwrite input vmf with output
			
		}
	}
	
	// Create the log window
//	public JFrame createLog() { 
//		JFrame log = new JFrame();
//		return log;
//	}
	
	// Popup dialog box
	public static void popupBox(String infoMessage, String location, int option) {
        JOptionPane.showMessageDialog(null, infoMessage, location, option);
    }
	
	// Saving settings
	public void save() {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("settings.txt")));
	        out.println("WARNING: ALTERING THIS FILE IN ANY WAY MAY MAKE THE APP FAIL");
	        GeneralTab.save(out);
	        AlterTab.save(out);
	        out.close();
	        Toast.makeText(frmJumpMapAid, "Settings saved", Style.NORMAL).display();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// Loading saved settings into the app
	public void load(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			br.readLine();
			GeneralTab.load(br);
			AlterTab.load(br);
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();	
		}
	}
}
