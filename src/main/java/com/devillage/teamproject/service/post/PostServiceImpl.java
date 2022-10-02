package com.devillage.teamproject.service.post;

import com.devillage.teamproject.entity.*;
import com.devillage.teamproject.entity.enums.CategoryType;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.category.CategoryRepository;
import com.devillage.teamproject.repository.post.BookmarkRepository;
import com.devillage.teamproject.repository.post.LikeRepository;
import com.devillage.teamproject.repository.post.PostRepository;
import com.devillage.teamproject.repository.post.ReportedPostRepository;
import com.devillage.teamproject.repository.posttag.PostTagRepository;
import com.devillage.teamproject.repository.tag.TagRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import com.devillage.teamproject.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReportedPostRepository reportedPostRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;

    @Override
    public Post savePost(Post post, CategoryType categoryType, List<String> tagValue, String token) {
        // 1. 카테고리 엔티티 찾아오기 ( => 포스트 안에 저장해줘야 하니까)
        // 2. 태그 가져오기
        // 2-1. 해당 태그가 기존에 존재한다면 걔를 가져와서 포스트에 넣어줘야하고
        // 2-2. 존재하지 않는다면 -> 새롭게 저장해야된다. -> 포스트 리스트
        // 3. List<포스트> -> 반복문 저장을

        // 문제 : 포스트 태그 하나는 저장 똑바로 안됨
        // 문제 : post에 날짜 시간 저장 안됨
        //

        // 할일 추가 -> 유저도 저장해야됨
        Long userId = jwtTokenUtil.getUserId(token);
        User findUser = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Category category = categoryRepository.findCategoriesByCategoryType(categoryType);

        tagValue.forEach(
                e -> {
                    if (tagRepository.findTagByName(e).isEmpty()) {
                        Tag tag = tagRepository.save(new Tag(e));
                        PostTag postTag = new PostTag(post, tag);
                        postTagRepository.save(postTag);
                        post.addPostTag(postTag);
                        post.addCategory(category);
                        postRepository.save(post);
                        findUser.addPost(post);
                        post.addUser(findUser);
                    } else {
                        Tag tag = tagRepository.findTagByName(e).orElseThrow(IllegalArgumentException::new);
                        PostTag postTag = new PostTag(post, tag);
                        postTagRepository.save(postTag);
                        post.addPostTag(postTag);
                        post.addCategory(category);
                        postRepository.save(post);
                        findUser.addPost(post);
                        post.addUser(findUser);
                    }
                }
        );

        return post;
    }

    @Override
    public Post editPost(Long id, Post post) {
        Post getPost = findVerifyPost(post.getId());
        getPost.edit(post);
        return getPost;
    }

    @Override
    public Post getPost(Long id) {
        return findVerifyPost(id);
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
    public Page<Post> getPostsBySearch(String word, int page, int size) {
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                word, word, PageRequest.of(page - 1, size, Sort.by("id").descending()));
    }

    @Override
    public Page<Post> getPostsByBookmark(Long userId, int page, int size) {
        User user = userService.findVerifiedUser(userId);

        List<Post> postsList = user.getBookmarks()
                .stream()
                .sorted(Comparator.comparing(bookmark -> -bookmark.getId()))
                .map(Bookmark::getPost)
                .collect(Collectors.toList());

        int start = (page - 1) * size;
        int end = Math.min(start + size, postsList.size());

        return new PageImpl<>(postsList.subList(start, end),
                PageRequest.of(page - 1, size),
                postsList.size());
    }

    @Override
    public Bookmark postBookmark(Long userId, Long postId) {
        User user = userService.findVerifiedUser(userId);
        Post post = findVerifyPost(postId);

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
    public ReportedPost postReport(Long userId, Long postId) {
        User user = userService.findVerifiedUser(userId);
        Post post = findVerifyPost(postId);

        List<ReportedPost> findReport = reportedPostRepository.findByUserIdAndPostId(userId, postId);

        if (!findReport.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_REPORTED);
        }

        ReportedPost reportedPost = new ReportedPost(user, post);
        post.addReportedPosts(reportedPost);
        return reportedPost;
    }

    @Override
    public Post postLike(Long userId, Long postId) {
        User user = userService.findVerifiedUser(userId);
        Post post = findVerifyPost(postId);

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

    private Post findVerifyPost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);

        return findPost.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND)
        );
    }

}
