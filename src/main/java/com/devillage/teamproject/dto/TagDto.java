package com.devillage.teamproject.dto;

<<<<<<< HEAD
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.PostTag;
import com.devillage.teamproject.entity.Tag;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
=======
import com.devillage.teamproject.entity.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
>>>>>>> origin/main

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagDto {

    @Getter
<<<<<<< HEAD
    @Builder
    @AllArgsConstructor
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private long tagId;
=======
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long id;
>>>>>>> origin/main
        private String name;

        public static Response of(Tag tag) {
            return new Response(tag.getId(), tag.getName());
        }
<<<<<<< HEAD

//        public static List<Response> of(Post post){
//            List<PostTag> all = post.getTags();
//            List<Response> tags = new ArrayList<>();
//
//            for(PostTag tag : all){
//                Response dto = Response.builder()
//                        .tagId(tag.getTag().getId())
//                        .name(tag.getTag().getName())
//                        .build();
//
//                tags.add(dto);
//            }
//            return tags;
//        }
=======
>>>>>>> origin/main
    }
}
