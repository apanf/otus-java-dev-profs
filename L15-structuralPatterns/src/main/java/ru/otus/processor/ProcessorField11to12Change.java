package ru.otus.processor;

import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayList;

public class ProcessorField11to12Change implements Processor {
    @Override
    public Message process(Message message) {
        var field13 = new ObjectForMessage();
        var data = message.getField13().getData();

        if (data != null)
            field13.setData(new ArrayList<>(data));

        return new Message.Builder(message.getId() + 1)
                .field1(message.getField1())
                .field2(message.getField2())
                .field3(message.getField3())
                .field4(message.getField4())
                .field5(message.getField5())
                .field6(message.getField6())
                .field7(message.getField7())
                .field8(message.getField8())
                .field9(message.getField9())
                .field10(message.getField10())
                .field11(message.getField12())
                .field12(message.getField11())
                .field13(field13)
                .build();
    }
}