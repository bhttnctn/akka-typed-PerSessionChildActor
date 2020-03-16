# akka-typed-PerSessionChildActor
In some cases a complete response to a request can only be created and sent back after collecting multiple answers from other actors. For these kinds of interaction it can be good to delegate the work to a per “session” child actor. The child could also contain arbitrary logic to implement retrying, failing on timeout, tail chopping, progress inspection etc.
