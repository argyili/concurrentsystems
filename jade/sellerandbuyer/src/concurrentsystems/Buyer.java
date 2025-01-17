package concurrentsystems;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


/**
Buyer:    Program which sends QUERY_REF to agents "s1,s2 & s3"
          and wait for the best quote.  If it is below $80, it sends
          a REQUEST and waits to see if the order is accepted.

@author Jean Vaucher (Sept 11 2003)
@author Hiroyuki Nakagawa (Update: July 27 2015)
*/
public class Buyer extends Agent{
	Random rnd = newRandom();
	static int TARGET_PRICE = 30;
	static int MAX_PRICE = 999;
	int bestPrice;
	   
	ACLMessage msg, bestOffer;
	Behaviour flusher;                                          

	protected void setup(){
		if(flusher==null){
			flusher = new GCBehaviour(this, 2000);
			addBehaviour(flusher);
		}
		bestPrice = MAX_PRICE;
		bestOffer = null;
			
		msg = newMsg(ACLMessage.QUERY_REF); 

		MessageTemplate template = MessageTemplate.and( 
				MessageTemplate.MatchPerformative(ACLMessage.INFORM),
				MessageTemplate.MatchConversationId(msg.getConversationId()));     
	      
		SequentialBehaviour seq = new SequentialBehaviour();
		addBehaviour(seq);
				
		ParallelBehaviour par = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);

		for(int i = 1; i<=3; i++){
			msg.addReceiver(new AID("s" + i, AID.ISLOCALNAME));
			par.addSubBehaviour(new MyReceiver(this, 1000, template){
				public void handle(ACLMessage msg){  
					if(msg != null){
						int offer = Integer.parseInt(msg.getContent());
						System.out.println("Got quote $" + offer +
								" from " + msg.getSender().getLocalName());
						if(offer < bestPrice){
							bestPrice = offer;
							bestOffer = msg;
						}
					}
				}
			});
		}
	             
		seq.addSubBehaviour(par);
		seq.addSubBehaviour(new DelayBehaviour(this, rnd.nextInt(2000)){
			public void handleElapsedTimeout(){  
				if(bestOffer == null) System.out.println("Got no quotes"); 
				else{
					System.out.println("\nBest Price $" + bestPrice +
							" from " + bestOffer.getSender().getLocalName());
					ACLMessage reply = bestOffer.createReply();
					if(bestPrice <= TARGET_PRICE){
						reply.setPerformative(ACLMessage.REQUEST);
//						reply.setContent("" + rnd.nextInt(TARGET_PRICE));
						reply.setContent("" + (int)(bestPrice*0.9));
						System.out.println(" ORDER at "+ reply.getContent());
						send(reply);
					}
				}
			}
		});
	      
		seq.addSubBehaviour(new MyReceiver(this, 1000,
				MessageTemplate.and( 
						MessageTemplate.MatchConversationId(msg.getConversationId()),
						MessageTemplate.or( 
								MessageTemplate.MatchPerformative(ACLMessage.AGREE),
								MessageTemplate.MatchPerformative(ACLMessage.REFUSE)))){
			public void handle(ACLMessage msg){  
				if(msg != null){
					System.out.println("Got " + 
							ACLMessage.getPerformative(msg.getPerformative()) +
							" from " + msg.getSender().getLocalName());
					
					if(msg.getPerformative() == ACLMessage.AGREE)
						System.out.println("  --------- Finished ---------\n");
					else setup();
				} 
				else{
					System.out.println("=="+getLocalName()+" timed out");
					setup();
				}
			}	
		});
		
		send(msg);
	}
	    
	// ========== Utility methods =========================
	//  --- generating Conversation IDs -------------------

	protected static int cidCnt = 0;
	String cidBase;
	   
	String genCID(){ 
		if(cidBase==null){
			cidBase = getLocalName() + hashCode() +
					System.currentTimeMillis()%10000 + "_";
		}
		return cidBase + (cidCnt++); 
	}

	//  --- generating distinct Random generator -------------------
	Random newRandom(){return new Random(hashCode()+System.currentTimeMillis());}

	//  --- Methods to initialize ACLMessages -------------------
	ACLMessage newMsg(int perf, String content, AID dest){
		ACLMessage msg = newMsg(perf);
		if(dest != null) msg.addReceiver(dest);
		msg.setContent(content);
		return msg;
	}

	ACLMessage newMsg(int perf){
		ACLMessage msg = new ACLMessage(perf);
		msg.setConversationId(genCID());
		return msg;
	}
	   
	// ----- clean-up agent which takes old messages out of the queue
	class GCBehaviour extends TickerBehaviour{
		Set<ACLMessage> seen = new HashSet<ACLMessage>(),
		    old  = new HashSet<ACLMessage>();
			
		GCBehaviour(Agent a, long dt){super(a,dt);}
		
		protected void onTick(){
			ACLMessage msg = myAgent.receive();
			while(msg != null){
				if(!old.contains(msg)) seen.add(msg);
				else{
					System.out.println("==== Flushing message:");
					dumpMessage(msg);
				}
				msg = myAgent.receive();
			}
			for(Iterator<ACLMessage> it = seen.iterator(); it.hasNext();)
				myAgent.putBack((ACLMessage) it.next());
					
			old.clear();
			Set<ACLMessage> tmp = old;
			old = seen;
			seen = tmp; 
		}
	}

	// ---------- Message print-out --------------------------------------
	static long t0 = System.currentTimeMillis();
	
	void dumpMessage(ACLMessage msg){
		System.out.print("t=" + (System.currentTimeMillis()-t0)/1000F + " in "
				+ getLocalName() + ": "
				+ ACLMessage.getPerformative(msg.getPerformative()));
		System.out.print("  from: " +
				(msg.getSender()==null ? "null" : msg.getSender().getLocalName())
				+  " --> to: ");
		for(Iterator<AID> it = msg.getAllReceiver(); it.hasNext();)
			System.out.print(((AID) it.next()).getLocalName() + ", ");
		System.out.println("  cid: " + msg.getConversationId());
		System.out.println("  content: " +  msg.getContent());
	}
}
