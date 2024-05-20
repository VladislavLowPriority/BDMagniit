package pro.akosarev.sandbox;


import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User saveOrUpdateUserData(String id, UserDto userDto) {
        log.info("Attempting to save or update user data for ID: {}", id);
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            log.info("Existing user found: {}", user);
            // Update user's data
            log.info("Current user data: {}", user);
            user.setIdentificationNumber(userDto.getIdentificationNumber());
            user.setFullName(userDto.getFullName());
            user.setWorkPhone(userDto.getWorkPhone());
            user.setCellPhone(userDto.getCellPhone());
            user.setDirection(userDto.getDirection());
            user.setDepartment(userDto.getDepartment());
            user.setBranch(userDto.getBranch());
            user.setOffice(userDto.getOffice());
            user.setJobTitle(userDto.getJobTitle());

            log.info("Updated user data: {}", user);
            // Save updated user
            User savedUser = userRepository.save(user);
            log.info("User data saved successfully: {}", savedUser);
            return savedUser;
        } else {
            log.error("User not found with ID: {}", id);
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
    }

    
}
