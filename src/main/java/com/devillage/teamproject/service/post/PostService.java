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
    Post savePost(Post post, CategoryType categoryType, List<String> tags, String token);

    Post editPost(Post post, CategoryType categoryType, List<String> tags, String token,Long id);

    Post getPost(Long id);

    Bookmark postBookmark(String accessToken, Long postId);

    ReportedPost postReport(String accessToken, Long postId);

    Post postLike(String accessToken, Long postId);

    Page<Post> getPostsByCategory(String category, int page, int size);

    Page<Post> getPostsBySearch(String word, int page, int size);

    Page<Post> getPostsByBookmark(String accessToken, int page, int size);

    void deletePost(Long postId);
}
