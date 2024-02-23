package com.chatbotcongtm.controller;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerApiException;
import com.github.messenger4j.exception.MessengerIOException;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.send.MessagePayload;
import com.github.messenger4j.send.MessagingType;
import com.github.messenger4j.send.NotificationType;
import com.github.messenger4j.send.message.TextMessage;
import com.github.messenger4j.send.recipient.IdRecipient;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.github.messenger4j.Messenger.*;
import static com.github.messenger4j.Messenger.SIGNATURE_HEADER_NAME;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@CrossOrigin("*")
@RestController
@RequestMapping("/webhook")
public class WebhookController {
  private final Messenger messenger;

  @Autowired
  public WebhookController(final Messenger messenger) {
    this.messenger = messenger;
  }

  @GetMapping
  public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
                                              @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken, @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {

    try {
      this.messenger.verifyWebhook(mode, verifyToken);
      return ResponseEntity.ok(challenge);
    } catch (MessengerVerificationException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
  }

  @PostMapping
  public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerVerificationException {
    this.messenger.onReceiveEvents(payload, of(signature), event -> {
      if (event.isTextMessageEvent()) {
        handleTextMessageEvent(event.asTextMessageEvent());
      } else {
        String senderId = event.senderId();
        sendTextMessageUser(senderId, "Chỉ có thể xử lý tin nhắn văn bản.");
      }
    });
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  private void handleTextMessageEvent(TextMessageEvent event) {
    final String senderId = event.senderId();
    sendTextMessageUser(senderId, "Chat bot");

  }

  private void sendTextMessageUser(String idSender, String text) {
    try {
      final IdRecipient recipient = IdRecipient.create(idSender);
      final NotificationType notificationType = NotificationType.REGULAR;
      final String metadata = "DEVELOPER_DEFINED_METADATA";

      final TextMessage textMessage = TextMessage.create(text, empty(), of(metadata));
      final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
          of(notificationType), empty());
      this.messenger.send(messagePayload);
    } catch (MessengerApiException | MessengerIOException e) {
      e.printStackTrace();
    }
  }
}
