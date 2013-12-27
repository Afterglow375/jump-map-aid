package afterglow.mapping;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class GeneralTab extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField pathText, vmfText;
	private JFileChooser fc = new JFileChooser();
	private JButton pathBrowse;
	//private static Path dir;
	
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
		
		vmfText = new JTextField();
		vmfText.setText("jump_");
		vmfText.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		vmfText.setColumns(10);
		
		JLabel vmfLabel = new JLabel("Map:");
		vmfLabel.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel pathDesc = new JLabel("Enter the filepath of where your vmfs are.");
		pathDesc.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel vmfDesc = new JLabel("Enter the name of your jump map.");
		vmfDesc.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		
		JLabel vmfLabel2 = new JLabel(".vmf");
		vmfLabel2.setVerticalAlignment(SwingConstants.BOTTOM);
		vmfLabel2.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		GroupLayout gl_General = new GroupLayout(this);
		gl_General.setHorizontalGroup(
			gl_General.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_General.createSequentialGroup()
					.addGroup(gl_General.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_General.createSequentialGroup()
							.addContainerGap()
							.addComponent(pathBrowse))
						.addGroup(gl_General.createSequentialGroup()
							.addGap(68)
							.addGroup(gl_General.createParallelGroup(Alignment.LEADING)
								.addComponent(pathDesc, GroupLayout.PREFERRED_SIZE, 330, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_General.createSequentialGroup()
									.addComponent(pathLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(pathText, GroupLayout.PREFERRED_SIZE, 468, GroupLayout.PREFERRED_SIZE))
								.addComponent(vmfDesc, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_General.createSequentialGroup()
									.addComponent(vmfLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(vmfText, GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(vmfLabel2, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addGap(38))
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
						.addComponent(vmfLabel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(vmfLabel2, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(100, Short.MAX_VALUE))
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
		
//		else if (e.getSource() == vmfBrowse) {
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
}
