package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.UserStatus;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements Reflection {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("findUser")
    public void findUser() throws Exception {
        // given
        User expectedUser = newInstance(User.class);
        setField(expectedUser, "id", 1L);
        setField(expectedUser, "email", "qwe@qwe.com");
        setField(expectedUser, "nickName", "qwe");
        setField(expectedUser, "userStatus", UserStatus.ACTIVE);
        setField(expectedUser, "point", 0L);
        setField(expectedUser, "statusMessage", "asd");
        setField(expectedUser, "pwdLastModifiedAt", LocalDateTime.now().minusMonths(3));

        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(expectedUser));

        // when
        User actualUser = userService.findUser(1L);

        // then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("deleteUser")
    public void deleteUser() throws Exception {
        // given
        User user = newInstance(User.class);
        setField(user, "id", 1L);
        setField(user, "email", "qwe@qwe.com");
        setField(user, "nickName", "qwe");
        setField(user, "userStatus", UserStatus.ACTIVE);
        setField(user, "point", 0L);
        setField(user, "statusMessage", "asd");
        setField(user, "pwdLastModifiedAt", LocalDateTime.now().minusMonths(3));

        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(user));

        // when
        userService.deleteUser(user.getId());

        // then
        assertEquals(UserStatus.RESIGNED, user.getUserStatus());

    }
}
