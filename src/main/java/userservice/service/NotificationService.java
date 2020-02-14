package userservice.service;

public interface NotificationService {

    void sendEmail(String destination, String message);
    void sendSms(String destination, String message);
}
