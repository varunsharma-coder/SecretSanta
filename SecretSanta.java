import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

//the email credentials of sender (used mailtrap app for emailing) 
public class SecretSanta {
    private static final String SENDER_EMAIL = "your_email@example.com";
    private static final String SENDER_PASSWORD = "your_password";

    public static void main(String[] args) {
    	//user input using scanner 
        Scanner scanner = new Scanner(System.in);
        
        //list that stores participant names 
        List<String> participants = new ArrayList<>();
        
        //map that stores emails by name 
        Map<String, String> emailMap = new HashMap<>();

        System.out.println("Enter the names and email addresses of 8 participants:");
        
        //get 8 names of participants and there email
        for (int i = 0; i < 8; i++) {
            System.out.print("Participant " + (i + 1) + " name: ");
            String name = scanner.nextLine();
            System.out.print("Participant " + (i + 1) + " email: ");
            String email = scanner.nextLine();
            
            //its added to list and map 
            participants.add(name);
            emailMap.put(name, email);
        }

        //create shuffled copy of people to assign the receivers giving the gift 
        List<String> receivers = new ArrayList<>(participants);
        Collections.shuffle(receivers);

        // make sure no one is assigned to themselves
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).equals(receivers.get(i))) {
                Collections.shuffle(receivers);
                i = -1; //start check again from beginning 
            }
        }
        // send participant email with there assigned person 
        System.out.println("Sending Secret Santa assignments via email...");
        for (int i = 0; i < participants.size(); i++) { 
            String sender = participants.get(i); //participant giving the gift
            String receiver = receivers.get(i); // participant receiving gift 
            String email = emailMap.get(sender); //sender email 
            sendEmail(email, sender, receiver); //send email 
            System.out.println("Email sent to: " + sender + " (" + email + ")");
        }

        scanner.close();
    }

    //sends secret santa email to participant 
    private static void sendEmail(String recipientEmail, String senderName, String receiverName) {
    	// SMTP properties set up to send email 
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", "587");

        //session with authentication  
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
        	
        	//new email message is created
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Your Secret Santa Assignment");
            
            //email that is sent to the participant on who they have 
            message.setText(
            	    "Hello " + senderName + ",\n\n" +
            	    "You have been assigned to give a gift to: " + receiverName + ".\n\n" +
            	    "Merry Christmas and Happy gifting!\n" +
            	    "Secret Santa Organizer"
            	);

            //send message 
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Failed to send email to " + senderName + " (" + recipientEmail + ")");
            e.printStackTrace();
        }
    }
}
