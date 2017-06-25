package de.htwg.se.ws1516.fourwinning.controller.actor;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actor extends UntypedAbstractActor{
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof String){
            log.info("Received message:{}", message);
            //getSender().tell(message,getSelf());
        }else {
            unhandled(message);
        }
    }
}
