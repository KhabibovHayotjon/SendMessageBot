package send.bot.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import send.bot.message.entitiy.Users;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    @Query(value = "select * from Users where registered_At >= :date",nativeQuery = true)
    List<Users> findAllRegistredDateIsEquals(LocalDate date);

    List<Users> findAllByChatId(Long chatId);
     Optional<Users> findByChatId(Long chatId);
    Users findByUserName(String userName);
    Users findAllByFirstName(String firstName);
    Optional<Users> findById(Long id);


//    List<Users> findAllRegistredDateIsEquals(LocalDate date);
}
