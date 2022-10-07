package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import com.devillage.teamproject.entity.enums.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    Post savePost(Post post, CategoryType categoryType, List<String> tags, Long userId);

    Post editPost(Post post, CategoryType categoryType, List<String> tags, Long userId,Long postId);

    Post getPost(Long userId);

    Bookmark postBookmark(Long userId, Long postId);

    ReportedPost postReport(Long userId, Long postId);

    Post postLike(Long userId, Long postId);

    Page<Post> getPostsByCategory(String category, int page, int size);

    Page<Post> getPostsBySearch(String word, int page, int size);

    Page<Post> getPostsByBookmark(Long userId, int page, int size);

    void deletePost(Long postId);

    Post findVerifyPost(Long postId);
}
