package concurrentSystems;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.AMSService;

// 3nd homework at July 19.
public class Ping extends Agent {
    public static void main(String[] args) throws Exception {
        // String regex = "\\b(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]";
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String text = "http://google.com";
        System.out.println(IsMatch(text,regex));
    }
    protected void setup(){
        AMSAgentDescription[] agents = null;
        try {
            SearchConstraints c =
            new SearchConstraints();
            c.setMaxResults (new Long(-1));
            agents = AMSService.search(this,
            new AMSAgentDescription (), c);
        }catch (Exception e) {
            e.printStackTrace();
        }
        ACLMessage msg =
            new ACLMessage(ACLMessage.INFORM);
        msg.setContent("Ping");
        for (int i=0; i<agents.length;i++)
            msg.addReceiver(agents[i].getName());
        send(msg);

        addBehaviour(new CyclicBehaviour(this){
            public void action() {
                ACLMessage msg= receive();
                if (msg!=null)
                    System.out.println("== Answer" + " <- " + msg.getContent() + " from " + msg.getSender().getName());
                block();
            }
        });
    }
}
