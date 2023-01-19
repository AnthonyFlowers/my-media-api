package mymedia.domain;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

    private final List<String> messages = new ArrayList<>();
    private T payload;
    private ResultType type = ResultType.SUCCESS;

    public void addMessage(ResultType resultType, String message) {
        type = resultType;
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public ResultType getType() {
        return type;
    }
}
