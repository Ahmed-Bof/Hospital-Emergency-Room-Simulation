public class Semaphore {
    private int value;

    public Semaphore(int initial) {
        this.value = initial;
    }

    public synchronized void acquire() throws InterruptedException {
        while (value <= 0) {
            wait();
        }
        value--;
    }

    public synchronized void release() {
        value++;
        notify();
    }

    public synchronized boolean tryAcquire(int timeoutMilliseconds) throws InterruptedException {
        long deadline = System.currentTimeMillis() + Math.max(0, timeoutMilliseconds);
        while (value <= 0) {
            long remaining = deadline - System.currentTimeMillis();
            if (remaining <= 0) {
                return false;
            }
            wait(remaining);
        }
        value--;
        return true;
    }

}
