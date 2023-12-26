package send.bot.message.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import send.bot.message.entitiy.Users;
import send.bot.message.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Component
public class SchedulerTest {
    @Autowired
    UserRepository userRepository;

    public void Test( ){
//        userRepository.findAllRegistredDateIsEquals(LocalDate.now());
        List<Users> userRepositories = userRepository.findAllRegistredDateIsEquals(LocalDate.now());

        for (Users users : userRepositories) {
            if (users.getRegistered_At().equals(LocalDate.now())) {
                System.out.println("Users111 " + users);
            }else {
                System.out.println("no");
            }

        }


//        if (userRepositories.equals(LocalDate.now())){
//        }

    }
    public String PayDay(Long chtId){
//        List<Users> userRepositories = userRepository.findAllRegistredDateIsEquals(LocalDate.now());
         List<Users> usersList = userRepository.findAllByChatId(chtId);
        for (Users users : usersList) {
            if (users.getUserName()==null){
                return "userName tanlab keyin urinib ko'ring";
            }else {
                System.out.println("Users22 " + users.getRegistered_At());
                return "firstName " + users.getFirstName() + " payDay " + users.getRegistered_At();
            }

        }

        return "Ro'yxatdan o'tmagansiz";
    }


    }



