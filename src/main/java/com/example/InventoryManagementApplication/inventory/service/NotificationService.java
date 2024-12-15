package com.example.InventoryManagementApplication.inventory.service;

import com.example.InventoryManagementApplication.inventory.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${notification.email.recipient}")
    private String recipientEmail;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLowStockNotification(List<ProductDto> lowStockProducts) {
        if (lowStockProducts.isEmpty()) {
            return; // No email if there's no low-stock product
        }

        StringBuilder messageBody = new StringBuilder("The following products are low on stock:\n");
        for (ProductDto product : lowStockProducts) {
            messageBody.append(String.format("Product: %s (ID: %d) - Stock: %d\n",
                    product.getName(), product.getId(), product.getQuantity()));
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Low Stock Alert");
        mailMessage.setText(messageBody.toString());

        mailSender.send(mailMessage);
    }
}
