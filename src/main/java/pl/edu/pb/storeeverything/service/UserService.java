package pl.edu.pb.storeeverything.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pb.storeeverything.dto.PageRequestDto;
import pl.edu.pb.storeeverything.dto.UserDto;
import pl.edu.pb.storeeverything.dto.UserRegisterDto;
import pl.edu.pb.storeeverything.dto.mapper.UserMapper;
import pl.edu.pb.storeeverything.entity.User;
import pl.edu.pb.storeeverything.exception.EmailAlreadyExists;
import pl.edu.pb.storeeverything.exception.LoginAlreadyExists;
import pl.edu.pb.storeeverything.repository.RoleRepository;
import pl.edu.pb.storeeverything.repository.UserRepository;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final SessionRegistry sessionRegistry;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: [%s] not found".formatted(email)));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID: [%s] not found".formatted(userId)));
    }

    public Page<UserDto> getUsers(PageRequestDto pageRequest) {
        Pageable pageable = PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.by(pageRequest.getSortDirection(), pageRequest.getSortField())
        );

        return userRepository.findAll(pageable)
                .map(userMapper::map);
    }

    @Transactional
    public void register(UserRegisterDto userRegisterDTO) {
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new EmailAlreadyExists("User with email: [%s] already exists".formatted(userRegisterDTO.getEmail()));
        }

        if (userRepository.existsByLogin(userRegisterDTO.getLogin())) {
            throw new LoginAlreadyExists("User with login: [%s] already exists".formatted(userRegisterDTO.getLogin()));
        }

        User user = User.builder()
                .login(userRegisterDTO.getLogin())
                .email(userRegisterDTO.getEmail())
                .password(passwordEncoder.encode(userRegisterDTO.getPassword()))
                .name(userRegisterDTO.getName())
                .surname(userRegisterDTO.getSurname())
                .age(userRegisterDTO.getAge())
                .roles(Set.of(roleRepository.findByRole("ROLE_USER")))
                .enabled(Boolean.TRUE)
                .build();

        userRepository.saveAndFlush(user);
    }

    @Transactional
    public void update(Long userId, UserDto updatedData) {
        User user = findById(userId);

        boolean rolesModified = updatedData.getRoles().size() != user.getRoles().size();
        if (rolesModified) {
            sessionRegistry.getAllSessions(user, false)
                    .forEach(SessionInformation::expireNow);
        }

        user.setName(updatedData.getName());
        user.setSurname(updatedData.getSurname());
        user.setAge(updatedData.getAge());
        user.setEmail(updatedData.getEmail());
        user.setEnabled(updatedData.getEnabled());
        user.setRoles(updatedData.getRoles());
    }
}


