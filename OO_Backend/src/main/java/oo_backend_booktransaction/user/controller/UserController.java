package oo_backend_booktransaction.user.controller;

import lombok.RequiredArgsConstructor;
import oo_backend_booktransaction.user.dto.LoginRequestDto;
import oo_backend_booktransaction.user.dto.LoginResponseDto;
import oo_backend_booktransaction.user.dto.SignupRequestDto;
import oo_backend_booktransaction.user.dto.SignupResponseDto;
import oo_backend_booktransaction.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto dto) {
        return userService.signup(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto dto) {
        return userService.login(dto);
    }
}
