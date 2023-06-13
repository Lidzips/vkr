package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.resource.UserResource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final UserRepository userRepository;

    private static byte [] secretKey;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    UserResource get(@PathVariable Integer id) {
        User entity = userRepository.select(id);
        if (entity == null) return null;
        return new UserResource(entity);
    }

    @GetMapping("/id")
    public ResponseEntity<User> getByLogin(@RequestParam("login") String login) {
        User result = userRepository.selectByLogin(login);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    UserResource[] getAll() {
        User[] users;
        users = userRepository.select();
        return Arrays.stream(users)
                .map(entity -> {
                    return new UserResource(entity);
                })
                .toArray(UserResource[]::new);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    UserResource post(@RequestBody UserResource resource) {
        User entity = userRepository.insert(resource.toEntity());
        if (entity == null) return null;
        return new UserResource(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    UserResource put(@PathVariable Integer id,
                         @RequestBody UserResource resource) {
        User entity = userRepository.update(id, resource.toEntity());
        if (entity == null) return null;
        return new UserResource(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    UserResource delete(@PathVariable Integer id) {
        User entity = userRepository.delete(id);
        if (entity == null) return null;
        return new UserResource(entity);
    }

    private String generateAccessToken(String login) {
        // Ваш секретный ключ для подписи токена
        if (secretKey == null) {
            secretKey = SecretKeyGenerator.generateSecretKey(32);
        }

        // Время истечения токена доступа (например, 1 час)
        long expirationTimeMillis = System.currentTimeMillis() + (60 * 60 * 1000); // 1 hour

        // Генерация токена доступа с использованием библиотеки jjwt
        String accessToken = Jwts.builder()
                .claim("login", login) // Установка субъекта (в данном случае - логина пользователя)
                .setExpiration(new Date(expirationTimeMillis)) // Установка времени истечения
                .signWith(SignatureAlgorithm.HS256, secretKey) // Подпись токена с использованием секретного ключа и алгоритма HS256
                .compact();

        return accessToken;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Получение логина и пароля из объекта LoginRequest
        String login = loginRequest.getLogin();
        String password = loginRequest.getPassword();

        // Выполнение проверки логина и пароля на сервере
        User user = userRepository.selectByLogin(login);
        if (user == null || !password.equals(user.getPassword())) {
            // Если пользователь не найден или пароли не совпадают - возвращаем Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Если проверка прошла успешно, можно создать токен доступа
        String accessToken = generateAccessToken(login);

        // Сохранение токена в базе данных
        user.setAccessToken(accessToken);
        userRepository.updateAccessToken(user.getLogin(), accessToken);

        // Создание объекта LoginResponse с токеном доступа и дополнительными данными о пользователе
        LoginResponse loginResponse = new LoginResponse(user.getId(), user.getLogin(), user.getIsAdmin(), accessToken, user.getPoints());

        // Вернуть объект LoginResponse вместе с ответом
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/restore-session")
    public ResponseEntity<?> restoreSession(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Извлечь токен из заголовка Authorization
            String token = authorizationHeader.substring("Bearer ".length());

            // Проверить и валидировать токен
            boolean isTokenValid = validateToken(token);

            // Получить идентификатор пользователя из токена
            String login = extractLoginFromToken(token);

            // Проверить наличие пользователя с указанным идентификатором в базе данных
            User user = userRepository.selectByLogin(login);
            if (user == null || !isTokenValid) {
                // Пользователь не найден, вернуть ошибку
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Создание объекта LoginResponse с токеном доступа и дополнительными данными о пользователе
            LoginResponse loginResponse = new LoginResponse(user.getId(), user.getLogin(), user.getIsAdmin(), token, user.getPoints());

            // Вернуть успешный ответ
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        // Удалить токен доступа из хранилища
        userRepository.removeAccessToken(logoutRequest.getLogin());

        // Вернуть ответ об успешном выходе пользователя
        return ResponseEntity.ok(new LogoutResponse("User logged out successfully"));
    }

    public static class SecretKeyGenerator {
        public static byte[] generateSecretKey(int keySize) {
            SecureRandom secureRandom = new SecureRandom();
            byte[] key = new byte[keySize];
            secureRandom.nextBytes(key);
            return key;
        }

        public static String byteArrayToHexString(byte[] byteArray) {
            StringBuilder sb = new StringBuilder();
            for (byte b : byteArray) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

    private boolean validateToken(String token) {
        try {
            // Проверка подписи и валидация токена
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Проверяем срок действия токена
            Date expirationDate = claims.getExpiration(); // Получение времени истечения токена
            Date currentDate = new Date(); // Текущая дата и время
            if (expirationDate != null && currentDate.after(expirationDate)) {
                return false;
            }
            return true; // Токен валиден
        } catch (Exception e) {
            // Токен недействительный или произошла ошибка при проверке
            return false;
        }
    }

    private String extractLoginFromToken(String token) {
        // Разбор токена и получение данных
        Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        Claims claims = jws.getBody();

        // Извлечение идентификатора пользователя из токена
        String userId = claims.get("login", String.class);

        return userId;
    }

    public static class LoginRequest {
        private String login;
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {

        private Integer id;

        private String login;
        private Boolean isAdmin;
        private String accessToken;

        private Integer points;

        public LoginResponse(Integer id, String login, Boolean isAdmin, String accessToken, Integer points) {
            this.id = id;
            this.login = login;
            this.isAdmin = isAdmin;
            this.accessToken = accessToken;
            this.points = points;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public Boolean getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Boolean admin) {
            isAdmin = admin;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }
    }

    public static class LogoutRequest {
        private String accessToken;
        private String login;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }
    }

    public static class LogoutResponse {
        private String message;

        public LogoutResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
