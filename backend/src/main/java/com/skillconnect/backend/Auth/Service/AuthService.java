package com.skillconnect.backend.Auth.Service;

import com.skillconnect.backend.Auth.DTO.AppUserDetails;
import com.skillconnect.backend.Auth.DTO.AuthResponse;
import com.skillconnect.backend.Auth.DTO.LoginRequest;
import com.skillconnect.backend.Auth.DTO.RegistrationRequest;
import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.Auth.Entity.Role;
import com.skillconnect.backend.Auth.Repository.AppUserRepository;
import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Freelancer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ApiResponse<AuthResponse> login(LoginRequest request) {

        // authenticating using the manager, currently Auth obj is unauthenticated
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // if we reach here, authentication was successful and Auth obj is authenticated
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        AppUserDetails appUserDetails = (AppUserDetails) userDetails;

        return ApiResponse.success(createAuthResponse(appUserDetails, token));
    }

    public ApiResponse<AuthResponse> registerClient(RegistrationRequest request) {
        return register(request, Role.ROLE_CLIENT);
    }

    public ApiResponse<AuthResponse> registerFreelancer(RegistrationRequest request) {
        return register(request, Role.ROLE_FREELANCER);
    }

    private ApiResponse<AuthResponse> register(RegistrationRequest request, Role role) {

        if (appUserRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("user already exists");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setPassword(passwordEncoder.encode(request.getPassword()));
        appUser.setRole(role);

        if (role == Role.ROLE_CLIENT) {

            Client clientProfile = new Client();
            clientProfile.setName(request.getName());
            clientProfile.setAppUser(appUser);
            appUser.setClientProfile(clientProfile);
        } else if (role == Role.ROLE_FREELANCER) {

            Freelancer freelancerProfile = new Freelancer();
            freelancerProfile.setName(request.getName());
            freelancerProfile.setAppUser(appUser);
            appUser.setFreelancerProfile(freelancerProfile);
        }

        AppUser savedUser = appUserRepository.save(appUser);
        AppUserDetails appUserDetails = new AppUserDetails(savedUser);

        // generate a token for immediate login
        String token = jwtService.generateToken(new AppUserDetails(savedUser));

        return ApiResponse.success(createAuthResponse(appUserDetails, token));
    }

    private AuthResponse createAuthResponse(AppUserDetails userDetails, String token) {
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("UNKNOWN_ROLE");

        return AuthResponse.builder()
                .name(userDetails.getName())
                .email(userDetails.getUsername())
                .role(role)
                .token(token)
                .build();
    }
}
