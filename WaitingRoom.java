import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    Queue<Object> waitingQueue = new LinkedList<>();
    Queue<Object> outsideQueue = new LinkedList<>();
    Semaphore emptySlots;
    Semaphore availablePatients;
    Semaphore mutex;

    private boolean closed = false;

    public WaitingRoom(int capacity) {

        waitingQueue = new LinkedList<>();
        outsideQueue = new LinkedList<>();
        emptySlots = new Semaphore(capacity);
        availablePatients = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    public void enter(Object patientWantToEnter) throws InterruptedException {
        mutex.acquire();

        try {
            if (emptySlots.tryAcquire(0) == true) {

                waitingQueue.add(patientWantToEnter);
                System.out.println("Patient entered the waiting room. Current waiting patients: " + waitingQueue.size());
                availablePatients.release();

            } else {
                outsideQueue.add(patientWantToEnter);
                System.out.println("Waiting room is full. Patient is waiting outside. Current outside patients: " + outsideQueue.size());
            }
        } finally {
            mutex.release();
        }

    }

    public Object takePatient() throws InterruptedException {
        availablePatients.acquire(); // acquire => decrease number of patients
        mutex.acquire();

        try {
            if (waitingQueue.isEmpty() && closed) {
                /*
                If we have no remaining patients and we closed
                */ 
                return null;
            }
            
            Object takenPatient = waitingQueue.remove();

            if (!outsideQueue.isEmpty()) {
                /*
                Take a patient from outside and put in the waiting queue
                */
                Object nextPatient = outsideQueue.remove();
                waitingQueue.add(nextPatient);
                System.out.println("Patient from outside has entered the waiting room. Current waiting patients: "+ waitingQueue.size());
                availablePatients.release();

            }else
                {
                    emptySlots.release();
                }
            
            return takenPatient ;

        } finally {
            mutex.release();
        }
    }

    public void returnPatient(Object patientToBeReturned) throws InterruptedException {
        enter(patientToBeReturned);
    }

    public void close(int numberOfDoctors) throws InterruptedException {
        mutex.acquire();

        try {
            closed = true;
        } finally {
            mutex.release();
        }
        
        for (int i = 0; i < numberOfDoctors; i++) {
            availablePatients.release();
        }
    }
}
