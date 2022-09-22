package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.Bookmark;
import com.devillage.teamproject.entity.Post;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.post.BookmarkRepository;
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
    private final BookmarkRepository bookmarkRepository;

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
        Optional<Post> findPost = postRepository.findById(postId);
        Optional<User> findUser = userRepository.findById(userId);
        List<Bookmark> findBookmark = bookmarkRepository.findByPostIdAndUserId(postId, userId);

        if (!findBookmark.isEmpty()) {
            bookmarkRepository.delete(findBookmark.get(0));
            return findBookmark.get(0);
        }

        Post post = findPost.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND)
        );
        User user = findUser.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
        );

        Bookmark bookmark = new Bookmark(user, post);
        user.addBookmark(bookmark);
        return bookmark;
    }
}
