package com.example.cinema_api.scheduler;

import com.example.cinema_api.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupJob {
    private final RefreshTokenRepository refreshTokenRepository;
    /**Tenemos una tarea automatizada, va eliminar tokens expirados cada cierto tiempo, maneja formato 24horas
     * segundo, minuto, hora, dia del mes, mes, dia de la semana
     * asterisco(*) significa que no importa o aplica siempre
     *
     *
     * Cada 30 min en el dia seria asi 0 /30 * * * *
     * Cada lunes 0 0 3 * * MON  o convinar dias seria MON,THU
     *
     * **/

    //Todos los dias a las 3am hacemos esta tarea
    @Scheduled(cron = "0 0 3 * * *")
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}
