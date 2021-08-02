package concurrentsystems;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class Ping extends Agent {
	protected void setup() {
		AMSAgentDescription[] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();
			// c.setMaxResults(new Long(-1));
			c.setMaxResults(Long.valueOf(-1));
			agents = AMSService.search(this, new AMSAgentDescription(), c);
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}

		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent("Ping");
		for (int i = 0; i < agents.length; i++)
			msg.addReceiver(agents[i].getName());
		send(msg);

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				ACLMessage msg = receive();
				if (msg != null)
					System.out.println("== Answer" + " <- " + msg.getContent() + " from " + msg.getSender().getName());
				block();
			}
		});
	}
}
