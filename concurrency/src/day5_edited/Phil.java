package day5_edited;

import java.util.concurrent.ThreadLocalRandom;

public class Phil extends Thread {
	final static int numOfPhils = 2;
	final int id;
	final Resource left, right;

	Phil(Resource left, Resource right, int id) {
		this.left = left;
		this.right = right;
		this.id = id;
	}

	public void run() {	
		while (true) {
			try {
				if (this.id % 2 == 0) {
					// non critical section
					Thread.sleep(ThreadLocalRandom.current().nextLong(100, 200));			
					synchronized (right) {
						System.out.println(id + " got right fork");
						synchronized (left) {
							System.out.println(id + " got left fork");
							// critical section starts
							Thread.sleep(100);
							// critical section ends
						}
						System.out.println(id + " put right fork");
					}
					System.out.println(id + " put left fork");
				} else {
					// non critical section
					Thread.sleep(ThreadLocalRandom.current().nextLong(100, 200));			
					synchronized (left) {
						System.out.println(id + " got left fork");
						synchronized (right) {
							System.out.println(id + " got right fork");
							// critical section starts
							Thread.sleep(100);
							// critical section ends
						}
						System.out.println(id + " put left fork");
					}
					System.out.println(id + " put right fork");
				}

			} catch (InterruptedException ex) {
				return;
			}
		}
	}

	public static void main(String[] args) {
		Resource[] fork = new Resource[numOfPhils];
		for (int i = 0; i < numOfPhils; i++) {
			fork[i] = new Resource("Resource " + i);
		}
		for (int i = 0; i < numOfPhils; i++) {
			new Phil(fork[(i + numOfPhils - 1) % numOfPhils], fork[i], i).start();
		}
	}
}
