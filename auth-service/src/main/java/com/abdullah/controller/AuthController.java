package com.abdullah.controller;

import com.abdullah.dto.request.*;
import com.abdullah.dto.response.RegisterResponseDto;
import com.abdullah.repository.entity.Auth;
import com.abdullah.repository.enums.ERole;
import com.abdullah.service.AuthService;
import com.abdullah.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.abdullah.constant.ApiUrls.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;
    private final JwtTokenManager tokenManager;
    private final CacheManager cacheManager;

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(REGISTER+"2")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
            return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean>activateStatus(@RequestBody ActivateRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>>findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping("/createtoken")
    public ResponseEntity<String>createToken(Long id, ERole role){
       return ResponseEntity.ok(tokenManager.createToken(id, role).get());
    }

    @GetMapping("/createtoken2")
    public ResponseEntity<String>createToken2(Long id){
        return ResponseEntity.ok(tokenManager.createToken(id).get());
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getidfromtoken")
    public ResponseEntity<Long>getIdFromToken(String token){
        return ResponseEntity.ok(tokenManager.getIdFromToken(token).get());
    }

    @GetMapping("/getrolefromtoken")

    public ResponseEntity<String>getRoleFromToken(String token){
        return ResponseEntity.ok(tokenManager.getRoleFromToken(token).get());
    }



    @PutMapping("/updateemailorusername")
    public ResponseEntity<Boolean>updateEmailOrUsername(@RequestBody UpdateEmailOrUsernameRequestDto dto){
        return ResponseEntity.ok(authService.updateEmailOrUsername(dto));
    }

    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean>delete(Long id){
        return ResponseEntity.ok(authService.delete(id));
    }

    @GetMapping("/redis")
    @Cacheable(value = "redisexample")
    public String redisExample(String value){
        try {
            Thread.sleep(2000L);
            return value;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/redisdelete")
    @CacheEvict(cacheNames = "redisexample",allEntries = true)
    public void redisDelete(){
    }

    @GetMapping("/redisdelete2")
    public Boolean redisDelete2(){
        try {
            cacheManager.getCache("redisexample").evict("mustafa");//sadece mustafalari silecek
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @GetMapping(FINDBYROLE)
    public ResponseEntity<List<Long>>findByRole(@RequestParam String role){
        return ResponseEntity.ok(authService.findByRole(role));
    }



}
