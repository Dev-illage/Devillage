package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.user.BlockRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(UserRepository userRepository, BlockRepository blockRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.blockRepository = blockRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public User joinUser(User user) {
        return null;
    }

    @Override
    public User findUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    @Override
    public User editUser(User user) {
        return null;
    }

    @Override
    public List<User> findUsers(int page, int size) {
        return null;
    }

    @Override
    public void deleteUser(String token) {
        User findUser = findUser(jwtTokenUtil.getUserId(token));
        findUser.deleteUser();
    }

    @Override
    public Block blockUser(Long destUserId, String token) {
        Long srcUserId = jwtTokenUtil.getUserId(token);
        Optional<Block> optionalBlock = blockRepository.findBySrcUserIdAndDestUserId(srcUserId, destUserId);
        if (optionalBlock.isPresent()) {
            Block block = optionalBlock.get();
            blockRepository.delete(block);
            return block;
        }

        User srcUser = findUser(srcUserId);
        User destUser = findUser(destUserId);
        Block block = Block.builder().srcUser(srcUser).destUser(destUser).build();
        srcUser.addBlock(block);
        return block;
    }
}
