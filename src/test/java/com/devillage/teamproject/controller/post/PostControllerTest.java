package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.dto.CommentDto;
import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.security.config.SecurityConfig;
import com.devillage.teamproject.security.resolver.ResultJwtArgumentResolver;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.util.Reflection;
import com.devillage.teamproject.util.TestConstants;
import com.devillage.teamproject.util.security.SecurityTestConfig;
import com.devillage.teamproject.util.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
@WithMockCustomUser
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class PostControllerTest implements Reflection {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    ResultJwtArgumentResolver resultJwtArgumentResolver;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();


    User user = newInstance(User.class);
    Post post = newInstance(Post.class);

    PostControllerTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        setField(user, "id", 1L);
        setField(post, "id", 2L);
    }

    @WithMockUser
    @Test
    public void postPost() throws Exception {
        //given
        PostDto.Post postDto = PostDto.Post.builder()
                .postId(post.getId())
                .title("안녕하세요.")
                .content(COMMENT_CONTENT)
                .tags(List.of("tag1","tag2"))
                .category(CategoryType.NOTICE)
                .build();


        String content = objectMapper.writeValueAsString(postDto);

        given(postService.savePost(any(Post.class), any(CategoryType.class), Mockito.anyList(), Mockito.isNull())).willReturn(postDto.toEntity());

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/posts")
                                .header(AUTHORIZATION_HEADER, "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        //then
        MvcResult result = actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andReturn();
    }

    @WithMockUser
    @Test
    public void patchPost() throws Exception {
        //given
//        setField(post,"title","수정 전 title");
//        setField(post,"content","수정 전 content");
        PostDto.Patch patchDto = PostDto.Patch.builder()
                .postId(post.getId())
                .title("안녕하세요.")
                .content(COMMENT_CONTENT)
                .tags(List.of("tag1","tag2"))
                .category(CategoryType.NOTICE)
                .build();

        String content = objectMapper.writeValueAsString(patchDto);

        given(postService.editPost(any(Post.class), any(CategoryType.class), Mockito.anyList(), Mockito.isNull(),Mockito.anyLong())).willReturn(patchDto.toEntity());

        //when
        ResultActions actions =
                mockMvc.perform(
                        patch("/posts/{post-id}",post.getId())
                                .header(AUTHORIZATION_HEADER, "token")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        //then
        MvcResult result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andReturn();
    }

    @WithMockUser
    @Test
    void getPost() throws Exception {
        //given
        UserDto.AuthorInfo authorInfo = newInstance(UserDto.AuthorInfo.class);
        Category category = newInstance(Category.class);
        PostTag postTag = newInstance(PostTag.class);
        Tag tag = newInstance(Tag.class);
        Comment comment = newInstance(Comment.class);

        setField(user, "id", 1L);
        setField(post, "id", 1L);
        setField(post, "category", category);
        setField(post, "title", "Mockito 관련 질문입니다.");
        setField(post, "tags", List.of(postTag));
        setField(post, "content", "안녕하세요. 스트링 통째로 드가는게 맞나요");
        setField(post, "clicks", 1L);
        setField(category, "categoryType", CategoryType.NOTICE);
        setField(postTag, "tag", tag);
        setField(tag, "id", 1L);
        setField(tag, "name", "mvcTest");
        setField(comment, "id", 1L);
        setField(comment, "content", "잘 봤습니다.");
        setField(authorInfo, "authorId", 1L);
        setField(authorInfo, "authorName", "강지");
        post.addUser(user);
        post.setDate();
        Long id = post.getId();

        given(postService.getPost(any(long.class))).willReturn(post);

        //when
        ResultActions actions =
                mockMvc.perform(
                        get("/posts/{post-id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        //todo: 검증 조건 및 restdocs 추가 예정
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.author.authorId").value(UserDto.AuthorInfo.of(post.getUser()).getAuthorId()))
                .andExpect(jsonPath("$.data.category").value(post.getCategory().getCategoryType().name()))
                .andReturn();
    }


    @Test
    void postBookmark() throws Exception {
        // given
        Bookmark bookmark = new Bookmark(user, post);
        setField(bookmark, "id", 3L);

        given(postService.postBookmark(any(), anyLong()))
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

        given(postService.postReport(any(), anyLong()))
                .willReturn(report);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/report", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, "token")
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

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL2, ID1, TestConstants.ROLES);

        given(postService.postLike(any(), anyLong()))
                .willReturn(post);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/like", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
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