import javax.swing.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.*;
import java.util.List;
import java.util.Properties;

public class EmailSender extends JFrame {
    private List<Participant> givers;
    private List<Participant> receivers;

    public EmailSender(List<Participant> givers, List<Participant> receivers) {
        this.givers = givers;
        this.receivers = receivers;

        setTitle("Send Secret Santa Emails");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2));

        // Fields for SMTP setup
        JTextField smtpHost = new JTextField("smtp.office365.com");
        JTextField smtpPort = new JTextField("587");
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton sendButton = new JButton("Send Emails");

        add(new JLabel("SMTP Host:"));
        add(smtpHost);
        add(new JLabel("Port:"));
        add(smtpPort);
        add(new JLabel("Sender Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(sendButton);

        sendButton.addActionListener(e -> {
            String host = smtpHost.getText();
            String port = smtpPort.getText();
            String senderEmail = emailField.getText();
            String password = String.valueOf(passwordField.getPassword());

            sendEmails(host, port, senderEmail, password);
        });
    }

    private void sendEmails(String host, String port, String senderEmail, String password) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            for (int i = 0; i < givers.size(); i++) {
                String recipientEmail = givers.get(i).getEmail();
                String senderName = givers.get(i).getName();
                String receiverName = receivers.get(i).getName();

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                message.setSubject("Your Secret Santa Assignment");
                message.setText(
                    "Hello " + senderName + ",\n\n" +
                    "You have been assigned to give a gift to: " + receiverName + ".\n\n" +
                    "Merry Christmas and Happy gifting!\n" +
                    "Secret Santa Organizer"
                );

                Transport.send(message);
            }

            JOptionPane.showMessageDialog(this, "Emails sent successfully!");
        } catch (MessagingException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send emails: " + ex.getMessage());
        }
    }
}
