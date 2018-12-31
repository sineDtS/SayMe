import com.company.persist.domain.Gender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PswInformation {
    // 123 - psw for michael corleone (user)
    // 123 - psw for илья (user)
    // 123 - psw for евг гаврушов (user)
    // 123 - psw for наташа (user)
    // 1 - psw for андрей дебелый (user)
    // 111 - psw for and vor (user)
    // 7913782o - psw for den str (admin)

    public static void main(String[] args) {
        String encoded=new BCryptPasswordEncoder().encode("7913782o");
        System.out.println(encoded);
    }
}
