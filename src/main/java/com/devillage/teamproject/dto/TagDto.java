package com.devillage.teamproject.dto;

import com.devillage.teamproject.entity.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagDto {

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long id;
        private String name;

        public static Response of(Tag tag) {
            return new Response(tag.getId(), tag.getName());
        }
    }
}
