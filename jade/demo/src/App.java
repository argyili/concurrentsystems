import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

// 1st homework at July 05.
// public class App extends Agent{
//     public static void main(String[] args) throws Exception {
//         System.out.println(100 / 2);

//         // setup();
//     }

//     protected void setup() {
//         System.out.println("Hello World.");
//         System.out.println(getLocalName());
  	    
//         // Make this agent terminate
//   	    // doDelete();
//     }
// }

// 2nd homework at July 12.
public class App extends Agent{
    protected void setup(){
        addBehaviour(new myBehaviour(this));
    }
    class myBehaviour extends SimpleBehaviour{
        int n=0;
        public myBehaviour(Agent a){
            super(a);
        }
        public void action(){
            //Write task to be executed
            System.out.println("Hello World");
            System.out.println("My name is "+getLocalName());
            n++;
        }
        public boolean done(){
            return n>=3;
        }
    }
}

// li-aiyi@li-aiyi-workstation:~/Documents/program/demo-project/java-jade/src/concurrentSystems$ /usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 @/tmp/cp_do3lt1gde1h1mmhd3ztj52jxx.argfile jade.Boot -agents aiyi:App

// /usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java -Dfile.encoding=UTF-8 @/tmp/cp_do3lt1gde1h1mmhd3ztj52jxx.argfile jade.Boot -agents aiyi:App
