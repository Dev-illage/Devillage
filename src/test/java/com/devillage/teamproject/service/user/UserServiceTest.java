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
}
