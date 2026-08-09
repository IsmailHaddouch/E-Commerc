package com.hero.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerEmailConfirmation(String destinataire, String nomClient, Long idCommande, double montant) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject("Confirmation de commande #" + idCommande);
        message.setText("Bonjour " + nomClient + ",\n\n" +
                "Votre commande #" + idCommande + " a été confirmée et payée.\n" +
                "Montant total : " + montant + " €\n\n" +
                "Merci pour votre achat !\n" +
                "L'équipe E-commerce");
        
        mailSender.send(message);
        System.out.println("✅ Email envoyé à : " + destinataire);
    }
}