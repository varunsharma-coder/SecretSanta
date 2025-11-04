import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SecretSanta {
    private static final String SENDER_EMAIL = "your_email@example.com";
    private static final String SENDER_PASSWORD = "your_password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> participants = new ArrayList<>();
        Map<String, String> emailMap = new HashMap<>();

        System.out.println("Enter the names and email addresses of 8 participants:");
        for (int i = 0; i < 8; i++) {
            System.out.print("Participant " + (i + 1) + " name: ");
            String name = scanner.nextLine();
            System.out.print("Participant " + (i + 1) + " email: ");
            String email = scanner.nextLine();
            participants.add(name);
            emailMap.put(name, email);
        }

        List<String> receivers = new ArrayList<>(participants);
        Collections.shuffle(receivers);

        // Ensure no one is assigned to themselves
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).equals(receivers.get(i))) {
                Collections.shuffle(receivers);
                i = -1;
            }
        }

        System.out.println("Sending Secret Santa assignments via email...");
        for (int i = 0; i < participants.size(); i++) {
            String sender = participants.get(i);
            String receiver = receivers.get(i);
            String email = emailMap.get(sender);
            sendEmail(email, sender, receiver);
            System.out.println("Email sent to: " + sender + " (" + email + ")");
        }

        scanner.close();
    }

    private static void sendEmail(String recipientEmail, String senderName, String receiverName) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Your Secret Santa Assignment");
            message.setText(
            	    "Hello " + senderName + ",\n\n" +
            	    "You have been assigned to give a gift to: " + receiverName + ".\n\n" +
            	    "Merry Christmas and Happy gifting!\n" +
            	    "Secret Santa Organizer"
            	);


            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Failed to send email to " + senderName + " (" + recipientEmail + ")");
            e.printStackTrace();
        }
    }
}
