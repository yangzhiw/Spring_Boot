package com.juzi.service;

import com.juzi.domain.User;
import com.juzi.repository.UserRepository;
import com.juzi.security.SecurityUtils;
import com.juzi.service.util.RandomUtil;
import com.juzi.web.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);


//    @Inject
//    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MongoTemplate mongoTemplate;

//    @Inject
//    private PersistentTokenRepository persistentTokenRepository;

//    @Inject
//    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
           })
           .map(user -> {
//               user.setPassword(passwordEncoder.encode(newPassword));
               user.setPassword(newPassword);
               user.setResetKey(null);
               user.setResetDate(null);
               userRepository.save(user);
               return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
        String langKey) {

        User newUser = new User();
//        Authority authority = authorityRepository.findOne("ROLE_USER");
//        Set<Authority> authorities = new HashSet<>();
//        String encryptedPassword = passwordEncoder.encode(password);
        String encryptedPassword = password;
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
      //  authorities.add(authority);
       // newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO managedUserDTO) {
        User user = new User();
        user.setLogin(managedUserDTO.getLogin());
        user.setFirstName(managedUserDTO.getFirstName());
        user.setLastName(managedUserDTO.getLastName());
        user.setEmail(managedUserDTO.getEmail());
        if (managedUserDTO.getLangKey() == null) {
            user.setLangKey("en"); // default language
        } else {
            user.setLangKey(managedUserDTO.getLangKey());
        }
//        if (managedUserDTO.getAuthorities() != null) {
//            Set<Authority> authorities = new HashSet<>();
//            managedUserDTO.getAuthorities().stream().forEach(
//                authority -> authorities.add(authorityRepository.findOne(authority))
//            );
//            user.setAuthorities(authorities);
//        }
        String encryptedPassword = RandomUtil.generatePassword();
//        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void deleteUserInformation(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public boolean changePassword(String partnerCode, String password) {
        Optional<User> user = userRepository.findOneByLogin(partnerCode);

        if (user.isPresent()) {
            String encryptedPassword = password;
//            String encryptedPassword = passwordEncoder.encode(password);
            Update update = new Update();
            update.set("password", encryptedPassword);
            mongoTemplate.updateFirst(new Query(Criteria.where("partnerCode").is(partnerCode)), update, User.class);
            return true;
        }

        return false;
    }

    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
    //        u.getAuthorities().size();
            return u;
        });
    }

    public User getUserWithAuthorities(String id) {
        User user = userRepository.findOne(id);
    //    user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
    //    user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void removeOldPersistentTokens() {
//        LocalDate now = LocalDate.now();
//        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
//            log.debug("Deleting token {}", token.getSeries());
//            persistentTokenRepository.delete(token);
//        });
//    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }


//    public boolean checkPassword(String partnerCode, String password) {
//        Optional<User> user =  userRepository.findOneByLogin(partnerCode);
//        if (user.isPresent()) {
//            if (passwordEncoder.matches(password, user.get().getPassword()))
//                return true;
//        } else {
//            log.info("Can't find the user {}", partnerCode);
//        }
//        return false;
//    }
}
