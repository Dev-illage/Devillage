package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.UserStatus;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.repository.user.BlockRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.util.Reflection;
import com.devillage.teamproject.util.TestConstants;
import com.devillage.teamproject.util.auth.AuthTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.devillage.teamproject.security.util.JwtConstants.ACCESS_TOKEN_EXPIRE_COUNT;
import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements Reflection {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    PasswordEncoder passwordEncoder;

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

//    @Test
//    public void updatePassword() throws Exception {
//        // given
//        User user = newInstance(User.class);
//        setField(user, "id",ID1);

//        setField(user, "password",PASSWORD1);
//
//        String updatedPassword = "sdfsdff!23##";
//
//        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
//        given(userService.validatePassword(updatedPassword)).willReturn(updatedPassword);
//
//        // when
//        userService.updatePassword(ID1, updatedPassword);
//
//        // then
//        assertThrows(BusinessLogicException.class, () -> user.getOauthProvider().equals(null));
//        assertEquals(updatedPassword,user.getPassword());
//    }

    @Test
    public void editUser() throws Exception {
        // given
        User user1 = newInstance(User.class);
        setField(user1, "id", ID1);
        setField(user1, "nickName", NICKNAME1);
        User user2 = newInstance(User.class);
        setField(user2, "id", ID2);
        setField(user2, "nickName", NICKNAME2);

        String newNickname = NICKNAME1 + "abc";
        String newStatusMessage = STATUS_MESSAGE1 + "abc";
        String alreadyExistNickName = NICKNAME2 + "abc";

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        given(userRepository.findById(user1.getId()))
                .willReturn(Optional.of(user1));
        given(userRepository.findById(user2.getId()))
                .willReturn(Optional.of(user2));

        given(userRepository.existsByNickName(anyString()))
                .willReturn(false);
        given(userRepository.existsByNickName(alreadyExistNickName))
                .willReturn(true);

        // when
        userService.editUser(user1.getId(), user1.getNickName(), null);
        userService.editUser(user1.getId(), newNickname, null);
        userService.editUser(user2.getId(), null, newStatusMessage);

        // then
        assertThat(user1.getNickName()).isEqualTo(newNickname);
        assertThat(user2.getStatusMessage()).isEqualTo(newStatusMessage);
        assertThrows(BusinessLogicException.class,
                () -> userService.editUser(3L, null, null));
        assertThrows(BusinessLogicException.class,
                () -> userService.editUser(user1.getId(), alreadyExistNickName, null));
    }

}



