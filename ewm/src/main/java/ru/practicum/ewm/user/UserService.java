package ru.practicum.ewm.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserDto save(UserShortDto userShortDto) {
        UserEntity savedUser = userRepository.save(userMapper.toEntity(userShortDto));
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

    private boolean checkIds(String[] ids) {
        for (String id : ids) {
            for (int i = 0; i < id.length(); i++) {
                if (!Character.isDigit(id.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
