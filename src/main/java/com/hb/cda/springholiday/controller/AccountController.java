package com.hb.cda.springholiday.controller;

import com.hb.cda.springholiday.business.AccountBusiness;
import com.hb.cda.springholiday.controller.dto.UpdatePasswordDTO;
import com.hb.cda.springholiday.controller.dto.UserRegisterDTO;
import com.hb.cda.springholiday.controller.dto.mapper.UserMapper;
import com.hb.cda.springholiday.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public String registerUser (@RequestBody @Valid UserRegisterDTO dto) {
        User user = mapper.convertToUser(dto);
        accountBusiness.register(user);
        return "Check your email please";
    }

    @GetMapping("/validate/{token}")
    public String activate(@PathVariable String token) {
        accountBusiness.activateUser(token);
        return "Account activated. Tou can now login";
    }

    @PostMapping("/password/{email}")
    public String resetPassword(@PathVariable String email) {
        accountBusiness.resetPassword(email);
        return "Check your email please";
    }

    @PatchMapping("/password")
    public String updatePassword(@RequestBody UpdatePasswordDTO dto, @AuthenticationPrincipal User user) {
        accountBusiness.updatePassword(user, dto.getNewPassword());
        return "Password updated";
    }
}
