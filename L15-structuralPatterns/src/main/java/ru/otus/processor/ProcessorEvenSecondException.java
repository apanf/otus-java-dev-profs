package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorEvenSecondException implements Processor {

    @Override
    public Message process(Message message) {
        if (System.currentTimeMillis() / 1000 % 2 == 0)
            throw new RuntimeException();
        return message;
    }
}
