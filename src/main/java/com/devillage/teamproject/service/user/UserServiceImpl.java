package com.devillage.teamproject.service.user;

import com.devillage.teamproject.dto.AuthDto;
import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.Like;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.entity.enums.UserStatus;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.exception.ExceptionCode;
import com.devillage.teamproject.repository.post.LikeRepository;
import com.devillage.teamproject.repository.user.BlockRepository;
import com.devillage.teamproject.repository.user.UserRepository;
import com.devillage.teamproject.security.resolver.AccessToken;
import com.devillage.teamproject.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BlockRepository blockRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User joinUser(User user) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(String token) {
        return findVerifiedUser(jwtTokenUtil.getUserId(token));
    }

    @Override
    public void editUser(Long userId, String nickName, String statusMessage) {
        Optional<User> optionalUser = userRepository.findById(userId);

        User user = optionalUser.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        if (nickName != null) {
            if (userRepository.existsByNickName(nickName)) {
                throw new BusinessLogicException(ExceptionCode.NICKNAME_ALREADY_EXISTS);
            }
            user.setNickName(nickName);
        }

        if (statusMessage != null) {
            user.setStatusMessage(statusMessage);
        }
    }

    @Override
    public List<User> findUsers(int page, int size) {
        return null;
    }

    @Override
    public void deleteUser(String token) {
        User findUser = findVerifiedUser(jwtTokenUtil.getUserId(token));
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

        User srcUser = findVerifiedUser(srcUserId);
        User destUser = findVerifiedUser(destUserId);
        Block block = Block.builder().srcUser(srcUser).destUser(destUser).build();
        srcUser.addBlock(block);
        return block;
    }

    @Transactional
    @Override
    public boolean updatePassword(AuthDto.UserInfo userInfo,String password, String updatePassword){
        String validPassword =  validatePassword(updatePassword);
        User user = findVerifiedUser(userInfo.getId());
        checkUserPassword(userInfo.getId(),password,userInfo.getId());

        if(user.getOauthProvider()!=null){
            throw new BusinessLogicException(ExceptionCode.CAN_NOT_UPDATE_PASSWORD);
        }
        user.updatePassword(passwordEncoder,validPassword);
        System.out.println(user.getPassword());
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public User findVerifiedUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        if (findUser.getUserStatus() == UserStatus.RESIGNED) {
            throw new BusinessLogicException(ExceptionCode.USER_RESIGNED);
        }
        return findUser;
    }

    @Override
    public Long checkUserPassword(Long id, String password, Long tokenId) {
        if (!Objects.equals(id, tokenId)) throw new IllegalArgumentException("잘못된 요청");

        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        if (!findUser.passwordVerification(passwordEncoder, password)) {
            throw new IllegalArgumentException("잘못된 패스워드");
        }

        return findUser.getId();
    }


    public String validatePassword(String password){
        Pattern passPattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher = passPattern.matcher(password);

        if(!passMatcher.matches()){
            throw new BusinessLogicException(ExceptionCode.NOT_VALID_PASSWORD);
        }
        return password;
    }

}
