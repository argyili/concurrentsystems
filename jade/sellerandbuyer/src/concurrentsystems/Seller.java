package concurrentSystems;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Seller: Agent which accepts QUERY_REF message and for each creates ------ a
 * conversation which waits a random time then answers with an INFORM containing
 * a random "price" It then waits for a REQUEST and answers with AGREE or REFUSE
 * 
 * @author Jean Vaucher (Sept 11 2003)
 * @author Hiroyuki Nakagawa (Update: July 27 2015)
 */
public class Seller extends Agent {
	Random rnd = newRandom();
	MessageTemplate query = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF);

	protected void setup() {
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				ACLMessage msg = receive(query);
				if (msg != null) {
					addBehaviour(new Transaction(myAgent, msg));
				}
				block();
			}
		});
		addBehaviour(new GCBehaviour(this, 5000));
	}

	class Transaction extends SequentialBehaviour {
		ACLMessage msg, reply;
		String convID;
		int price = rnd.nextInt(90) + 10; // 10-99

		public Transaction(Agent a, ACLMessage msg) {
			super(a);
			this.msg = msg;
			convID = msg.getConversationId();
		}

		public void onStart() {
			int delay = rnd.nextInt(1500);
			System.out.println(" - " + myAgent.getLocalName() + " <- QUERY from " + msg.getSender().getLocalName()
					+ ".  Will answer $" + price + " in " + delay + " ms");

			addSubBehaviour(new DelayBehaviour(myAgent, delay) {
				public void handleElapsedTimeout() {
					reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent("" + price);
					send(reply);
				}
			});

			MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
					MessageTemplate.MatchConversationId(convID));

			addSubBehaviour(new MyReceiver(myAgent, 2000, template) {
				public void handle(ACLMessage msg1) {
					if (msg1 != null) {
						int offer = Integer.parseInt(msg1.getContent());
						System.out.println("Got proposal $" + offer + " from " + msg1.getSender().getLocalName()
								+ " & my price is $" + price);

						reply = msg1.createReply();
						if ((int) (Math.random() * 3) >= 1)
							// if(offer >= rnd.nextInt(price))
							reply.setPerformative(ACLMessage.AGREE);
						else
							reply.setPerformative(ACLMessage.REFUSE);
						send(reply);
						System.out.println("  == " + ACLMessage.getPerformative(reply.getPerformative()));
					} else {
						System.out.println(
								"Timeout ! quote $" + price + " from " + getLocalName() + " is no longer valid");
					}
				}
			});
		}
	}

	// ========== Utility methods =========================

	// --- generating Conversation IDs -------------------
	protected static int cidCnt = 0;
	String cidBase;

	String genCID() {
		if (cidBase == null) {
			cidBase = getLocalName() + hashCode() + System.currentTimeMillis() % 10000 + "_";
		}
		return cidBase + (cidCnt++);
	}

	// --- generating distinct Random generator -------------------
	Random newRandom() {
		return new Random(hashCode() + System.currentTimeMillis());
	}

	// ----- clean-up behaviour which takes old messages out of the queue
	class GCBehaviour extends TickerBehaviour {
		Set<ACLMessage> seen = new HashSet<ACLMessage>(), old = new HashSet<ACLMessage>();

		GCBehaviour(Agent a, long dt) {
			super(a, dt);
		}

		protected void onTick() {
			ACLMessage msg = myAgent.receive();
			while (msg != null) {
				if (!old.contains(msg))
					seen.add(msg);
				else {
					System.out.print("+++ Flushing message: ");
					dumpMessage(msg);
				}
				msg = myAgent.receive();
			}
			for (Iterator<ACLMessage> it = seen.iterator(); it.hasNext();)
				myAgent.putBack((ACLMessage) it.next());
			old.clear();
			Set<ACLMessage> tmp = old;
			old = seen;
			seen = tmp;
		}
	}

	// ---------- Message print-out --------------------------------------

	static long t0 = System.currentTimeMillis();

	void dumpMessage(ACLMessage msg) {
		System.out.print("t=" + (System.currentTimeMillis() - t0) / 1000F + " in " + getLocalName() + ": "
				+ ACLMessage.getPerformative(msg.getPerformative()));
		System.out
				.print("  from: " + (msg.getSender() == null ? "null" : msg.getSender().getLocalName()) + " --> to: ");
		for (Iterator<AID> it = msg.getAllReceiver(); it.hasNext();)
			System.out.print(((AID) it.next()).getLocalName() + ", ");
		System.out.println("  cid: " + msg.getConversationId());
		System.out.println("  content: " + msg.getContent());
	}
}
