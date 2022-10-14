package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.dto.PostDto;
import com.devillage.teamproject.dto.ReportDto;
import com.devillage.teamproject.dto.UserDto;
import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.entity.enums.ReportType;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.comment.CommentService;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.util.Reflection;
import com.devillage.teamproject.util.TestConstants;
import com.devillage.teamproject.util.security.SecurityTestConfig;
import com.devillage.teamproject.util.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
    CommentService commentService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);

    PostControllerTest() throws Exception {
        setField(user, "id", ID1);
        setField(post, "id", ID2);
    }

    @WithMockUser
    @Test
    public void postPost() throws Exception {
        //given
        PostDto.Post postDto = PostDto.Post.builder()
                .postId(post.getId())
                .title("안녕하세요.")
                .content(COMMENT_CONTENT)
                .tags(List.of("tag1", "tag2"))
                .category(CategoryType.NOTICE)
                .build();


        String content = objectMapper.writeValueAsString(postDto);

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(postService.savePost(any(Post.class), any(CategoryType.class), Mockito.anyList(), Mockito.anyLong())).willReturn(postDto.toEntity());

        //when
        ResultActions actions =
                mockMvc.perform(
                        post("/posts")
                                .header(AUTHORIZATION_HEADER, token)
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
                .tags(List.of("tag1", "tag2"))
                .category(CategoryType.NOTICE)
                .build();

        String content = objectMapper.writeValueAsString(patchDto);

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(postService.editPost(any(Post.class), any(CategoryType.class), Mockito.anyList(), Mockito.anyLong(), Mockito.anyLong())).willReturn(patchDto.toEntity());

        //when
        ResultActions actions =
                mockMvc.perform(
                        patch("/posts/{post-id}", post.getId())
                                .header(AUTHORIZATION_HEADER, token)
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
        setField(post,"postLastModifiedAt", LocalDateTime.of(0000, 12, 31, 00, 00,00,3333));
        setField(category, "categoryType", CategoryType.NOTICE);
        setField(postTag, "tag", tag);
        setField(tag, "id", 1L);
        setField(tag, "name", "mvcTest");
        setField(comment, "id", 1L);
        setField(comment, "content", "잘 봤습니다.");
        setField(authorInfo, "authorId", 1L);
        setField(authorInfo, "authorName", "강지");
        post.setDate();
        post.addUser(user);
        Long id = post.getId();
        setField(user, "nickName", NICKNAME1);

        Comment comment1 = Comment.builder().id(ID1).content(COMMENT_CONTENT).user(user).post(post).commentLikes(List.of()).build();
        ReComment reComment1_1 = ReComment.builder().id(ID1).content(COMMENT_CONTENT).user(user).comment(comment1).build();
        comment1.getReComments().add(reComment1_1);
        Comment comment2 = Comment.builder().id(ID2).content(COMMENT_CONTENT).user(user).post(post).commentLikes(List.of()).build();
        Comment comment3 = Comment.builder().id(ID2 + 1).content(COMMENT_CONTENT).user(user).post(post).commentLikes(List.of()).build();

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(postService.getPost(any(long.class))).willReturn(post);
        given(commentService.findComments(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .willReturn(new PageImpl<>(List.of(comment1, comment2, comment3)));

        //when
        ResultActions actions =
                mockMvc.perform(
                        get("/posts/{post-id}", id)
                                .header(AUTHORIZATION_HEADER, token)
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

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/bookmark", post.getId())
                        .header(AUTHORIZATION_HEADER, token)
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
        ReportType reportType = ReportType.AD;
        Integer reportTypeNum = reportType.ordinal() + 1;
        String content = "광고에요.";
        ReportedPost report = new ReportedPost(user, post, reportType, content);
        setField(report, "id", 3L);

        ReportDto reportDto = newInstance(ReportDto.class);
        setField(reportDto, "reportType", reportTypeNum);
        setField(reportDto, "content", content);

        String json = objectMapper.writeValueAsString(reportDto);

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(postService.postReport(ID1, post.getId(), reportTypeNum, content))
                .willReturn(report);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/report", post.getId())
                        .header(AUTHORIZATION_HEADER, token)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
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
                        requestFields(
                                fieldWithPath("reportType").type(JsonFieldType.NUMBER).description("신고 분류"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("신고 사유")
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

        String token = BEARER + jwtTokenUtil.createAccessToken(EMAIL1, ID1, TestConstants.ROLES);

        given(postService.postLike(any(), anyLong()))
                .willReturn(post);

        // when
        ResultActions actions = mockMvc.perform(
                post("/posts/{post-id}/like", post.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION_HEADER, token)
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
    @Test
    void deletePost() throws Exception{
        //given
        Post post = newInstance(Post.class);
        setField(post,"id",1L);

        Long postId = 1L;
        doNothing().when(postService).deletePost(postId);

        //when
        ResultActions actions =
                mockMvc.perform(
                        delete("/posts/{post-id}", postId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        actions
                .andExpect(status().isNoContent())
                .andReturn();
    }

}