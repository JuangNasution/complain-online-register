package com.example.complain_online.service;

import com.example.complain_online.model.Card;
import com.example.complain_online.model.Customer;
import com.example.complain_online.model.CustomerCard;
import com.example.complain_online.model.Token;
import com.example.complain_online.repository.CardRepository;
import com.example.complain_online.repository.CustomerRepository;
import com.example.complain_online.repository.TokenRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juang Nasution
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardRepository cardRepository;
    private final JavaMailSender javaMailSender;
    private final TokenRepository tokenRepository;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
            CardRepository cardRepository, JavaMailSender javaMailSender, TokenRepository tokenRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardRepository = cardRepository;
        this.javaMailSender = javaMailSender;
        this.tokenRepository = tokenRepository;
    }

    public boolean registCustomer(CustomerCard customerCard) {
        if (customerRepository.findByEmail(customerCard.getEmail()) != null) {
            return false;
        }
        Customer customer = new Customer();
        customer.setName(customerCard.getName());
        customer.setNoKtp(customerCard.getNoKtp());
        customer.setAddress(customerCard.getAddress());
        customer.setEmail(customerCard.getEmail());
        customer.setPassword(passwordEncoder.encode(customerCard.getPassword()));
        customer.setIsActive('N');
        customer.setCreatedDate(new Date());
        customerRepository.save(customer);
        saveCard(customerCard.getCards(), customer.getId());
        sendMail(customerCard.getName(), customerCard.getEmail(), generatedToken(customer.getId()));
        return true;
    }

    public void saveCard(Card[] card, int id) {
        List<Card> cards = new ArrayList<>();
        for (Card string : card) {
            cards.add(new Card(string.getCardNumber(), id));
        }
        cardRepository.saveAll(cards);
    }

    public String updateActive(Token token) {
        Token t = tokenRepository.findByConfirmationToken(token.getConfirmationToken());
        if (t != null) {
            Customer customer = customerRepository.findById(t.getCustomerId()).get();
            if (customer.getIsActive().equals('N')) {
                customer.setIsActive('Y');
                customerRepository.save(customer);
            } else {
                return "Your Account Already Activated";
            }
        } else {
            return "Wrong Token";
        }
        return "Yout Account Has Been Activated";
    }

    public void sendMail(String name, String email, String token) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String text = "Hello " + name + "\n"
                + "\n"
                + "You registered an account on complain-online.com , before being able to use your account you need "
                + "to verify that this is your email address by input this token:" + token
                + "\n"
                + "Thanks! - The BNI team: \n";
        //mailMessage.setFrom("bootcampbni@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Verification Email #" + name + new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()));
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }

    public String loginCustomer(String email, String password) {
        if (customerRepository.findByEmail(email) == null) {
            return "email not found";
        } else {
            Customer customer = customerRepository.findByEmail(email);
            String pass = customer.getPassword();
            if (!passwordEncoder.matches(password, pass)) {
                return "wrong password";
            } else if (customer.getIsActive().equals('N')) {
                return "account not activated yet";
            }
        }
        return "login success";
    }

    public String generatedToken(int id) {
        Token token = new Token();
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();
        String generatedToken = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        token.setConfirmationToken(generatedToken);
        token.setCreatedDate(new Date());
        token.setCustomerId(id);
        tokenRepository.save(token);
        return generatedToken;
    }
}
