package ru.mts.aggregatorservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.mts.aggregatorservice.exception.MissingAuthorizationHeaderException;
import ru.mts.aggregatorservice.exception.UnauthorizedAccessException;
import ru.mts.aggregatorservice.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Применяет фильтрацию аутентификации к запросам, проходящим через Aggregator Service (Spring Cloud Gateway).
     * Проверяет наличие и валидность JWT токена в заголовках запроса.
     *
     * @param config Конфигурационный объект, используемый для настройки поведения фильтра.
     * @return {@link GatewayFilter} для дальнейшей обработки запроса.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Содержит ли заголовок токен или нет
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new MissingAuthorizationHeaderException(
                            "AUTH_HEADER_ABSENCE",
                            "Отсутствует заголовок авторизации");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    throw new UnauthorizedAccessException(
                            "UNAUTHORIZED_LOGIN_ATTEMPT",
                            "Попытка неавторизованного входа в приложение");
                }
            }
            return chain.filter(exchange);
        });
    }

    /**
     * Внутренний статический класс {@code Config} используется для конфигурации фильтра.
     * Не содержит дополнительных полей или методов.
     */
    public static class Config {
    }
}
