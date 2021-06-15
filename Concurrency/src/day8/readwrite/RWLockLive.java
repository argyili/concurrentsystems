package day8.readwrite;

public final class RWLockLive implements ReadWriteLock {
	// private int readingReaders = 0; 
	private int writingWriters = 0; 
	// private int waitingWriters = 0;
	private boolean isReadersTurn = false;

	public synchronized void acquireRead() throws InterruptedException {
		while (writingWriters > 0 && isReadersTurn == true) {
			wait();
		}
	}

	public synchronized void releaseRead() {
		notifyAll();
	}

	public synchronized void acquireWrite() throws InterruptedException {
		isReadersTurn = false;		
		while (writingWriters > 0) {
			wait();
		}
		writingWriters++; 
		isReadersTurn = true;
	}

	public synchronized void releaseWrite() {
		writingWriters--;                      
		notifyAll();
	}
}
