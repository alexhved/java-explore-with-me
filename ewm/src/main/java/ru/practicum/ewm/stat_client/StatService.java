package ru.practicum.ewm.stat_client;

import client.StatClient;
import dto.RequestStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class StatService {
    public static final String EWM_MAIN_SERVICE = "ewm-main-service";
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatClient statClient;

    public void saveStat(HttpServletRequest servletRequest) {
        RequestStat requestStat = buildRequestStatDto(servletRequest);
        statClient.save(requestStat);

    }

    public static RequestStat buildRequestStatDto(HttpServletRequest servletRequest) {
        RequestStat requestStat = new RequestStat();
        requestStat.setApp(EWM_MAIN_SERVICE);
        requestStat.setUri(servletRequest.getRequestURI());
        requestStat.setIp(servletRequest.getRemoteAddr());
        requestStat.setTimestamp(LocalDateTime.now().format(DTF));
        return requestStat;
    }
}
