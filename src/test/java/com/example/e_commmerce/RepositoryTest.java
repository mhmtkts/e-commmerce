package com.example.e_commmerce;

import com.example.e_commmerce.entity.Cart;
import com.example.e_commmerce.entity.Role;
import com.example.e_commmerce.entity.User;
import com.example.e_commmerce.repository.CartRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
@Transactional
@Rollback
class RepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    private String uniqueEmail;

    @BeforeEach
    void setUp() {
        uniqueEmail = "test" + UUID.randomUUID().toString() + "@example.com";
    }

    @Test
    void testFindByUserId() {
        EntityManager em = entityManager.getEntityManager();

        // Mevcut "USER" rolünü bul veya oluştur
        Role role = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", "USER")
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    entityManager.persist(newRole);
                    return newRole;
                });

        // Kullanıcıyı bul veya oluştur
        User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", uniqueEmail)
                .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(uniqueEmail);
                    newUser.setPassword("password123");
                    newUser.setRole(role);
                    entityManager.persist(newUser);
                    return newUser;
                });

        // Kullanıcı için bir Sepet oluştur ve kaydet
        Cart cart = new Cart(user);
        entityManager.persist(cart);

        entityManager.flush();

        // CartRepository'yi test et
        Optional<Cart> found = cartRepository.findByUserId(user.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getId(), found.get().getUser().getId());
    }
    @Test
    void testFindByUserIdNotFound() {
        Optional<Cart> found = cartRepository.findByUserId(999L);

        assertFalse(found.isPresent());
    }
}