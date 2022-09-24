package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.PostTag;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagDto {

    @Getter
    @Builder
    @AllArgsConstructor
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private long tagId;
        private String name;

        public static List<Response> of(Post post){
            List<PostTag> all = post.getTags();
            List<Response> tags = new ArrayList<>();

            for(PostTag tag : all){
                Response dto = Response.builder()
                        .tagId(tag.getTag().getId())
                        .name(tag.getTag().getName())
                        .build();

                tags.add(dto);
            }
            return tags;
        }
    }
}
