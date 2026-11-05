import java.util.Random;

public class Doctor implements Runnable {
    private int doctorId;
    private WaitingRoom waitingRoom;
    private TreatmentRoomManager treatmentRooms;

    public Doctor(int id, WaitingRoom waitingRoom,
                  TreatmentRoomManager treatmentRooms) {
        this.doctorId = id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
    }

    @Override
    public void run() {
        Random random = new Random();

        try {
            while (true) {
                // WaitingRoom defines takePatient(), which returns an Object.
                Patient patient = (Patient) waitingRoom.takePatient();

                if (patient == null) {
                    break;
                }

                System.out.println("Doctor " + doctorId +
                        " assigned to Patient " + patient.getPatientId());

                System.out.println("Doctor " + doctorId +
                        " trying to acquire treatment room");

                if (treatmentRooms.tryAcquireRoom(3000)) {
                    System.out.println("Doctor " + doctorId +
                            " acquired a treatment room");

                    System.out.println("Doctor " + doctorId +
                            " begins treating Patient " + patient.getPatientId());

                    try {
                        int duration = random.nextInt(3000) + 1000;
                        Thread.sleep(duration);

                        System.out.println("Doctor " + doctorId +
                                " finished treating Patient " + patient.getPatientId());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    } finally {
                        treatmentRooms.releaseRoom();
                        System.out.println("Doctor " + doctorId +
                                " released the treatment room");
                    }

                    System.out.println("Doctor " + doctorId + " is now available");
                    patient.finishTreatment();
                } else {
                    System.out.println("Doctor " + doctorId +
                            " could not acquire a treatment room");

                    System.out.println("Doctor " + doctorId +
                            " is sending patient back");

                    waitingRoom.returnPatient(patient);
                    System.out.println("Doctor " + doctorId + " is now available");
                }
            }
        } catch (InterruptedException e) {
            // Stop the doctor thread cleanly when it is interrupted.
            Thread.currentThread().interrupt();
        }
    }
}
