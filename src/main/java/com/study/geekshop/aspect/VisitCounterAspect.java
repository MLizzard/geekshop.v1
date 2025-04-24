package com.study.geekshop.aspect;

import com.study.geekshop.service.CounterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class VisitCounterAspect {

    private final CounterService counterService;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void anyRestController() {}

    @AfterReturning("anyRestController()")
    public void countRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            String uri = request.getRequestURI();

            if (!uri.equals("/visits/count")) {
                counterService.increment(uri);
            }
        }
    }

}