import com.company.persist.domain.Gender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class asd {
    // 123 - psw for илья (user)
    // 123 - psw for евг гаврушов (user)
    // 123 - psw for наташа (user)
    // 1 - psw for андрей дебелый (user)
    // 111 - psw for and vor (user)
    // 7913782o - psw for den str (admin)

    public static void main(String[] args) {
        String encoded=new BCryptPasswordEncoder(11).encode("111");
        System.out.println(encoded);
    }
}
