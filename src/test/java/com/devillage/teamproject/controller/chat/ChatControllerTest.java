package com.devillage.teamproject.controller.chat;

import com.devillage.teamproject.entity.Chat;
import com.devillage.teamproject.entity.ChatIn;
import com.devillage.teamproject.entity.ChatRoom;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.MessageType;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.chat.ChatService;
import com.devillage.teamproject.util.Reflection;
import com.devillage.teamproject.util.TestConstants;
import com.devillage.teamproject.util.security.SecurityTestConfig;
import com.devillage.teamproject.util.security.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {ChatController.class, JwtTokenUtil.class})
@WithMockCustomUser
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class ChatControllerTest implements Reflection {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChatService chatService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    public void getRoom() throws Exception {
        // given
        ChatRoom room = newInstance(ChatRoom.class);
        Chat chat = newInstance(Chat.class);
        ChatIn chatIn = newInstance(ChatIn.class);
        User user = newInstance(User.class);
        setField(room, "id", ID1);
        setField(room, "roomName", "스프링");
        setField(room, "chats", List.of(chat));
        setField(room, "chatIns", List.of(chatIn));
        setField(chat, "id", ID1);
        setField(chat, "messageType", MessageType.CHAT);
        setField(chat, "content", "안녕하세요.");
        setField(chat, "chatroom", room);
        setField(chat, "user", user);
        setField(user, "nickName", "닉네임");
        setField(chatIn, "id", ID1);
        setField(chatIn, "user", user);
        chat.setDate();

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(chatService.getRoom(ID1, room.getRoomName()))
                .willReturn(room);

        // when
        ResultActions actions = mockMvc.perform(
                get("/chat/{room-name}", room.getRoomName())
                        .header(AUTHORIZATION_HEADER, token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value(room.getRoomName()))
                .andExpect(jsonPath("$.users[0].username").value(user.getNickName()))
                .andExpect(jsonPath("$.chats[0].type").value(chat.getMessageType().name()))
                .andExpect(jsonPath("$.chats[0].sender").value(user.getNickName()))
                .andExpect(jsonPath("$.chats[0].content").value("안녕하세요."))
                .andDo(document("chat/getRoom",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("JWT")
                        ),
                        pathParameters(
                                parameterWithName("room-name").description("채팅방 이름")
                        ),
                        responseFields(
                                fieldWithPath("roomName").type(JsonFieldType.STRING).description("채팅방 이름"),
                                fieldWithPath("users").type(JsonFieldType.ARRAY).description("채팅방 유저"),
                                fieldWithPath("users[].username").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("chats").type(JsonFieldType.ARRAY).description("채팅방 채팅"),
                                fieldWithPath("chats[].type").type(JsonFieldType.STRING).description("채팅 종류"),
                                fieldWithPath("chats[].sender").type(JsonFieldType.STRING).description("채팅 보낸 사람"),
                                fieldWithPath("chats[].content").type(JsonFieldType.STRING).description("채팅 내용"),
                                fieldWithPath("chats[].createdAt").type(JsonFieldType.STRING).description("채팅 시간")
                        )
                ));
    }

    @Test
    public void getRooms() throws Exception {
        // given
        ChatRoom room1 = newInstance(ChatRoom.class);
        ChatRoom room2 = newInstance(ChatRoom.class);
        ChatRoom room3 = newInstance(ChatRoom.class);
        setField(room1, "roomName", "자바");
        setField(room2, "roomName", "스프링");
        setField(room3, "roomName", "리액트");
        setField(room1, "chatIns", List.of(1, 2, 3));
        setField(room2, "chatIns", List.of(1, 2, 3, 4, 5));
        setField(room3, "chatIns", List.of(1, 2, 3, 4, 5, 6, 7));

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(chatService.getRooms())
                .willReturn(List.of(room1, room2, room3));

        // when
        ResultActions actions = mockMvc.perform(
                get("/chat")
                        .header(AUTHORIZATION_HEADER, token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].roomName").value(room1.getRoomName()))
                .andExpect(jsonPath("$.[1].roomName").value(room2.getRoomName()))
                .andExpect(jsonPath("$.[2].roomName").value(room3.getRoomName()))
                .andExpect(jsonPath("$.[0].numberOfUser").value(room1.getChatIns().size()))
                .andExpect(jsonPath("$.[1].numberOfUser").value(room2.getChatIns().size()))
                .andExpect(jsonPath("$.[2].numberOfUser").value(room3.getChatIns().size()))
                .andDo(document("chat/getRooms",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("JWT")
                        ),
                        responseFields(
                                fieldWithPath("[].roomName").type(JsonFieldType.STRING).description("채팅방 이름"),
                                fieldWithPath("[].numberOfUser").type(JsonFieldType.NUMBER).description("채팅방 유저 수")
                        )
                ));
    }

    @Test
    public void postRoom() throws Exception {
        // given
        String roomName = "스프링";
        ChatRoom chatRoom = new ChatRoom(roomName);

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(chatService.postRoom(ID1, roomName))
                .willReturn(chatRoom);

        // when
        ResultActions actions = mockMvc.perform(
                post("/chat/{room-name}", roomName)
                        .header(AUTHORIZATION_HEADER, token)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(document("/chat/postRoom",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("JWT")
                        ),
                        pathParameters(
                                parameterWithName("room-name").description("채팅방 이름")
                        )
                ));
    }

}