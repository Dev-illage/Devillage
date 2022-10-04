package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.UserStatus;
import com.devillage.teamproject.repository.user.BlockRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.util.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.devillage.teamproject.util.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements Reflection {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("findUser")
    public void findUser() throws Exception {
        // given
        User expectedUser = newInstance(User.class);
        setField(expectedUser, "id", ID1);
        setField(expectedUser, "email", EMAIL1);
        setField(expectedUser, "nickName", NICKNAME1);
        setField(expectedUser, "userStatus", UserStatus.ACTIVE);
        setField(expectedUser, "point", 0L);
        setField(expectedUser, "statusMessage", STATUS_MESSAGE1);
        setField(expectedUser, "pwdLastModifiedAt", PASSWORD_LAST_MODIFIED_AT1);

        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(expectedUser));
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(expectedUser.getId());

        // when
        User actualUser = userService.findUser("someToken");

        // then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("deleteUser")
    public void deleteUser() throws Exception {
        // given
        User user = newInstance(User.class);
        setField(user, "id", ID1);
        setField(user, "email", EMAIL1);
        setField(user, "nickName", NICKNAME1);
        setField(user, "userStatus", UserStatus.ACTIVE);
        setField(user, "point", 0L);
        setField(user, "statusMessage", STATUS_MESSAGE1);
        setField(user, "pwdLastModifiedAt", PASSWORD_LAST_MODIFIED_AT1);

        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(user));
        String originalEmail = user.getEmail();
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(user.getId());

        // when
        userService.deleteUser("someToken");

        // then
        assertEquals(UserStatus.RESIGNED, user.getUserStatus());
        assertNotEquals(originalEmail, user.getEmail());
        assertEquals(0, user.getUserRoles().size());
    }

    @Test
    @DisplayName("blockUser")
    public void blockUser() throws Exception {
        // given
        User srcUser = newInstance(User.class);
        setField(srcUser, "id", ID1);
        User destUser = newInstance(User.class);
        setField(destUser, "id", ID2);

        given(blockRepository.findBySrcUserIdAndDestUserId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.empty());
        given(userRepository.findById(srcUser.getId())).willReturn(Optional.of(srcUser));
        given(userRepository.findById(destUser.getId())).willReturn(Optional.of(destUser));
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(srcUser.getId());

        // when
        Block actualBlock = userService.blockUser(destUser.getId(), "someToken");

        // then
        assertEquals(srcUser, actualBlock.getSrcUser());
        assertEquals(destUser, actualBlock.getDestUser());
        assertEquals(actualBlock, srcUser.getBlockedUsers().get(0));
    }

    @Test
    @DisplayName("undoBlock")
    public void undoBlock() throws Exception {
        // given
        Block block = Block.builder().build();

        given(blockRepository.findBySrcUserIdAndDestUserId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(block));
        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(ID1);

        // when
        Block actualBlock = userService.blockUser(ID2, "someToken");

        // then
        assertEquals(block, actualBlock);

    }

    @Test
    public void updatePassword() throws Exception {
        // given
        User user = newInstance(User.class);
        setField(user, "id",ID1);
        setField(user, "password",ID1);
        PasswordEncoder passwordEncoder;
        String updatedPassword = "aakkff##!!";

        given(jwtTokenUtil.getUserId(Mockito.anyString())).willReturn(ID1);

        // when
        Block actualBlock = userService.blockUser(ID2, "someToken");

        // then
        assertEquals(updatedPassword,user.getPassword());

    }


}
