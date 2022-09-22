package com.devillage.teamproject.controller.user;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.UserStatus;
import com.devillage.teamproject.service.user.UserService;
import com.devillage.teamproject.util.Reflection;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class},
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@Slf4j
class UserControllerTest implements Reflection {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("getProfile")
    public void getProfile() throws Exception {
        // given
        User user = newInstance(User.class);
        setField(user, "id", 1L);
        setField(user, "email", "qwe@qwe.com");
        setField(user, "nickName", "qwe");
        setField(user, "statusMessage", "asd");
        setField(user, "pwdLastModifiedAt", LocalDateTime.now().minusMonths(3));

        given(userService.findUser(Mockito.anyLong())).willReturn(user);

        // when
        ResultActions actions = mockMvc.perform(
                get("/users/profile/{user-id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(jsonPath("$.data.nickname").value(user.getNickName()))
                .andExpect(jsonPath("$.data.statusMessage").value(user.getStatusMessage()))
                .andReturn();

        String actualPasswordModifiedAt = JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.data.passwordModifiedAt").toString();

        assertEquals(user.getPwdLastModifiedAt().toString().substring(0, 19),
                actualPasswordModifiedAt.substring(0, 19));

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("deleteUser")
    public void deleteUser() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(
                delete("/users/{user-id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isNoContent());

    }
}
