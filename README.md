#Secret Santa Assignment

A Java GUI app that is designed to be used for Secret Santa pairing and email assignments through SMTP.

##Features
- Input the participant names and emails directly into the GUI.
- Random assignment (no self-assignment is involved in the process).
- Send emails through the SMTP provider that you choose.

  ##What Was Used
  - Java Swing, AWT
  - JavaMail API
  - OOP (Participant, GUI, EmailSender classes)

##Usage
Run the program through: 
javac *.java
java SecretSantaGUI 

- Add all participants names and emails. 
- Click "Assign Secret Santa's).
- Enter SMTP information when asked. 
- Emails sent to participants with there assigned partner listed.
