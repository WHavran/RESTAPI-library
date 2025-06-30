package com.library.restapi.demo.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class ControllerLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLogAspect.class);

    @Pointcut("execution(* com.library.restapi.demo.controller..*(..))")
    public void controllerAllCM() {}

    @Pointcut("execution(* com.library.restapi.demo.controller.manager..*(..))")
    public void controllerManagerPack() {}

    @Pointcut("controllerAllCM() && !controllerManagerPack()")
    public void controllerAllWithoutManagerPack(){}

    @Before("controllerAllWithoutManagerPack()")
    public void controllerRequestsLogging(JoinPoint theJoinPoint) {

        String methodName = theJoinPoint.getSignature().toShortString();

        String clientIp = "unknown";
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            clientIp = request.getHeader("X-Forwarded-For");
            if (clientIp == null || clientIp.isEmpty()) {
                clientIp = request.getRemoteAddr();
            } else {
                clientIp = clientIp.split(",")[0].trim();
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName()
                : "Unregistered user";

        String log = String.format("Request:: Method:[%s] By user:[%s] From IP:[%s]", methodName, username, clientIp);
        logger.info(log);
        writeToFile(setLogLineForFile(log));
    }

    @AfterReturning(
            pointcut = "controllerAllWithoutManagerPack()"
    )
    public void controllerResponseLogging(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        String log = String.format("Response:: Method:[%s] Successfully", methodName);
        logger.info(log);
        writeToFile(setLogLineForFile(log));
    }

    @AfterThrowing(
            pointcut = "controllerAllWithoutManagerPack()",
            throwing = "ex"
    )
    public void controllerResponseExceptionLogging(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().toShortString();
        String log = String.format("Response-Exception:: Method:[%s] Returned exception:[%s] Message:[%s]", methodName,
                ex.getClass().getSimpleName(), ex.getMessage());
        logger.error(log);
        writeToFile(setLogLineForFile(log));
    }

    private String setLogLineForFile(String log){
        String dateTimeForLog = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringBuilder logLine = new StringBuilder();
        logLine.append(dateTimeForLog).append(": ");
        logLine.append(log);
        return logLine.toString();
    }

    private void writeToFile(String inputLogLine){

        String dateTimeForName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String folderPath = "logs/";
        String fileNamePrefix = "ControllerLog_";
        String fileNameSuffix = ".txt";
        StringBuilder fullLogFilePath = new StringBuilder();
        fullLogFilePath
                .append(folderPath)
                .append(fileNamePrefix)
                .append(dateTimeForName)
                .append(fileNameSuffix);

        try{
            Path logFilePath = Paths.get(fullLogFilePath.toString());
            Files.writeString(
                    logFilePath,
                    inputLogLine + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
