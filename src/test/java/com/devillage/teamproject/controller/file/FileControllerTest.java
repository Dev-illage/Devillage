package com.devillage.teamproject.controller.file;

import com.devillage.teamproject.entity.File;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.FileType;
import com.devillage.teamproject.security.resolver.ResultJwtArgumentResolver;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.file.FileService;
import com.devillage.teamproject.util.security.SecurityTestConfig;
import com.devillage.teamproject.util.security.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import static com.devillage.teamproject.security.util.JwtConstants.AUTHORIZATION_HEADER;
import static com.devillage.teamproject.util.TestConstants.ID1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FileController.class, ResultJwtArgumentResolver.class})
@WithMockCustomUser
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class FileControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ResultJwtArgumentResolver resultJwtArgumentResolver;
    @MockBean
    FileService fileService;

    @Test
    @DisplayName("postImage")
    public void postImage() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        File file = File.builder().fileSize(1234512L).fileType(FileType.IMAGE).originalFilename("image1.jpg")
                .remotePath("https://www.someserver.com/?q=some-uuid.jpg")
                .localPath("images/20221014/some-uuid.jpg").filename("some-uuid.jpg").id(ID1).owner(user).build();
        MockMultipartFile imageFile = new MockMultipartFile("file", "originalFilename.jpg", MediaType.IMAGE_JPEG_VALUE, "someFile".getBytes());

        given(fileService.saveFile(Mockito.any(), Mockito.any(MultipartFile.class), Mockito.any(StringBuffer.class))).willReturn(file);

        // when
        ResultActions actions = mockMvc.perform(
                multipart("/files")
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(AUTHORIZATION_HEADER, "some-token")
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(file.getId()))
                .andExpect(jsonPath("$.originalFilename").value(file.getOriginalFilename()))
                .andExpect(jsonPath("$.filename").value(file.getFilename()))
                .andExpect(jsonPath("$.fileSize").value(file.getFileSize()))
                .andExpect(jsonPath("$.localPath").value(file.getLocalPath()))
                .andExpect(jsonPath("$.remotePath").value(file.getRemotePath()))
                .andExpect(jsonPath("$.fileType").value(file.getFileType().toString()))
                .andExpect(jsonPath("$.ownerUserId").value(file.getOwner().getId()))
                .andDo(document(
                        "image/post",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER).description("jwt 토큰")
                        ),
                        requestPartBody(
                          "file"
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("파일 식별자"),
                                fieldWithPath("originalFilename").type(JsonFieldType.STRING).description("원본파일 이름"),
                                fieldWithPath("filename").type(JsonFieldType.STRING).description("저장된 파일 이름"),
                                fieldWithPath("fileSize").type(JsonFieldType.NUMBER).description("파일 용량"),
                                fieldWithPath("localPath").description("서버에 저장된 경로"),
                                fieldWithPath("remotePath").description("원격서버에 저장된 경로"),
                                fieldWithPath("fileType").type(JsonFieldType.STRING).description("파일 타입"),
                                fieldWithPath("ownerUserId").type(JsonFieldType.NUMBER).description("파일을 업로드한 사람")
                        )
                ));

    }

    @Test
    @DisplayName("getImage")
    public void getImage() throws Exception {
        // given
        User user = User.builder().id(ID1).build();
        File file = File.builder().fileSize(1234512L).fileType(FileType.IMAGE).originalFilename("image1.jpg")
                .remotePath("https://www.someserver.com/?q=image1.jpg")
                .localPath("init-images/image1.jpg").filename("image1.jpg").id(ID1).owner(user).build();
        given(fileService.findFileWithFilename(Mockito.anyString())).willReturn(file);

        // when
        ResultActions actions = mockMvc.perform(
                get("/files")
                        .param("q", "filename")
                        .accept(MediaType.IMAGE_JPEG)
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "image/get",
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("q").description("찾고자 하는 이미지의 파일 이름")
                        )
                ));
    }

}