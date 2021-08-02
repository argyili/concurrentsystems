package concurrentSystems;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;

// 3nd homework at July 19.
public class Pong extends Agent {
    protected void setup(){
        addBehaviour(new CyclicBehaviour(this){
            public void action(){
                ACLMessage msg = receive();
                // System.out.println("Pong");
                if (msg!=null) {
                    System.out.println(" - "+myAgent.getLocalName()
                        +" received: "+ msg.getContent());
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Pong");
                    send(reply);
                }
                block();
            }
        });
    }
}


// /usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 @/tmp/cp_do3lt1gde1h1mmhd3ztj52jxx.argfile jade.Boot -gui -local-host::127.0.0.1 -container -agents pong1:concurrentSystems.Pong;pong2:concurrentSystems.Pong;john:concurrentSystems.AMSDump;ping:ping:concurrentSystems.Ping

// java @/tmp/cp_do3lt1gde1h1mmhd3ztj52jxx.argfile jade.Boot -gui -local-host::127.0.0.1 -container -agents pong1:concurrentSystems.Pong;pong2:concurrentSystems.Pong;john:concurrentSystems.AMSDump;ping:ping:concurrentSystems.Ping
