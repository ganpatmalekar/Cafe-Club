package com.example.swapnil.coffeeshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Session session;

    private String mail;
    private String subject;
    private String message;

    private ProgressDialog progressDialog;

    public SendMail(Context context, String mail, String sub, String msg)
    {
        //initializing variables
        this.context = context;
        this.mail = mail;
        this.subject = sub;
        this.message = msg;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Properties properties = new Properties();

        //Configuring properties for gmail
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        //Creating a new Session
        session = Session.getDefaultInstance(properties, new Authenticator() {
            //Authenticating the password
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
            }
        });

        try {
            //Creating MimeMessage Object
            MimeMessage mimeMessage = new MimeMessage(session);

            //Setting sender address
            mimeMessage.setFrom(new InternetAddress(Config.EMAIL));

            //Adding Receiver
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));

            //Adding subject
            mimeMessage.setSubject(subject);

            //Adding message
            mimeMessage.setText(message);

            //Sending mail
            Transport.send(mimeMessage);

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();
    }
}
