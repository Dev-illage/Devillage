package com.devillage.teamproject.controller.post;

import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.service.post.PostService;
import com.devillage.teamproject.util.Reflection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        })
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class GetPostsControllerTest implements Reflection {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    User user = newInstance(User.class);
    Post post = newInstance(Post.class);

    Category category = newInstance(Category.class);
    PostTag postTag = newInstance(PostTag.class);
    Tag tag = newInstance(Tag.class);
    PostsFile postsFile = newInstance(PostsFile.class);
    File file = newInstance(File.class);

    int page = 1;
    int size = 2;

    List<Post> posts = new ArrayList<>();
    Page<Post> pagePosts;

    GetPostsControllerTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        setField(user, "id", 1L);
        setField(post, "id", 2L);

        setField(category, "categoryType", CategoryType.FREE);
        setField(postTag, "tag", tag);
        setField(tag, "id", 1L);
        setField(tag, "name", "태그");
        setField(postsFile, "file", file);
        setField(file, "id", 1L);
        setField(file, "originalFileName", "originalFileName");
        setField(file, "fileSize", 1234L);
        setField(file, "localPath", "/localPath/file");
        setField(file, "remotePath", "/remotePath/file");
        setField(file, "type", "type");
        setField(file, "user", user);

        setField(post, "title", "제목");
        setField(post, "content", "내용");
        setField(post, "clicks", 1L);
        setField(post, "category", category);
        setField(post, "tags", List.of(postTag));
        setField(post, "postsFile", List.of(postsFile));
        post.setDate();

        posts.add(post);

        pagePosts = new PageImpl<>(posts, PageRequest.of(page - 1, size), 1);
    }

    @Test
    public void getPostsByCategory() throws Exception {
        // given
        String freeCategory = CategoryType.FREE.name();

        given(postService.getPostsByCategory(freeCategory, page, size))
                .willReturn(pagePosts);

        // when
        ResultActions actions = mockMvc.perform(
                get("/posts?category={category}&page={page}&size={size}", freeCategory, page, size)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(post.getId()))
                .andExpect(jsonPath("$.data[0].title").value(post.getTitle()))
                .andExpect(jsonPath("$.data[0].content").value(post.getContent()))
                .andExpect(jsonPath("$.data[0].clicks").value(post.getClicks()))
                .andExpect(jsonPath("$.data[0].category").value(post.getCategory().getCategoryType().name()))
                .andExpect(jsonPath("$.data[0].tags[0].tagId").value(tag.getId()))
                .andExpect(jsonPath("$.data[0].tags[0].name").value(tag.getName()))
                .andExpect(jsonPath("$.data[0].files[0].id").value(file.getId()))
                .andExpect(jsonPath("$.data[0].files[0].originalFileName").value(file.getOriginalFileName()))
                .andExpect(jsonPath("$.data[0].files[0].fileSize").value(file.getFileSize()))
                .andExpect(jsonPath("$.data[0].files[0].localPath").value(file.getLocalPath()))
                .andExpect(jsonPath("$.data[0].files[0].remotePath").value(file.getRemotePath()))
                .andExpect(jsonPath("$.data[0].files[0].type").value(file.getType()))
                .andExpect(jsonPath("$.data[0].files[0].userId").value(file.getUser().getId()))
                .andExpect(jsonPath("$.pageInfo.page").value(page))
                .andExpect(jsonPath("$.pageInfo.size").value(size))
                .andDo(document("posts/getPostsByCategory",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("category").description("카테고리"),
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("data[].clicks").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                                fieldWithPath("data[].tags").type(JsonFieldType.ARRAY).description("게시글 태그"),
                                fieldWithPath("data[].tags[].tagId").type(JsonFieldType.NUMBER).description("태그 식별자"),
                                fieldWithPath("data[].tags[].name").type(JsonFieldType.STRING).description("태그 이름"),
                                fieldWithPath("data[].files").type(JsonFieldType.ARRAY).description("게시글 파일"),
                                fieldWithPath("data[].files[].id").type(JsonFieldType.NUMBER).description("파일 식별자"),
                                fieldWithPath("data[].files[].originalFileName").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("data[].files[].fileSize").type(JsonFieldType.NUMBER).description("파일 이름"),
                                fieldWithPath("data[].files[].localPath").type(JsonFieldType.STRING).description("파일 로컬 경로"),
                                fieldWithPath("data[].files[].remotePath").type(JsonFieldType.STRING).description("파일 리모트 경로"),
                                fieldWithPath("data[].files[].type").type(JsonFieldType.STRING).description("파일 타입"),
                                fieldWithPath("data[].files[].userId").type(JsonFieldType.NUMBER).description("파일 생성자"),
                                fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("게시글 생성일시"),
                                fieldWithPath("data[].lastModifiedAt").type(JsonFieldType.STRING).description("게시글 수정일시"),

                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                        )
                ));
    }

    @Test
    public void getPostsBySearch() throws Exception {
        // given
        String word = "내용";

        given(postService.getPostsBySearch(word, page, size))
                .willReturn(pagePosts);

        // when
        ResultActions actions = mockMvc.perform(
                get("/posts/search?q={word}&page={page}&size={size}", word, page, size)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(post.getId()))
                .andExpect(jsonPath("$.data[0].title").value(post.getTitle()))
                .andExpect(jsonPath("$.data[0].content").value(post.getContent()))
                .andExpect(jsonPath("$.data[0].clicks").value(post.getClicks()))
                .andExpect(jsonPath("$.data[0].category").value(post.getCategory().getCategoryType().name()))
                .andExpect(jsonPath("$.data[0].tags[0].tagId").value(tag.getId()))
                .andExpect(jsonPath("$.data[0].tags[0].name").value(tag.getName()))
                .andExpect(jsonPath("$.data[0].files[0].id").value(file.getId()))
                .andExpect(jsonPath("$.data[0].files[0].originalFileName").value(file.getOriginalFileName()))
                .andExpect(jsonPath("$.data[0].files[0].fileSize").value(file.getFileSize()))
                .andExpect(jsonPath("$.data[0].files[0].localPath").value(file.getLocalPath()))
                .andExpect(jsonPath("$.data[0].files[0].remotePath").value(file.getRemotePath()))
                .andExpect(jsonPath("$.data[0].files[0].type").value(file.getType()))
                .andExpect(jsonPath("$.data[0].files[0].userId").value(file.getUser().getId()))
                .andExpect(jsonPath("$.pageInfo.page").value(page))
                .andExpect(jsonPath("$.pageInfo.size").value(size))
                .andDo(document("posts/getPostsBySearch",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("q").description("검색어"),
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("data[].clicks").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                                fieldWithPath("data[].tags").type(JsonFieldType.ARRAY).description("게시글 태그"),
                                fieldWithPath("data[].tags[].tagId").type(JsonFieldType.NUMBER).description("태그 식별자"),
                                fieldWithPath("data[].tags[].name").type(JsonFieldType.STRING).description("태그 이름"),
                                fieldWithPath("data[].files").type(JsonFieldType.ARRAY).description("게시글 파일"),
                                fieldWithPath("data[].files[].id").type(JsonFieldType.NUMBER).description("파일 식별자"),
                                fieldWithPath("data[].files[].originalFileName").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("data[].files[].fileSize").type(JsonFieldType.NUMBER).description("파일 이름"),
                                fieldWithPath("data[].files[].localPath").type(JsonFieldType.STRING).description("파일 로컬 경로"),
                                fieldWithPath("data[].files[].remotePath").type(JsonFieldType.STRING).description("파일 리모트 경로"),
                                fieldWithPath("data[].files[].type").type(JsonFieldType.STRING).description("파일 타입"),
                                fieldWithPath("data[].files[].userId").type(JsonFieldType.NUMBER).description("파일 생성자"),
                                fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("게시글 생성일시"),
                                fieldWithPath("data[].lastModifiedAt").type(JsonFieldType.STRING).description("게시글 수정일시"),

                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                        )
                ));
    }

    @Test
    public void getPostsByBookmark() throws Exception {
        // given
        given(postService.getPostsByBookmark("", page, size))
                .willReturn(pagePosts);

        // when
        ResultActions actions = mockMvc.perform(
                get("/posts/bookmark?page={page}&size={size}", page, size)
                        .header(HttpHeaders.AUTHORIZATION, "")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(post.getId()))
                .andExpect(jsonPath("$.data[0].title").value(post.getTitle()))
                .andExpect(jsonPath("$.data[0].content").value(post.getContent()))
                .andExpect(jsonPath("$.data[0].clicks").value(post.getClicks()))
                .andExpect(jsonPath("$.data[0].category").value(post.getCategory().getCategoryType().name()))
                .andExpect(jsonPath("$.data[0].tags[0].tagId").value(tag.getId()))
                .andExpect(jsonPath("$.data[0].tags[0].name").value(tag.getName()))
                .andExpect(jsonPath("$.data[0].files[0].id").value(file.getId()))
                .andExpect(jsonPath("$.data[0].files[0].originalFileName").value(file.getOriginalFileName()))
                .andExpect(jsonPath("$.data[0].files[0].fileSize").value(file.getFileSize()))
                .andExpect(jsonPath("$.data[0].files[0].localPath").value(file.getLocalPath()))
                .andExpect(jsonPath("$.data[0].files[0].remotePath").value(file.getRemotePath()))
                .andExpect(jsonPath("$.data[0].files[0].type").value(file.getType()))
                .andExpect(jsonPath("$.data[0].files[0].userId").value(file.getUser().getId()))
                .andExpect(jsonPath("$.pageInfo.page").value(page))
                .andExpect(jsonPath("$.pageInfo.size").value(size))
                .andDo(document("posts/getPostsByBookmark",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("JWT")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지"),
                                parameterWithName("size").description("사이즈")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("data[].clicks").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
                                fieldWithPath("data[].tags").type(JsonFieldType.ARRAY).description("게시글 태그"),
                                fieldWithPath("data[].tags[].tagId").type(JsonFieldType.NUMBER).description("태그 식별자"),
                                fieldWithPath("data[].tags[].name").type(JsonFieldType.STRING).description("태그 이름"),
                                fieldWithPath("data[].files").type(JsonFieldType.ARRAY).description("게시글 파일"),
                                fieldWithPath("data[].files[].id").type(JsonFieldType.NUMBER).description("파일 식별자"),
                                fieldWithPath("data[].files[].originalFileName").type(JsonFieldType.STRING).description("파일 이름"),
                                fieldWithPath("data[].files[].fileSize").type(JsonFieldType.NUMBER).description("파일 이름"),
                                fieldWithPath("data[].files[].localPath").type(JsonFieldType.STRING).description("파일 로컬 경로"),
                                fieldWithPath("data[].files[].remotePath").type(JsonFieldType.STRING).description("파일 리모트 경로"),
                                fieldWithPath("data[].files[].type").type(JsonFieldType.STRING).description("파일 타입"),
                                fieldWithPath("data[].files[].userId").type(JsonFieldType.NUMBER).description("파일 생성자"),
                                fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("게시글 생성일시"),
                                fieldWithPath("data[].lastModifiedAt").type(JsonFieldType.STRING).description("게시글 수정일시"),

                                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지"),
                                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("사이즈"),
                                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 갯수"),
                                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수")
                        )
                ));
    }

}
