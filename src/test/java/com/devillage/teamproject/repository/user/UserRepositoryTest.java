package com.devillage.teamproject.repository.user;

import com.devillage.teamproject.entity.Block;
import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.exception.BusinessLogicException;
import com.devillage.teamproject.util.Reflection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest implements Reflection {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Test
    public void saveAndFindById() throws Exception {
        // given
        User user = newInstance(User.class);

        userRepository.save(user);

        // when
        User findUser = userRepository.findById(user.getId()).get();

        // then
        Assertions.assertThat(findUser).isEqualTo(user);
    }

    @Test
    @DisplayName("saveAndFindBlock")
    public void saveAndFindBlock() throws Exception {
        // given
        User srcUser = newInstance(User.class);
        User destUser = newInstance(User.class);
        userRepository.save(srcUser);
        userRepository.save(destUser);
        Block block = Block.builder().srcUser(srcUser).destUser(destUser).build();
        blockRepository.save(block);

        // when
        Optional<Block> optionalBlock = blockRepository.findBySrcUserIdAndDestUserId(srcUser.getId(), destUser.getId());

        // then
        assertTrue(optionalBlock.isPresent());
        assertEquals(block, optionalBlock.get());
    }

    @Test
    @DisplayName("cantBlockTwice")
    public void cantBlockTwice() throws Exception {
        // given
        User srcUser = newInstance(User.class);
        User destUser = newInstance(User.class);
        userRepository.save(srcUser);
        userRepository.save(destUser);
        Block block1 = Block.builder().srcUser(srcUser).destUser(destUser).build();
        blockRepository.save(block1);
        Block block2 = Block.builder().srcUser(srcUser).destUser(destUser).build();

        // when then
        assertThrows(DataIntegrityViolationException.class, () -> blockRepository.save(block2));
    }

}