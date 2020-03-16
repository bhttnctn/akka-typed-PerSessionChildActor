package per.session;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class Drawer {

	public static Behavior<GetWallet> create() {
		return Behaviors.receiveMessage(Drawer::onGetWallet);
	}

	private static Behavior<GetWallet> onGetWallet(GetWallet message) {
		System.out.println("Drawer.onGetWallet");
		message.replyTo.tell(new Wallet());
		return Behaviors.same();
	}

	public static class GetWallet {
		public final String whoseWallet;
		public final ActorRef<Object> replyTo;

		public GetWallet(String whoseWallet, ActorRef<Object> replyTo) {
			this.whoseWallet = whoseWallet;
			this.replyTo = replyTo;
		}
	}
}