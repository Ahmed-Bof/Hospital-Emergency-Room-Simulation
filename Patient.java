import java.util.concurrent.Semaphore;

public class Patient implements Runnable {

    private int patientId;
    private WaitingRoom waitingRoom;
    private Semaphore treatmentDone;

    public Patient(int id, WaitingRoom waitingRoom) {
        this.patientId = id;
        this.waitingRoom = waitingRoom;
        this.treatmentDone = new Semaphore(0);
    }

    @Override
    public void run() {
        System.out.println("Patient " + patientId + " arrived");

        try {
            waitingRoom.enter(this);
            treatmentDone.acquire();
            System.out.println("Patient " + patientId + " left hospital");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void finishTreatment() {
        treatmentDone.release();
    }

    public int getPatientId() {
        return patientId;
    }
}
