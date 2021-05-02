package day3_edited;

// Thread name must be "0" or "1"
public class LockAttempt3 implements MyLock {
	private volatile boolean[] want = new boolean[2];
	
	@Override
	public void lock() {
		int id = Integer.parseInt(Thread.currentThread().getName());
		want[id] = true;
		int last = 1 - id;
		while (want[1 - id] == true && last == 1 - id) {}
	}

	@Override
	public void unlock() {
		int id = Integer.parseInt(Thread.currentThread().getName());
		want[id] = false;
	}
}
