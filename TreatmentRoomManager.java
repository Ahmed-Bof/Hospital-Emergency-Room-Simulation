public class TreatmentRoomManager {
    private Semaphore treatmentRooms;

    public TreatmentRoomManager(int capacity) {
        treatmentRooms = new Semaphore(capacity);
    }


   public boolean tryAcquireRoom(int timeoutMilliseconds) throws InterruptedException {
        return treatmentRooms.tryAcquire(timeoutMilliseconds);
    }

    public void releaseRoom() {

        treatmentRooms.release();
    }

}
