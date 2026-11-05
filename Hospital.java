import java.util.Scanner;

public class Hospital {
    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter waiting room capacity: ");
        int capacity = input.nextInt();
        System.out.print("Enter number of doctors: ");
        int numDoctors = input.nextInt();
        System.out.print("Enter number of patients: ");
        int numPatients = input.nextInt();
        input.close();

        System.out.println("\n=== Simulation Started ===\n");
        System.out.println("Patients arriving: ");
        StringBuilder pList = new StringBuilder();
        for (int i = 1; i <= numPatients; ++i) {
            pList.append("P").append(i).append(" ");
        }
        System.out.println(pList.toString().trim() + "\n");

        WaitingRoom waitingRoom = new WaitingRoom(capacity);
        TreatmentRoomManager treatmentRoom = new TreatmentRoomManager(2);

        // Threads for doctors to work
        Thread[] doctorThread = new Thread[numDoctors];
        Doctor[] doctors = new Doctor[numDoctors];

        for (int i = 0; i < numDoctors; ++i) {
            doctors[i] = new Doctor(i + 1, waitingRoom, treatmentRoom);
            doctorThread[i] = new Thread(doctors[i]);
            doctorThread[i].start();
        }
        // Threads of patients
        Thread[] patientThread = new Thread[numPatients];
        for (int i = 0; i < numPatients; ++i) {
            Patient patient = new Patient(i + 1, waitingRoom);
            patientThread[i] = new Thread(patient);
            patientThread[i].start();
            Thread.sleep(300);
        }

        // waiting untill all patients arrive and enter waiting room
        for (int i = 0; i < numPatients; ++i) {
            patientThread[i].join();
        }

        Thread.sleep(5000);

        for (int i = 0; i < numDoctors; ++i) {
            doctorThread[i].interrupt();
        }

        System.out.println("\nAll patients have been treated.");
        System.out.println("Simulation completed.");

    }
}
