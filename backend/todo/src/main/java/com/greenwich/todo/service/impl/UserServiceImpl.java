package com.greenwich.todo.service.impl;

import com.greenwich.todo.dto.request.UserRequestDTO;
import com.greenwich.todo.dto.request.UserUpdatePatchDTO;
import com.greenwich.todo.dto.request.UserUpdatePutDTO;
import com.greenwich.todo.dto.response.UserResponseDTO;
import com.greenwich.todo.entity.User;
import com.greenwich.todo.repository.UserRepository;
import com.greenwich.todo.service.UserServiceI;
import com.greenwich.todo.util.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class UserServiceImpl implements UserServiceI {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id)));
    }

    @Override
    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToUserResponseDTO(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserResponseDTO> findAll() {
        List<User> users = userRepository.findAll();
        return convertToUserResponseDTOList(users);
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
       try {
           User newUser = new User();
           newUser.setEmail(userRequestDTO.getEmail());
           newUser.setUsername(createUserName(userRequestDTO.getEmail()));
           newUser.setPassword(userRequestDTO.getPassword());
           //some logic security here to check if is a guest or not is an admin or not
           //default for role field should be USER;
           newUser.setRole("USER");
           newUser.setUserStatus(UserStatus.ACTIVE);
           User savedUser = userRepository.save(newUser);

           return UserResponseDTO.builder()
                   .id(savedUser.getId())
                   .email(savedUser.getEmail())
                   .username(savedUser.getUsername())
                   .role(savedUser.getRole())
                   .build();
       } catch (Exception e) {
           log.error("Service layer : error while creating user: {}", e.getMessage());
           throw e;
       }
    }

    @Override
    public UserResponseDTO updateUserStatus(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            if (user.getUserStatus() == UserStatus.ACTIVE) {
                user.setUserStatus(UserStatus.BANNED);
            } else {
                user.setUserStatus(UserStatus.ACTIVE);
            }
            User updatedUser = userRepository.save(user);
            return convertToUserResponseDTO(updatedUser);
        } catch (Exception e) {
            log.error("Service layer : error while updating user status with id: {}", id);
            throw e;
        }
    }

    private String createUserName(String email) {
        // Giả sử username là phần trước dấu '@' trong email
        return email.split("@")[0];
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdatePutDTO userRequestDTO) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
            user.setEmail(userRequestDTO.getEmail());
            // username có 2 cách update 1 là chỉnh từ email, 2 là tự chỉnh
            //need something like a check if userRequestDTO.getUsername() is null or not
            if (user.getUsername() == null) {
                user.setUsername(createUserName(userRequestDTO.getEmail()));
            }
            user.setPassword(userRequestDTO.getPassword());
            // Giả sử role không được phép thay đổi, nếu cần có thể thêm logic để cập nhật
            user.setRole("USER");
            User updatedUser = userRepository.save(user);

            return convertToUserResponseDTO(updatedUser);
        } catch (Exception e) {
            log.error("Service layer : error while updating user with id: {}", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public UserResponseDTO updatePartialUser(Long id, UserUpdatePatchDTO userRequestDTO) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

            if (userRequestDTO.getEmail() != null) {
                user.setEmail(userRequestDTO.getEmail());
            } else if (userRequestDTO.getUsername() != null) {
                user.setUsername(userRequestDTO.getUsername());
            } else if (userRequestDTO.getPassword() != null) {
                user.setPassword(userRequestDTO.getPassword());
            } else if (userRequestDTO.getRole() != null) {
                user.setRole(userRequestDTO.getRole());
            } else {
                throw new RuntimeException("No fields to update");
            }
            User updatedUser = userRepository.save(user);

            return convertToUserResponseDTO(updatedUser);
        } catch (Exception e) {
            log.error("Service layer : error while updating partial user with id: {}", id);
            throw e; //ném lên controller nhận hoặc global chơi
        }

    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return true;
            } else {
                log.warn("User not found with id: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("Service layer : error while deleting user with id: {}", id);
            throw e; //ném lên controller nhận hoặc global chơi
        }
    }

    @Override
    public Optional<UserResponseDTO> findByEmail(String email) {
        return Optional.empty();
    }

    private List<UserResponseDTO> convertToUserResponseDTOList(List<User> users) {
        return users.stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .build())
                .toList();
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
