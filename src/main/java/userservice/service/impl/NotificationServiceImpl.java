package userservice.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import userservice.service.NotificationService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.sns.region}")
    private String snsRegion;

    @Value("${aws.sns.sender-id}")
    private String snsSender;

    @Value("${aws.ses.sender-email}")
    private String senderEmail;

    @Value("${aws.ses.region}")
    private String sesRegion;

    // The subject line for the email.
    final String SUBJECT = "One last step to complete your registration with App";

    // The HTML body for the email.
    final String HTMLBODY = "<h1>Please verify your email address</h1>"
            + "<p>Thank you for registering with our app. To complete registration process and be able to log in,"
            + " click on the following link: "
            + "<a href='http://localhost:8101/api/users/verify/";
    // The email body for recipients with non-HTML email clients.
    final String TEXTBODY = "Please verify your email address. "
            + "Thank you for registering with our app. To complete registration process and be able to log in,"
            + " open then the following URL in your browser window: "
            + " http://localhost:8101/api/users/verify/";

    @Override
    public void sendEmail(String destination, String message) {
        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            .withCredentials(new AWSStaticCredentialsProvider(getBasicAWSCredentials()))
                            .withRegion(sesRegion).build();

            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(destination))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(HTMLBODY + message + "'>\n "
                                                                + "Final step to complete your registration \n"
                                                                + "</a><br/><br/> \n"
                                                                + "Thank you! And we are waiting for you inside!"))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(TEXTBODY + message + "\n Thank you! And we are waiting for you inside!")))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(senderEmail);

            client.sendEmail(request);
            log.info("Email sent!");
        } catch (Exception ex) {
            log.error("The email was not sent. Error message: "
                    + ex.getMessage());
        }
    }

    @Override
    public void sendSms(String destination, String message) {
        AmazonSNS awsSns = getAwsSns();
        Map<String, MessageAttributeValue> smsAttributes = getSmsStringMessageAttributeValueMap();

        awsSns.publish(new PublishRequest()
                .withMessage(TEXTBODY + message + "\n Thank you! And we are waiting for you inside!")
                .withPhoneNumber(destination)
                .withMessageAttributes(smsAttributes));
    }

    private Map<String, MessageAttributeValue> getSmsStringMessageAttributeValueMap() {
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<String, MessageAttributeValue>();
        smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
                .withStringValue(snsSender) //The sender ID shown on the device.
                .withDataType("String"));
        smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
                .withStringValue("0.50") //Sets the max price to 0.50 USD.
                .withDataType("Number"));
        smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
                .withStringValue("Promotional") //Sets the type to promotional.
                .withDataType("String"));
        return smsAttributes;
    }

    private AmazonSNS getAwsSns() {
        return AmazonSNSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(getBasicAWSCredentials()))
                .withRegion(snsRegion)
                .build();
    }

    private BasicAWSCredentials getBasicAWSCredentials() {
        return new BasicAWSCredentials(
                accessKey,
                secretKey);
    }
}
