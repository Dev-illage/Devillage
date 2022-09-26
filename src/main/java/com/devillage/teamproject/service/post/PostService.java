package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Post savePost(Post post);

    Post editPost(Long id, Post post);

    Post getPost(Long id);

    Bookmark postBookmark(String accessToken, Long postId);

    ReportedPost postReport(String accessToken, Long postId);

    Post postLike(String accessToken, Long postId);

    Page<Post> getPostsByCategory(String category, int page, int size);

    Page<Post> getPostsByBookmark(String accessToken, int page, int size);

    void deletePost();
}
