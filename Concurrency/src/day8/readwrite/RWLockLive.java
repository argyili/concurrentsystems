package day8.readwrite;

public final class RWLockLive implements ReadWriteLock {
<<<<<<< HEAD
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
=======
	private int readingReaders = 0; 
	private int writingWriters = 0; 
	private int waitingWriters = 0;
	private boolean isReadersTurn = false;
	
	public synchronized void acquireRead() throws InterruptedException {
		while (writingWriters > 0 || (waitingWriters > 0 && !isReadersTurn)) {
			wait();
		}
		readingReaders++;
	}

	public synchronized void releaseRead() {
		readingReaders--;   
		isReadersTurn = false;
>>>>>>> upstream/master
		notifyAll();
	}

	public synchronized void acquireWrite() throws InterruptedException {
<<<<<<< HEAD
		isReadersTurn = false;		
		while (writingWriters > 0) {
			wait();
		}
		writingWriters++; 
		isReadersTurn = true;
	}

	public synchronized void releaseWrite() {
		writingWriters--;                      
=======
		waitingWriters++;
		while (readingReaders > 0 || writingWriters > 0) {
			wait();
		}
		waitingWriters--;
		writingWriters++; 
	}

	public synchronized void releaseWrite() {
		writingWriters--;   
		isReadersTurn = true;
>>>>>>> upstream/master
		notifyAll();
	}
}
