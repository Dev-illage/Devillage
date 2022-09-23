package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Like;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.ReportedPost;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    Post savePost();

    Post editPost();

    Post getPost();

    Page<Post> getPosts(String category, int page, int size);

    void deletePost();

    Bookmark postBookmark(Long postId);

    ReportedPost postReport(Long postId);

    Like postLike(Long postId);
}
