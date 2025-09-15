package com.hb.cda.springholiday.controller;

import com.hb.cda.springholiday.business.AccountBusiness;
import com.hb.cda.springholiday.controller.dto.SimpleMessageDTO;
import com.hb.cda.springholiday.controller.dto.UpdatePasswordDTO;
import com.hb.cda.springholiday.controller.dto.UserRegisterDTO;
import com.hb.cda.springholiday.controller.dto.mapper.UserMapper;
import com.hb.cda.springholiday.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/account")
public class AccountController {
    private UserMapper mapper;
    private AccountBusiness accountBusiness;
    private AuthenticationManager authenticationManager;

    public AccountController(UserMapper mapper, AccountBusiness accountBusiness) {
        this.mapper = mapper;
        this.accountBusiness = accountBusiness;
    }

/*    @GetMapping("/test")
    public String testMail() {
        mailService.sendEmailValidation(
                new User("test", "test@test.com", "1234", "ROLE_USER", false, null)
        );
        return "Mail sent";
    }*/

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleMessageDTO registerUser (@RequestBody @Valid UserRegisterDTO dto) {
        User user = mapper.convertToEntity(dto);
        accountBusiness.register(user);
        return new SimpleMessageDTO("Check your email please");
    }

    @GetMapping("/validate/{token}")
    public SimpleMessageDTO activate(@PathVariable String token) {
        accountBusiness.activateUser(token);
        return new SimpleMessageDTO("Account activated. Tou can now login");
    }

    @PostMapping("/password/{email}")
    public SimpleMessageDTO resetPassword(@PathVariable String email) {
        accountBusiness.resetPassword(email);
        return new SimpleMessageDTO("Check your email please");
    }

    // User forcément connecté pour faire la demande
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/password")
    public SimpleMessageDTO updatePassword(@RequestBody UpdatePasswordDTO dto, @AuthenticationPrincipal User user) {
        accountBusiness.updatePassword(user, dto.getNewPassword());
        return new SimpleMessageDTO("Password updated");
    }
}
