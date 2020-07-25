import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityTest {

    public static void main(String[] args) {

        //1.创建一个BCryptPasswordEncoder对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //2.准备明文字符串
        String rawPassword = "123123";

        //3.加密
        String encode = passwordEncoder.encode(rawPassword);
        System.out.println(encode);

    }
}

class EncodeTest {
    public static void main(String[] args) {

        //1.准备明文字符串
        String rawPassword = "123123";

        //2.准备密文字符串
        String encodedPassword = "$2a$10$L9xCIZ8kA9EJNVYHIGPQ8.FuC5Xz/uBtZvI64BbU//sRAXRYatAxK";

        //3.创建BCryptPasswordEncoder对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //4.比较
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        System.out.println(matches);
    }
}
