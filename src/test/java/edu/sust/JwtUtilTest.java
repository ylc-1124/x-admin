package edu.sust;

import edu.sust.common.utils.JwtUtil;
import edu.sust.sys.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt() {
        User user = new User();
        user.setUsername("zhangsan");
        user.setPhone("123456789");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
    }

    @Test
    public void testParseJwt() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwODUwOGUyZS0zNTI3LTQ1ZGUtYjM1MS1mZTQzNWE4MDMxOGQiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzNDU2Nzg5XCIsXCJ1c2VybmFtZVwiOlwiemhhbmdzYW5cIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2ODEyODk4NzEsImV4cCI6MTY4MTI5MTY3MX0.pezxJms_ga0lWl1OFA-cEKr56KJdX2SivL6gZzeS5AA";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }
    @Test
    public void testParseJwt2() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwODUwOGUyZS0zNTI3LTQ1ZGUtYjM1MS1mZTQzNWE4MDMxOGQiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTIzNDU2Nzg5XCIsXCJ1c2VybmFtZVwiOlwiemhhbmdzYW5cIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2ODEyODk4NzEsImV4cCI6MTY4MTI5MTY3MX0.pezxJms_ga0lWl1OFA-cEKr56KJdX2SivL6gZzeS5AA";
        User user = jwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }
}
