package com.devillage.teamproject.service.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.user.BlockRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;

    public UserServiceImpl(UserRepository userRepository, BlockRepository blockRepository) {
        this.userRepository = userRepository;
        this.blockRepository = blockRepository;
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
    public void deleteUser(Long userId) {
        User findUser = findUser(userId);
        findUser.deleteUser();
    }

    @Override
    public Block blockUser(Long srcUserId, Long destUserId) {
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
