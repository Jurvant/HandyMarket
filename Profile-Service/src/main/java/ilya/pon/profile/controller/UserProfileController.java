package ilya.pon.profile.controller;

import ilya.pon.profile.repository.UserProfileRepository;
import ilya.pon.profile.service.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;
}
