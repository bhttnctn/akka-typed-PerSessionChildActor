package per.session;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class KeyCabinet {
	public static Behavior<GetKeys> create() {
		return Behaviors.receiveMessage(KeyCabinet::onGetKeys);
	}

	private static Behavior<GetKeys> onGetKeys(GetKeys message) {
		System.out.println("KeyCabinet.onGetKeys");
		message.replyTo.tell(new Keys());
		return Behaviors.same();
	}

	public static class GetKeys {
		public final String whoseKeys;
		public final ActorRef<Object> replyTo;

		public GetKeys(String whoseKeys, ActorRef<Object> respondTo) {
			this.whoseKeys = whoseKeys;
			this.replyTo = respondTo;
		}
	}
}