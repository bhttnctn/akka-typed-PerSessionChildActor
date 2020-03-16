package per.session;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.Behaviors;

public class MainActorSystem {

	public static void main(String args[]) {

		System.out.println("Starting...");
		final ActorSystem<Home.Command> system = ActorSystem.create(Home.create(), "home");
		System.out.println("Actor systems created...");
		final ActorRef<Home.Command> homeActor = system;
		ActorRef<Home.ReadyToLeaveHome> finalActor = system.systemActorOf(readyToLeaveHomeBehavior(), "finalActor", Props.empty());
		var leaveHome = new Home.LeaveHome("Abdulkerim", finalActor);
		homeActor.tell(leaveHome);
	}

	private static Behavior<Home.ReadyToLeaveHome> readyToLeaveHomeBehavior() {
		return Behaviors.receive(Home.ReadyToLeaveHome.class)//
				.onMessage(Home.ReadyToLeaveHome.class, MainActorSystem::onResult).build();
	}

	public static Behavior<Home.ReadyToLeaveHome> onResult(Home.ReadyToLeaveHome msg) {
		System.out.println(msg);
		return Behaviors.same();
	}
}
