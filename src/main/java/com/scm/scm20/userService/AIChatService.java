package com.scm.scm20.userService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scm.scm20.entity.Contact;
import com.scm.scm20.entity.User;
import com.scm.scm20.userRepo.ContactRepository;
import com.scm.scm20.userRepo.UserRepository;

@Service
public class AIChatService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OpenAiService openAIService;

    private final ObjectMapper mapper = new ObjectMapper();

    public String processMessage(String message) {
        try {
            if (message == null || message.trim().isEmpty()) {
                return "Please type a contact question.";
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            Optional<User> optionalUser = userRepository.findByEmail(username);
            if (optionalUser.isEmpty()) {
                return "User not found.";
            }

            User user = optionalUser.get();

            String localAnswer = answerFromLocalIntent(message, user);
            if (localAnswer != null) {
                return localAnswer;
            }

            String aiResponse;
            try {
                aiResponse = openAIService.getAIResponse(message);
            } catch (Exception ex) {
                return fallback(message, user);
            }

            if (aiResponse == null) {
                return fallback(message, user);
            }

            JsonNode choices = mapper.readTree(aiResponse).path("choices");
            if (choices.isEmpty()) {
                return fallback(message, user);
            }

            String content = choices.get(0).path("message").path("content").asText();
            int start = content.indexOf("{");
            int end = content.lastIndexOf("}");

            if (start == -1 || end == -1) {
                return fallback(message, user);
            }

            JsonNode json = mapper.readTree(content.substring(start, end + 1));
            String intent = json.path("intent").asText("");
            String name = json.path("name").asText("");

            if (intent.equalsIgnoreCase("get_phone")) {
                return contactPhone(name, user);
            }

            if (intent.equalsIgnoreCase("get_email")) {
                return contactEmail(name, user);
            }

            if (intent.equalsIgnoreCase("get_all")) {
                return allContacts(user);
            }

            return fallback(message, user);
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong while reading your request.";
        }
    }

    private String answerFromLocalIntent(String message, User user) {
        String normalized = normalize(message);

        if (normalized.contains("all contact") || normalized.contains("all contacts")
                || normalized.contains("show contacts") || normalized.contains("list contacts")
                || normalized.equals("contacts")) {
            return allContacts(user);
        }

        if (normalized.contains("email") || normalized.contains("mail")) {
            String name = cleanName(normalized, "email", "mail", "ka", "ki", "kya", "hai", "do", "batao", "id",
                    "what", "is", "the", "of", "please", "tell", "me", "contact", "contacts");
            return contactEmail(name, user);
        }

        if (normalized.contains("number") || normalized.contains("phone") || normalized.contains("mobile")) {
            String name = cleanName(normalized, "number", "phone", "mobile", "ka", "ki", "kya", "hai", "do", "batao",
                    "what", "is", "the", "of", "please", "tell", "me", "contact", "contacts");
            return contactPhone(name, user);
        }

        return null;
    }

    private String fallback(String message, User user) {
        String answer = answerFromLocalIntent(message, user);
        if (answer != null) {
            return answer;
        }

        return "I can help with contact questions like: Raja ka number kya hai, Raja email, or show all contacts.";
    }

    private String contactPhone(String name, User user) {
        String searchName = normalizeSearchText(name);

        if (searchName.isBlank()) {
            return "Please include the contact name.";
        }

        List<Contact> list = contactRepository.findByUserAndNameLike(user, searchName);
        if (list.isEmpty()) {
            return "Contact not found.";
        }

        Contact contact = list.get(0);
        return contact.getName() + " phone number: " + contact.getPhoneNumber();
    }

    private String contactEmail(String name, User user) {
        String searchName = normalizeSearchText(name);

        if (searchName.isBlank()) {
            return "Please include the contact name.";
        }

        List<Contact> list = contactRepository.findByUserAndNameLike(user, searchName);
        if (list.isEmpty()) {
            return "Contact not found.";
        }

        Contact contact = list.get(0);
        return contact.getName() + " email: " + contact.getEmail();
    }

    private String allContacts(User user) {
        List<Contact> contacts = contactRepository.findByUser(user);
        if (contacts.isEmpty()) {
            return "No contacts found.";
        }

        StringBuilder reply = new StringBuilder("Your contacts:");
        for (Contact contact : contacts) {
            reply.append("\n")
                    .append(contact.getName())
                    .append(" - ")
                    .append(contact.getPhoneNumber());
        }

        return reply.toString();
    }

    private String normalize(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9@. ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String cleanName(String message, String... wordsToRemove) {
        String name = message;
        for (String word : wordsToRemove) {
            name = name.replaceAll("\\b" + word + "\\b", " ");
        }
        return name.replaceAll("\\s+", " ").trim();
    }

    private String normalizeSearchText(String value) {
        if (value == null) {
            return "";
        }

        return normalize(value);
    }
}
