package per.session;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

public class Home {

	private final ActorContext<Command> context;
	private final ActorRef<KeyCabinet.GetKeys> keyCabinet;
	private final ActorRef<Drawer.GetWallet> drawer;

	private Home(ActorContext<Command> context) {
		this.context = context;
		this.keyCabinet = context.spawn(KeyCabinet.create(), "key-cabinet");
		this.drawer = context.spawn(Drawer.create(), "drawer");
	}

	// actor behavior
	public static Behavior<Command> create() {
		return Behaviors.setup(context -> new Home(context).behavior());
	}

	private Behavior<Command> behavior() {
		return Behaviors.receive(Command.class)//
				.onMessage(LeaveHome.class, this::onLeaveHome).build();
	}

	private Behavior<Command> onLeaveHome(LeaveHome message) {
		System.out.println("onLeaveHome");
		ActorRef<Object> prepareActor = context.spawn(PrepareToLeaveHome.create(message.who, message.respondTo, keyCabinet, drawer), "leaving" + message.who);
		keyCabinet.tell(new KeyCabinet.GetKeys("Abdulkerim", prepareActor));
		drawer.tell(new Drawer.GetWallet("Abdulkerim", prepareActor));
		return Behaviors.same();
	}

	public interface Command {
	}

	public static class LeaveHome implements Command {
		public final String who;
		public final ActorRef<ReadyToLeaveHome> respondTo;

		public LeaveHome(String who, ActorRef<ReadyToLeaveHome> respondTo) {
			this.who = who;
			this.respondTo = respondTo;
		}
	}

	public static class ReadyToLeaveHome {
		public final String who;
		public final Keys keys;
		public final Wallet wallet;

		public ReadyToLeaveHome(String who, Keys keys, Wallet wallet) {
			this.who = who;
			this.keys = keys;
			this.wallet = wallet;
			System.out.println("ReadyToLeaveHome() created");
		}

		@Override
		public String toString() {
			return "ReadyToLeaveHome{" + "who='" + who + '\'' + ", keys=" + keys + ", wallet=" + wallet + '}';
		}
	}
}