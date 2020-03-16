package per.session;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Optional;

// per session actor behavior
public class PrepareToLeaveHome extends AbstractBehavior<Object> {

	private final String whoIsLeaving;
	private final ActorRef<Home.ReadyToLeaveHome> replyTo;
	private final ActorRef<KeyCabinet.GetKeys> keyCabinet;
	private final ActorRef<Drawer.GetWallet> drawer;
	private Optional<Wallet> wallet = Optional.empty();
	private Optional<Keys> keys = Optional.empty();

	private PrepareToLeaveHome(ActorContext<Object> context, String whoIsLeaving, ActorRef<Home.ReadyToLeaveHome> replyTo,
			ActorRef<KeyCabinet.GetKeys> keyCabinet, ActorRef<Drawer.GetWallet> drawer) {
		super(context);
		this.whoIsLeaving = whoIsLeaving;
		this.replyTo = replyTo;
		this.keyCabinet = keyCabinet;
		this.drawer = drawer;
	}

	static Behavior<Object> create(String whoIsLeaving, ActorRef<Home.ReadyToLeaveHome> replyTo, ActorRef<KeyCabinet.GetKeys> keyCabinet,
			ActorRef<Drawer.GetWallet> drawer) {
		return Behaviors.setup(context -> new PrepareToLeaveHome(context, whoIsLeaving, replyTo, keyCabinet, drawer));
	}

	@Override
	public Receive<Object> createReceive() {
		return newReceiveBuilder().onMessage(Wallet.class, this::onWallet).onMessage(Keys.class, this::onKeys).build();
	}

	private Behavior<Object> onWallet(Wallet wallet) {
		System.out.println("PrepareToLeaveHome.onWallet");
		this.wallet = Optional.of(wallet);
		return completeOrContinue();
	}

	private Behavior<Object> onKeys(Keys keys) {
		System.out.println("PrepareToLeaveHome.onKeys");
		this.keys = Optional.of(keys);
		return completeOrContinue();
	}

	private Behavior<Object> completeOrContinue() {
		if (wallet.isPresent() && keys.isPresent()) {
			replyTo.tell(new Home.ReadyToLeaveHome(whoIsLeaving, keys.get(), wallet.get()));
			return Behaviors.stopped();
		} else {
			return this;
		}
	}
}