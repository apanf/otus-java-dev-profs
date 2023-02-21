package homework.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * Общий класс исключений банкомата.
 */
public class AtmException extends RuntimeException {
    public AtmException(String pattern, Object... args) {
        super(MessageFormatter.basicArrayFormat(pattern, args));
    }
}
