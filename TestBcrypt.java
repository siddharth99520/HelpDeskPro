import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBcrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Matches: " + encoder.matches("password", "$2a$10$wE.VwVb/8xV1qY4oH1N2P.V8r5p3L6Fz3yM/O5Qz/e9m/v5M2bM9i"));
    }
}
