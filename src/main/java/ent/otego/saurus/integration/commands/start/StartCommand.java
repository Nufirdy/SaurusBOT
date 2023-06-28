package ent.otego.saurus.integration.commands.start;

import ent.otego.saurus.integration.*;
import ent.otego.saurus.integration.commands.Command;
import ent.otego.saurus.integration.model.Subscriber;

public abstract class StartCommand<T> implements Command<T> {

    public final static String COMMAND_NAME = "start";
    protected final SubscriberService subscriberService;

    public StartCommand(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void executeCommand(ChatMessage<T> message) {
        Subscriber subscriber = getSubscriber(message.getMessengerData());
        if (subscriber == null) {
            subscriber = saveNewSubscriber(message.getMessengerData());
            subscriberService.onSubscription(subscriber);
            onSubscription(message.getMessengerData());
        } else {
            onResubscription(message.getMessengerData());
        }
    }

    protected abstract Subscriber getSubscriber(T messengerData);

    protected abstract Subscriber saveNewSubscriber(T messengerData);

    protected abstract void onSubscription(T messengerData);

    protected abstract void onResubscription(T messengerData);
}
