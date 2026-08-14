package com.example.cinema_api.security.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.cinema_api.dto.AuthLoginRequest;
import com.example.cinema_api.dto.AuthResponse;
import com.example.cinema_api.entity.RefreshToken;
import com.example.cinema_api.entity.UserSec;
import com.example.cinema_api.exception.ResourceNotFoundException;
import com.example.cinema_api.repository.RefreshTokenRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

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
                .forEach(permission -> authoritiesList
                        .add(new SimpleGrantedAuthority(permission.getPermissionName())));


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
        String email = authLoginRequest.getEmail();
        String password = authLoginRequest.getPassword();

        Authentication authentication = this.authenticate(email, password);

        //save data en context holder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateAccesToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);


        DecodedJWT decodedJWT = jwtUtils.verifyToken(refreshToken);
        UserSec user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        RefreshToken entity = new RefreshToken();
        entity.setId(decodedJWT.getId()); //UUID que genere en JwtUtils
        entity.setUserSec(user);
        entity.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(entity);


        AuthResponse response = new AuthResponse(email, "Login successful", accessToken,refreshToken, true);
        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        //verifico que el refreshToken sea un jwt valido
        DecodedJWT decodedJWT = jwtUtils.verifyToken(refreshToken);

        //extraigo el "type" que guardamos al crear el token (access/refresh)
        String type = jwtUtils.getSpecificClaim(decodedJWT, "type").asString();
        if(!"refresh".equals(type)) {
            throw  new BadCredentialsException("Invalid token type. expected refresh token");
        }

        //Nuevo
        RefreshToken stored = refreshTokenRepository.findById(decodedJWT.getId())
                .orElseThrow(() -> new BadCredentialsException("refresh token not recognized"));
        if(stored.isRevoked()){
            throw   new BadCredentialsException("Refresh token has been revoked");
        }

        String email = jwtUtils.extractUser(decodedJWT);
        UserDetails userDetails = this.loadUserByUsername(email);//cargo el usuario denuevo desde la base de datos en su estado actual

        //armo un objeto authentication, (no pasa por login con password, porque no lo necesitamos, ya confiamos
        //en el refreshToken validado arriba)
        //el segundo parametro null es la password que no se aplica aqui
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email, null, userDetails.getAuthorities());

        //genero un nuevo access token de accesso al usuario
        String newAccessToken = jwtUtils.generateAccesToken(authentication);


        //el refreshToken original se devuelve igual, no se genera uno nuevo
        return new AuthResponse(email, "Token refreshed", newAccessToken, refreshToken, true);
    }

    public void revokeRefreshToken(String refreshToken) {
        DecodedJWT decodedJWT = jwtUtils.verifyToken(refreshToken);
        RefreshToken stored = refreshTokenRepository.findById(decodedJWT.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);
    }
}
