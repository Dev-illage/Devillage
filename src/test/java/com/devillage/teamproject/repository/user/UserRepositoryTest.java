package com.devillage.teamproject.repository.user;

import com.devillage.teamproject.entity.User;
import com.devillage.teamproject.util.Reflection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest implements Reflection {

    @Autowired
    private UserRepository userRepository;

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

}