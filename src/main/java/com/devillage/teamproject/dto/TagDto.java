package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.PostTag;
import com.devillage.teamproject.entity.Tag;
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

        public static Response of(Tag tag) {
            return new Response(tag.getId(), tag.getName());
        }
    }
}