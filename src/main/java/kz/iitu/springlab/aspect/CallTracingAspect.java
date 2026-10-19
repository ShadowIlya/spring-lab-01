package kz.iitu.springlab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CallTracingAspect {

    private static final Logger log = LoggerFactory.getLogger(CallTracingAspect.class);

    // Переменная для хранения текущей глубины вложенности для каждого потока
    private static final ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);

    // Срабатывает перед выполнением метода сервиса
    @Before("kz.iitu.springlab.aspect.Pointcuts.serviceLayer()")
    public void traceBefore(JoinPoint jp) {
        int depth = callDepth.get();
        String indent = "  ".repeat(depth); // Создаем отступ (2 пробела на каждый уровень)

        log.info("{}[TRACE] ---> Вход в метод: {}", indent, jp.getSignature().toShortString());

        callDepth.set(depth + 1); // Увеличиваем глубину для следующих вложенных вызовов
    }

    // Срабатывает после выполнения метода сервиса (даже если была ошибка)
    @After("kz.iitu.springlab.aspect.Pointcuts.serviceLayer()")
    public void traceAfter(JoinPoint jp) {
        int newDepth = callDepth.get() - 1;
        callDepth.set(newDepth);

        String indent = "  ".repeat(Math.max(0, newDepth));
        log.info("{}[TRACE] <--- Выход из метода: {}", indent, jp.getSignature().toShortString());

        // Очищаем ThreadLocal, когда возвращаемся на нулевой уровень (чтобы не было утечек памяти)
        if (newDepth <= 0) {
            callDepth.remove();
        }
    }
}