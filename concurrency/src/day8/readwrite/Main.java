package day8.readwrite;

public class Main {
    public static void main(String[] args) {
        Data data = new Data(10);
        new ReaderThread(data).start();
        new ReaderThread(data).start();
        // new ReaderThread(data).start();
        
        new WriterThread(data, "ABCDEFGHIJKLMNOPQRSTUVWXYZ").start();
<<<<<<< HEAD
        // new WriterThread(data, "abcdefghijklmnopqrstuvwxyz").start();
        //        new WriterThread(data, "0123456789").start();
        //        new WriterThread(data, "!#$%&'()=~|").start();
=======
        new WriterThread(data, "abcdefghijklmnopqrstuvwxyz").start();
        new WriterThread(data, "0123456789").start();
        new WriterThread(data, "!#$%&'()=~|").start();
        new WriterThread(data, "ABCDEFGHIJKLMNOPQRSTUVWXYZ").start();
        new WriterThread(data, "abcdefghijklmnopqrstuvwxyz").start();
        new WriterThread(data, "0123456789").start();
        new WriterThread(data, "!#$%&'()=~|").start();
>>>>>>> upstream/master
    }
}
