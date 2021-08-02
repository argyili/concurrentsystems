package concurrentSystems;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.AMSService;

// 3nd homework at July 19.
public class AMSDump extends Agent {
    protected void setup(){
        AMSAgentDescription[] agents = null;
        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults (new Long(-1)); //-1: ALL
            agents = AMSService.search(this, new AMSAgentDescription (), c);
        }catch (Exception e) {
            e.printStackTrace();
        }
        AID myID = getAID();
        for (int i=0; i<agents.length;i++){
            AID agentID = agents[i].getName();
            System.out.println((agentID.equals(myID) ? "*** " : " ") + i + ": " + agentID.getName());
        }
    }
}