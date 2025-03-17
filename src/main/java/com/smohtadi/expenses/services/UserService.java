package com.smohtadi.expenses.services;

import com.smohtadi.expenses.dtos.UserCreateDto;
import com.smohtadi.expenses.dtos.UserDto;
import com.smohtadi.expenses.models.User;
import com.smohtadi.expenses.repositories.UserRepository;
import com.smohtadi.expenses.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto create(UserCreateDto userCreateDto) {
        User user = new User(
                userCreateDto.username(),
                userCreateDto.githubId(),
                DateUtil.utcNow(),
                DateUtil.utcNow()
        );
        userRepository.save(user);
        return toDto(user);
    }

    public UserDto getByUid(String uid) {
        User user = userRepository.findByUid(uid);
        if (user == null) return null;
        return toDto(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId().toString(), user.getUsername());
    }
}
