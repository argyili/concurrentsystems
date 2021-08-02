package concurrentsystems;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;

public class AMSDump extends Agent {
	protected void setup() {
		AMSAgentDescription[] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1)); // -1: ALL
			c.setMaxResults(Long.valueOf(-1));
			agents = AMSService.search(this, new AMSAgentDescription(), c);
		} catch (Exception e) {
			// System.out.println( "Problem searching AMS: " + e );
			e.printStackTrace();
		}

		AID myID = getAID();
		for (int i = 0; i < agents.length; i++) {
			AID agentID = agents[i].getName();
			System.out.println((agentID.equals(myID) ? "*** " : "    ") + i + ": " + agentID.getName());
		}
		// doDelete();
		// System.exit(0);
	}
}
