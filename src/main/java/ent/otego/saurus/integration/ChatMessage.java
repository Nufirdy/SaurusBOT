package ent.otego.saurus.integration;

public class ChatMessage<T> {

    private final T messengerData;

    public ChatMessage(T messengerData) {
        this.messengerData = messengerData;
    }

    public T getMessengerData() {
        return messengerData;
    }
}
