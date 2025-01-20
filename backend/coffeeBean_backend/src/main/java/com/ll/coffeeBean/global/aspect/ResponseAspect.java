package com.ll.coffeeBean.global.aspect;

import com.ll.coffeeBean.global.exceptions.ServiceException;
import com.ll.coffeeBean.global.rsData.RsData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseAspect {
    private final HttpServletResponse response;

    @Around("""
            (
                within
                (
                    @org.springframework.web.bind.annotation.RestController *
                )
                &&
                (
                    @annotation(org.springframework.web.bind.annotation.GetMapping)
                    ||
                    @annotation(org.springframework.web.bind.annotation.PostMapping)
                    ||
                    @annotation(org.springframework.web.bind.annotation.PutMapping)
                    ||
                    @annotation(org.springframework.web.bind.annotation.DeleteMapping)
                    ||
                    @annotation(org.springframework.web.bind.annotation.RequestMapping)
                )
            )
            ||
            @annotation(org.springframework.web.bind.annotation.ResponseBody)
            """)
    public Object handleResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object proceed = joinPoint.proceed();

            if (proceed instanceof RsData<?>) {
                RsData<?> rsData = (RsData<?>) proceed;
                response.setStatus(rsData.getStatusCode());
                log.info("[INFO] Code : {}, Message : {}, Data : {}", rsData.getStatusCode(), rsData.getMsg(), rsData.getData());
            }

            return proceed;
        } catch (ServiceException ex) {
            log.error("[ERROR] Code : {}, Message : {} ", ex.getResultCode(), ex.getMsg());
            throw ex;
        } catch (ResponseStatusException ex) {
            log.error("[ERROR] Code : {}, Message : {} ", ex.getStatusCode().value(), ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("[ERROR] Code : [000], Message : {} ", ex.getMessage());
            throw ex;
        }
    }
}