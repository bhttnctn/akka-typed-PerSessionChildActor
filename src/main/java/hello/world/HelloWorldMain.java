package hello.world;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class HelloWorldMain extends AbstractBehavior<HelloWorldMain.Start> {

	private final ActorRef<HelloWorld.Greet> greeter;

	private HelloWorldMain(ActorContext<Start> context) {
		super(context);
		greeter = context.spawn(HelloWorld.create(), "greeter");
	}

	public static Behavior<Start> create() {
		return Behaviors.setup(HelloWorldMain::new);
	}

	public static void main(String[] arg) {
		final ActorSystem<Start> system = ActorSystem.create(HelloWorldMain.create(), "hello");

		system.tell(new HelloWorldMain.Start("World"));
		system.tell(new HelloWorldMain.Start("Akka"));
	}

	@Override
	public Receive<Start> createReceive() {
		return newReceiveBuilder().onMessage(Start.class, this::onStart).build();
	}

	private Behavior<Start> onStart(Start command) {
		ActorRef<HelloWorld.Greeted> replyTo = getContext().spawn(HelloWorldBot.create(3), command.name);
		greeter.tell(new HelloWorld.Greet(command.name, replyTo));
		return this;
	}

	public static class Start {
		public final String name;

		public Start(String name) {
			this.name = name;
		}
	}
}