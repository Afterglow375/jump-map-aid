import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class MainWindow implements ActionListener {
	private JFrame frmJumpMapAid;
	private JButton output, merge;
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
		
		output = new JButton("Output");
		output.setToolTipText("Saves to <input>_output.vmf");
		output.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		output.setBounds(465, 480, 91, 27);
		output.addActionListener(this);
		frmJumpMapAid.getContentPane().add(output);
		
	    merge = new JButton("Merge");
		merge.setToolTipText("Merges changes into original vmf");
		merge.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		merge.setBounds(580, 480, 91, 27);
		merge.addActionListener(this);
		frmJumpMapAid.getContentPane().add(merge);
		frmJumpMapAid.setTitle("Jump Map Aid 0.90");
		frmJumpMapAid.setBounds(100, 100, 700, 550);
		frmJumpMapAid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == output) {
			//String file = GeneralTab.pathText.paramString() + GeneralTab.vmfText.paramString();
		}
		
		else if (e.getSource() == merge) {
		}
	}
	
	// Popup dialog box
	public static void popupBox(String infoMessage, String location, int option) {
        JOptionPane.showMessageDialog(null, infoMessage, location, option);
    }
}
