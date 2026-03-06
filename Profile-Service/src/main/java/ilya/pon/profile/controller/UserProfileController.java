package ilya.pon.profile.controller;

import ilya.pon.profile.dto.LoginDto;
import ilya.pon.profile.dto.RegisterDto;
import ilya.pon.profile.dto.ResponseLoginDto;
import ilya.pon.profile.service.LoginService;
import ilya.pon.profile.service.RegisterService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("profile-service/api/v1")
public class UserProfileController {
    private final RegisterService registerService;
    private final LoginService loginService;

    @PostMapping("/register")
    public void registerNewProfile(@RequestBody RegisterDto dto) {
        registerService.register(dto);
    }

    @GetMapping("/login")
    public ResponseLoginDto login(@RequestBody LoginDto loginDto) {
        return loginService.login(loginDto);
    }
}