package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.post.*;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final ReportedPostRepository reportedPostRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Post savePost(Post post) {
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    @Override
    public Post editPost(Long id,Post post) {
        Post getPost = verifyPost(post.getId());
        getPost.edit(post);
        return getPost;
    }

    @Override
    public Post getPost(Long id) {
        return verifyPost(id);
    }

    @Override
    public Page<Post> getPostsByCategory(String category, int page, int size) {
        try {
            CategoryType.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND);
        }

        return postRepository.findByCategory_CategoryType(
                CategoryType.valueOf(category.toUpperCase()),
                PageRequest.of(page - 1, size, Sort.by("id").descending()));
    }

    @Override
    public void deletePost() {

    }

    @Override
    public Bookmark postBookmark(String accessToken, Long postId) {
        Long userId = jwtTokenUtil.getUserId(accessToken);
        User user = verifyUser(userId);
        Post post = verifyPost(postId);

        List<Bookmark> findBookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId);
        Bookmark bookmark;

        if (!findBookmark.isEmpty()) {
            bookmark = findBookmark.get(0);
            bookmarkRepository.deleteAll(findBookmark);
        } else {
            bookmark = new Bookmark(user, post);
            user.addBookmark(bookmark);
        }

        return bookmark;
    }

    @Override
    public ReportedPost postReport(String accessToken, Long postId) {
        Long userId = jwtTokenUtil.getUserId(accessToken);
        User user = verifyUser(userId);
        Post post = verifyPost(postId);

        List<ReportedPost> findReport = reportedPostRepository.findByUserIdAndPostId(userId, postId);

        if (!findReport.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_REPORTED);
        }

        ReportedPost reportedPost = new ReportedPost(user, post);
        post.addReportedPosts(reportedPost);
        return reportedPost;
    }

    @Override
    public Post postLike(String accessToken, Long postId) {
        Long userId = jwtTokenUtil.getUserId(accessToken);
        User user = verifyUser(userId);
        Post post = verifyPost(postId);

        List<Like> findLikes = likeRepository.findByUserIdAndPostId(userId, postId);
        Long count = likeRepository.countByPostId(postId);

        if (!findLikes.isEmpty()) {
            likeRepository.deleteAll(findLikes);
            count -= 1L;
        } else {
            Like like = new Like(user, post);
            user.addLike(like);
            count += 1L;
        }

        post.setLikeCount(count);
        return post;
    }

    private Post verifyPost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);

        return findPost.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND)
        );
    }

    private User verifyUser(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);

        return findUser.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)
        );
    }
}
