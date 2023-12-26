package send.bot.message.entitiy;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    @Column(unique = true,nullable = false)
    private String userName;
    private String firstName;
    private String lastName;
//    private LocalDate  registered_At = LocalDate.now().plusDays(28);
    private boolean payment_verification = true;
    private LocalDate  registered_At = LocalDate.now();




}
