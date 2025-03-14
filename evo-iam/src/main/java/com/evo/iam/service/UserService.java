package com.evo.iam.service;

import com.evo.iam.dto.UserDto;
import com.evo.iam.entity.EmailDetails;
import com.evo.iam.entity.User;
import com.evo.iam.payload.request.ChangePasswordRequest;
import com.evo.iam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KeycloakService keycloakService;

    public Optional<User> findbyEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public String updateUser(UUID id, @Valid UserDto user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found!"));
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getAddress() != null && !user.getAddress().isEmpty()) {
            existingUser.setAddress(user.getAddress());
        }
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getProfilePicturePath() != null && !user.getProfilePicturePath().isEmpty()) {
            existingUser.setProfilePicturePath(user.getProfilePicturePath());
        }
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            existingUser.setPhoneNumber(user.getPhoneNumber());
        }
        if (user.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(user.getDateOfBirth());
        }
        userRepository.save(existingUser);
        EmailDetails email = new EmailDetails(existingUser.getEmail(), "Your Information have been changed!!!\n\nIf this is not your action please contact us.", "Successful Changed Information");
        emailService.sendSimpleMail(email);
        return "Cập nhật user thành công";
    }

    public String updatePassword(UUID id, ChangePasswordRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found!"));

        if (!encoder.matches(request.getOldPassword(), existingUser.getPassword())) {
            return "Sai mật khẩu!! Vui lòng nhập lại";
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(request.getNewPassword()));
        }
        keycloakService.changeUserPassword(existingUser.getKeycloakUserId(), request.getNewPassword());
        userRepository.save(existingUser);
        EmailDetails mail = new EmailDetails(existingUser.getEmail(), "Your password have been changed!!!\n\nIf this is not your action please contact us.", "Successful Changed Password");
        emailService.sendSimpleMail(mail);
        return "Đổi mật khẩu thành công";
    }

    public String updateProfileImage(String email, String imageUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with Email " + email + " not found!"));

        if (imageUrl != null && !imageUrl.isEmpty()) {
            user.setProfilePicturePath(imageUrl);
        }

        userRepository.save(user);
        return "Đổi ảnh đại diện thành công";
    }

    public String forgotPassword(String email, String newpassword) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with Email " + email + " not found!"));
        if (newpassword != null && !newpassword.isEmpty()) {
            existingUser.setPassword(encoder.encode(newpassword));
        }
        userRepository.save(existingUser);
        EmailDetails mail = new EmailDetails(existingUser.getEmail(), "Your password have been changed!!!\n\nIf this is not your action please contact us.", "Successful Changed Password");
        emailService.sendSimpleMail(mail);
        return "Đổi mật khẩu thành công";
    }


}
