import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class GeneralTab extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField pathText, vmfText;
	private final JFileChooser fc = new JFileChooser();
	private JButton pathBrowse, vmfBrowse;
	
	public GeneralTab() {
		JLabel pathLabel = new JLabel("Path:");
		pathLabel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		pathText = new JTextField();
		pathText.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		pathText.setColumns(10);
		
		// Browse button for directory of vmfs
		pathBrowse = new JButton("Browse...");
		pathBrowse.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		pathBrowse.addActionListener(this);
		
		// Browse button for locating the specific vmf
		vmfBrowse = new JButton("Browse...");
		vmfBrowse.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		vmfBrowse.addActionListener(this);
//		vmfBrowse.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			    int returnVal = fc.showOpenDialog(FileChooserDemo.this);
//			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
//			            "vmf");
//			        fc.setFileFilter(filter);
//			    if (returnVal == JFileChooser.APPROVE_OPTION) {
//			        File file = fc.getSelectedFile();
//			        //This is where a real application would open the file.
//			        log.append("Opening: " + file.getName() + "." + newline);
//			    }
//			}
//		});
		
		
		vmfText = new JTextField();
		vmfText.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		vmfText.setColumns(10);
		
		JLabel vmfLabel = new JLabel("VMF:");
		vmfLabel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel pathDesc = new JLabel("Enter the filepath of where your vmfs are.");
		pathDesc.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel vmfDesc = new JLabel("Enter the name of your jump map.");
		vmfDesc.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GroupLayout gl_General = new GroupLayout(this);
		gl_General.setHorizontalGroup(
			gl_General.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_General.createSequentialGroup()
					.addGroup(gl_General.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_General.createSequentialGroup()
							.addContainerGap()
							.addComponent(pathBrowse))
						.addGroup(gl_General.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_General.createSequentialGroup()
								.addGap(68)
								.addGroup(gl_General.createParallelGroup(Alignment.LEADING)
									.addComponent(pathDesc, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
									.addGroup(gl_General.createSequentialGroup()
										.addComponent(pathLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(pathText, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE))))
							.addGroup(gl_General.createSequentialGroup()
								.addGap(68)
								.addGroup(gl_General.createParallelGroup(Alignment.LEADING)
									.addComponent(vmfDesc, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
									.addGroup(gl_General.createParallelGroup(Alignment.TRAILING)
										.addComponent(vmfBrowse)
										.addGroup(gl_General.createSequentialGroup()
											.addComponent(vmfLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(vmfText, GroupLayout.PREFERRED_SIZE, 469, GroupLayout.PREFERRED_SIZE)))))))
					.addGap(85))
		);
		gl_General.setVerticalGroup(
			gl_General.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_General.createSequentialGroup()
					.addGap(73)
					.addComponent(pathDesc, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_General.createParallelGroup(Alignment.BASELINE)
						.addComponent(pathText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(pathLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(pathBrowse, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(40)
					.addComponent(vmfDesc, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_General.createParallelGroup(Alignment.BASELINE)
						.addComponent(vmfText, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(vmfLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(vmfBrowse, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(118, Short.MAX_VALUE))
		);
		this.setLayout(gl_General);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pathBrowse) {
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int returnVal = fc.showOpenDialog(this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		        File file = fc.getSelectedFile();
		        pathText.setText(file.getAbsolutePath());
		    }
		}
		
		else if (e.getSource() == vmfBrowse) {
			//FileNameExtensionFilter filter = new FileNameExtensionFilter("vmf");
		    //fc.setFileFilter(filter);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			Path dir = Paths.get(pathText.getText());
			if (pathText.getText().equals(""))
				MainWindow.popupBox("You must first identify the filepath where your vmfs are saved.", "Error", JOptionPane.ERROR_MESSAGE);
			else if (Files.notExists(dir)) {
				MainWindow.popupBox("Invalid filepath.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				fc.setCurrentDirectory(dir.toFile());
			    int returnVal = fc.showOpenDialog(this);
			    if (returnVal == JFileChooser.APPROVE_OPTION) {
			        File file = fc.getSelectedFile();
			        vmfText.setText(file.getAbsolutePath());
			    }
			}
		}
	}
}
