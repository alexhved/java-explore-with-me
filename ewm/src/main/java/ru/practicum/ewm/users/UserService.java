package ru.practicum.ewm.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserDto save(NewUserRequest newUserRequest) {
        UserEntity savedUser = userRepository.save(userMapper.toEntity(newUserRequest));
        return userMapper.toUserDto(savedUser);
    }

    public List<UserDto> find(Long[] ids, Integer from, Integer size) {
        Page<UserEntity> userEntityPage = userRepository.findByIdS(ids, PageRequest.of(from, size));
        return userEntityPage.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

}
