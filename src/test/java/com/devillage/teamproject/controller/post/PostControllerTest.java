package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.security.config.SecurityConfig;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.util.Reflection;
import com.devillage.teamproject.util.security.DefaultConfigWithoutCsrf;
import com.devillage.teamproject.util.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class, JwtTokenUtil.class})
@Import(DefaultConfigWithoutCsrf.class)
@WithMockCustomUser
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class PostControllerTest implements Reflection {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);

    PostControllerTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        setField(user, "id", 1L);
        setField(post, "id", 2L);
    }
//
//    @WithMockUser
//    @Test
//    public void postPost() throws Exception {
//        //given
//        PostDto.Post postDto = newInstance(PostDto.Post.class);
//        Category category = newInstance(Category.class);
//        PostTag postTag = newInstance(PostTag.class);
//        Tag tag = newInstance(Tag.class);
//
//        setField(postDto, "category", CategoryType.NOTICE);
//        setField(postDto, "title", "Mockito 관련 질문입니다.");
//        setField(postDto, "tags", List.of(postTag));
//        setField(postDto, "content", "안녕하세요. 스트링 통째로 드가는게 맞나요");
//        setField(category, "categoryType", CategoryType.NOTICE);
//        setField(postTag, "tag", tag);
//        setField(tag, "id", 1L);
//        setField(tag, "name", "mvcTest");
//
//        given(postService.savePost(Mockito.any(Post.class), Mockito.any(CategoryType.class), Mockito.anyList(), Mockito.anyString())).willReturn(post);
//
//        String content = objectMapper.writeValueAsString(post);
//
//        //when
//        ResultActions actions =
//                mockMvc.perform(
//                        post("/posts")
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
//                );
//
//        //then
//        MvcResult result = actions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.title").value(post.getTitle()))
//                .andExpect(jsonPath("$.content").value(post.getContent()))
//                .andReturn();
//    }

//    @WithMockUser
//    @Test
//    void getPost() throws Exception {
//        //given
//        UserDto.AuthorInfo authorInfo = newInstance(UserDto.AuthorInfo.class);
//        Category category = newInstance(Category.class);
//        PostTag postTag = newInstance(PostTag.class);
//        Tag tag = newInstance(Tag.class);
//        Comment comment = newInstance(Comment.class);
//
//        setField(post, "category", category);
//        setField(post, "id", 1L);
//        setField(post, "title", "Mockito 관련 질문입니다.");
//        setField(post, "tags", List.of(postTag));
//        setField(post, "content", "안녕하세요. 스트링 통째로 드가는게 맞나요");
//        setField(post, "clicks", 1L);
//        setField(post, "createdAt", LocalDateTime.now());
//        setField(post, "lastModifiedAt", LocalDateTime.now());
//        setField(category, "categoryType", CategoryType.NOTICE);
//        setField(postTag, "tag", tag);
//        setField(tag, "id", 1L);
//        setField(tag, "name", "mvcTest");
//        setField(comment, "id", 1L);
//        setField(comment, "content", "잘 봤습니다.");
//        setField(authorInfo, "authorId", 1L);
//        setField(authorInfo, "authorName", "강지");
//        Long id = post.getId();
//
//        given(postService.getPost(Mockito.any(long.class))).willReturn(post);
//
//        //when
//        ResultActions actions =
//                mockMvc.perform(
//                        get("/posts/{post-id}", id)
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                );
//
//        //then
//        //todo: 검증 조건 및 restdocs 추가 예정
//        actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.response.title").value(post.getTitle()))
//                .andExpect(jsonPath("$.response.content").value(post.getContent()))
//                .andReturn();
//    }


    @Test
    void postBookmark() throws Exception {
        // given
        Bookmark bookmark = new Bookmark(user, post);
        setField(bookmark, "id", 3L);

        given(postService.postBookmark(anyString(), anyLong()))
                .willReturn(bookmark);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/bookmark", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, "")
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(user.getId()))
                .andExpect(jsonPath("$.post").value(post.getId()))
                .andExpect(jsonPath("$.bookmark").value(bookmark.getId()))
                .andDo(document("posts/bookmark",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("JWT")
                        ),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("user").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("post").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("bookmark").type(JsonFieldType.NUMBER).description("북마크 식별자")
                        )
                ));

    }

    @Test
    void postReport() throws Exception {
        // given
        ReportedPost report = new ReportedPost(user, post);
        setField(report, "id", 3L);

        given(postService.postReport(anyString(), anyLong()))
                .willReturn(report);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/report", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, "")
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(user.getId()))
                .andExpect(jsonPath("$.post").value(post.getId()))
                .andExpect(jsonPath("$.report").value(report.getId()))
                .andDo(document("posts/report",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("JWT")
                        ),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("user").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("post").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("report").type(JsonFieldType.NUMBER).description("신고글 식별자")
                        )
                ));

    }

    @Test
    void postLike() throws Exception {
        // given
        Like like = new Like(user, post);
        setField(like, "id", 3L);
        setField(post, "user", user);
        setField(post, "likeCount", 1L);

        given(postService.postLike(anyString(), anyLong()))
                .willReturn(post);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/like", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, "")
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.user").value(user.getId()))
                .andExpect(jsonPath("$.post").value(post.getId()))
                .andExpect(jsonPath("$.like").value(post.getLikeCount()))
                .andDo(document("posts/like",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("JWT")
                        ),
                        pathParameters(
                                parameterWithName("post-id").description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("user").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("post").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("like").type(JsonFieldType.NUMBER).description("좋아요 개수")
                        )
                ));

    }

}