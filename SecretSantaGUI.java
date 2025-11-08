import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SecretSantaGUI extends JFrame {
	private DefaultListModel<Participant> participantModel;
	private JTextArea resultArea;
	
	public SecretSantaGUI() {
		setTitle("Secret Santa Organizer");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(1,2));
		
		
		//left side panel
		participantModel = new DefaultListModel<>();
		JList<Participant> participantList = new JList<>(participantModel);
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(new JScrollPane(participantList), BorderLayout.CENTER);
		
		
		JPanel addPanel = new JPanel(new GridLayout(3,2));
		JTextField nameField = new JTextField();
		JTextField emailField = new JTextField();
		JButton addButton = new JButton("Add Participant");
		
		addButton.addActionListener(e -> {
			String name = nameField.getText().trim();
			String email = emailField.getText().trim();
			
			if (!name.isEmpty() && !email.isEmpty()) {
				participantModel.addElement(new Participant(name, email));
				nameField.setText("");
				emailField.setText("");
			} else {
				JOptionPane.showMessageDialog(this, "both fields are needed!");
			}
			
		});
		 
		addPanel.add(new JLabel("Name"));
		addPanel.add(nameField);
		
		addPanel.add(new JLabel("Email"));
		addPanel.add(emailField);
		
		addPanel.add(addButton);
		
		leftPanel.add(addPanel, BorderLayout.SOUTH);
		
		//right side panel 
		resultArea = new JTextArea();
		resultArea.setEditable(false);
		
		
		JButton assignButton = new JButton("Assign the Secret Santa's");
        assignButton.addActionListener(e -> assignAndDisplay());
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        rightPanel.add(assignButton, BorderLayout.SOUTH);
        
        
        add(leftPanel);
        add(rightPanel);
	}
	
	private void assignAndDisplay() {
		List<Participant> participants = Collections.list(participantModel.elements());
		if (participants.size() < 2) {
			JOptionPane.showMessageDialog(this, "Add atleast 2 participants!");
			return;
		}
		
		List<Participant> santas = new ArrayList<>(participants);
		Collections.shuffle(santas);
		
		//make sure no ones gets them self
		resultArea.setText("Secret Santa Assignment: \n");
		for (int i = 0; i < participants.size(); i++) {
			resultArea.append(participants.get(i).getName() + "->" + santas.get(i).getName() + "\n");
			
		}
		
		int sendOption = JOptionPane.showConfirmDialog(this,"Assignments are complete! Should I send the emails now?", "Send Emails", JOptionPane.YES_NO_OPTION);
		
		if (sendOption == JOptionPane.YES_OPTION) {
			new EmailSender(participants, santas).setVisible(true);
		}
		
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SecretSantaGUI().setVisible(true));
	}

}
