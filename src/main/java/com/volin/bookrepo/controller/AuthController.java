package com.volin.bookrepo.controller;

import com.volin.bookrepo.exceptions.AppException;
import com.volin.bookrepo.model.Role;
import com.volin.bookrepo.model.RoleName;
import com.volin.bookrepo.model.User;
import com.volin.bookrepo.payload.ApiResponse;
import com.volin.bookrepo.payload.JwtAuthenticationResponse;
import com.volin.bookrepo.payload.LoginRequest;
import com.volin.bookrepo.payload.SignUpRequest;
import com.volin.bookrepo.repositories.RoleRepository;
import com.volin.bookrepo.repositories.UserRepository;
import com.volin.bookrepo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

/**
 * Методы класса выполняют проверку аутентификацию пользователя и авторизацию
 * его доступа к ресурсам приложения
 * @RestController Указывает, что контроллер является REST, это означает, 
 * что ответы будут записаны в теле ответа, а не в визуализированном шаблоне.
 * @RequestMapping Указывает URL, который будет обрабатывать контроллер
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;


    /**
     * Метод, который выполняет идентификацию и аутентификацию пользователя.
     *  - сохраняет аутентификационную информацию пользователя в контексте безопасности 
     *  - возвращает JWT токен 
     *
     * @param loginRequest
     *
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    /**
     * Метод сохраняет регистрационные данные пользователя 
     *
     * @param signUpRequest
     *
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    	
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Создание учетной записи пользователя
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        // сохраняем в ней пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        // ... роли
        user.setRoles(Collections.singleton(userRole));
        
        // сохраняем пользователя и возвращаем его с присвоенным идентификатором
        User result = userRepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        ResponseEntity<?> re = ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
        //System.out.println("ResponseEntity" + re.toString());
        return re;
    }

}
