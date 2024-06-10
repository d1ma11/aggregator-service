package ru.mts.aggregatorservice.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    /**
     * Список разрешенных endpoint'ов
     */
    public static final List<String> openApiEndpoints = List.of(
            "/customer/login"
    );

    /**
     * Предикат, определяющий, является ли запрос защищенным.
     * <p>
     * Используется для сортировки защищенных endpoint'ов от открытых endpoint'ов
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
