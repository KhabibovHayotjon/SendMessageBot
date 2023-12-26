package send.bot.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import send.bot.message.entitiy.Temporality;
import send.bot.message.entitiy.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemporalityRepository extends JpaRepository<Temporality,Long> {

    List<Temporality> findAllByChatId(Long chatId);
    Temporality findByFirstName(String firstName);
    Temporality findByUserName(String userName);
//    Optional<Temporality> findById(Long id);


}
