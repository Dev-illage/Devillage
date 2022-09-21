package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post savePost() {
        return null;
    }

    @Override
    public Post editPost() {
        return null;
    }

    @Override
    public Post getPost() {
        return null;
    }

    @Override
    public List<Post> getPosts() {
        return null;
    }

    @Override
    public void deletePost() {

    }

    @Override
    public Bookmark postBookmark(Long postId) {
        Long userId = 1L; // Security 메서드 구현 필요
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(userId);

        if (post.isEmpty()) throw new BusinessLogicException(ExceptionCode.POST_NOT_FOUND);
        if (user.isEmpty()) throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);

        Bookmark bookmark = new Bookmark(user.get(), post.get());
        user.get().addBookmark(bookmark);
        return bookmark;
    }
}
