package com.example.cinema_api.security.service;

import com.example.cinema_api.dto.AuthLoginRequest;
import com.example.cinema_api.dto.AuthResponse;
import com.example.cinema_api.entity.UserSec;
import com.example.cinema_api.repository.UserRepository;
import com.example.cinema_api.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserSec user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //creamos una lista donde guardaremos permisos y roles
        List<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();

        //obtenemos los roles y los convierto en un simplegrantedauthority
        user.getRoles().forEach(role -> authoritiesList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));


        /**
         * traer permisos y convertirlos en SimpleGrantedAuthority
         * a la lista de roles lo transformo en un stream para usar flatmap que permite hacer un mapeo de lista de permisos
         * que tienen el rol y al mismo tiempo las transformo a un stream para usar el forEach
         * que por cada permiso que encuentre lo agregue al authorityList haciendo una conversion a
         * SimpleGrantedAuthority apartir del nombre del permiso que trae
         * **/
        user.getRoles().stream().flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authoritiesList.add(new SimpleGrantedAuthority(permission.getPermissionName())));


        //retorno el usuario en formato user de spring security
        return new User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                authoritiesList
        );
    }

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = this.loadUserByUsername(email);

        if(userDetails == null) {
            throw new UsernameNotFoundException("Invalid email or password.");
        }else if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw  new BadCredentialsException("Incorrect password.");
        }
        //Remplace userDetails.getPassword() por null para no pasar hashes
        return new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(email, password);

        //save data en context holder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse response = new AuthResponse(email, "Login successful", accessToken, true);
        return response;
    }
}
