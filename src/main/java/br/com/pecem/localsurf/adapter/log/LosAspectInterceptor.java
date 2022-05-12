package br.com.pecem.localsurf.adapter.log;

import br.com.pecem.localsurf.domain.model.LogEvento;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Aspect
@Component
public class LosAspectInterceptor {

    @Around("@annotation(logEventoAnnotation)")
    public Object intercept(final ProceedingJoinPoint pjp,final LogEvento logEventoAnnotation) throws Throwable {
        final CodeSignature signature = (CodeSignature) pjp.getSignature();
        final String name = StringUtils.hasText(logEventoAnnotation.value()) ? logEventoAnnotation.value() : signature.getName();
        final Logger log = LoggerFactory.getLogger(signature.getDeclaringType());
        final Suporte suporte = new Suporte();
        final Map<String,Object> argumentos = criarArgumentos(signature.getParameterNames(),pjp.getArgs());
        final LogEstruturaEvento logEvento = new LogEstruturaEvento();
        logEvento.evento("metodo", name,name);
        logEvento.evento("argumentos",argumentos, null);
        if (logEventoAnnotation.logTime()){
            final Instant start = Instant.now();
            logEvento.evento("duracao",null,() -> Duration.between(start,Instant.now()).toMillis());
        }
        logEvento.evento("payload",null,() -> suporte.getValue());

        try{
            log.info("[START]",logEvento.start());
            final Object processo = pjp.proceed();
            suporte.setValue(processo);
            return processo;
        }finally {
            log.info("[END]", logEvento.end());
        }
    }

    private Map<String, Object> criarArgumentos(String[] parameterNames, Object[] args) {
        final Map<String,Object> map = new HashMap<>();
        for (int i =0; i < parameterNames.length;i++){
            map.put(parameterNames[i],args[i]);
        }
        return map;
    }

    @Getter
    @Setter
    private static class Suporte{
        private Object value;
    }
}
