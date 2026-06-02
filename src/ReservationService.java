import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private DiscountPolicy discountPolicy;
    private List<Student> students;
    private List<Equipment> equipmentList;
    private List<Reservation> reservations;
    private int reservationCounter = 1;

    public ReservationService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
        this.students = new ArrayList<>();
        this.equipmentList = new ArrayList<>();
        this.reservations = new ArrayList<>();
        initializeData();
    }

    private void initializeData() {
        students.add(new Student("S001", "Anna Kowalska", "12c", 120));
        students.add(new Student("S002", "Marek Nowak", "12c", 40));
        students.add(new Student("S003", "Julia Zielinska", "13a", 0));

        // PJAIT specific test student for local validation
        students.add(new Student("s35587", "Viktor Chygrynets", "PGO1", 150));

        equipmentList.add(new LaptopSet("E001", "Lenovo ThinkPad Lab", 80.0, 32, true));
        equipmentList.add(new LaptopSet("E002", "Dell XPS Demo", 100.0, 16, false));
        equipmentList.add(new CameraKit("E003", "Sony Content Kit", 90.0, 3, true));
        equipmentList.add(new CameraKit("E004", "Canon Interview Kit", 70.0, 1, true));
    }

    public void displayStudents() {
        System.out.println("--- Students ---");
        for (Student s : students) {
            System.out.println(s.toString());
        }
    }

    public void displayEquipment() {
        System.out.println("--- Equipment ---");
        for (Equipment e : equipmentList) {
            System.out.println(e.getDisplayText());
        }
    }

    public void createReservation(String studentId, String equipmentId, int days) throws ReservationException {
        if (days < 1 || days > 14) {
            throw new ReservationException("Number of days must be between 1 and 14.");
        }

        Student student = students.stream()
                .filter(s -> s.getId().equalsIgnoreCase(studentId))
                .findFirst()
                .orElseThrow(() -> new ReservationException("Student not found."));

        Equipment equipment = equipmentList.stream()
                .filter(e -> e.getId().equalsIgnoreCase(equipmentId))
                .findFirst()
                .orElseThrow(() -> new ReservationException("Equipment not found."));

        if (!equipment.isAvailable()) {
            throw new ReservationException("Equipment " + equipmentId + " is not available.");
        }

        String resId = String.format("R%03d", reservationCounter++);
        Reservation reservation = new Reservation(resId, student, equipment, days, discountPolicy);
        equipment.setAvailable(false);
        reservations.add(reservation);

        System.out.printf("Reservation %s created.\nEquipment: %s\nCost: %.2f PLN\nStatus: %s\n",
                resId, equipment.getName(), reservation.getTotalCost(), reservation.getStatus());
    }

    public void returnEquipment(String reservationId) throws ReservationException {
        Reservation reservation = reservations.stream()
                .filter(r -> r.getId().equalsIgnoreCase(reservationId))
                .findFirst()
                .orElseThrow(() -> new ReservationException("Reservation not found."));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new ReservationException("Reservation is not active.");
        }

        reservation.completeReservation();
        reservation.getEquipment().setAvailable(true);

        int earnedPoints = (int) (reservation.getTotalCost() / 10);
        reservation.getStudent().addLoyaltyPoints(earnedPoints);

        System.out.printf("Equipment returned. The student received %d loyalty points.\n", earnedPoints);
    }

    public void displayActiveReservations() {
        System.out.println("--- Active Reservations ---");
        reservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .forEach(r -> System.out.println(r.getDisplayText()));
    }

    public void printReport() {
        System.out.println("--- Report ---");
        double totalRevenue = 0;
        System.out.println("Completed Reservations:");

        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.RETURNED) {
                System.out.println(r.getDisplayText());
                totalRevenue += r.getTotalCost();
            }
        }

        System.out.printf("Total revenue from completed reservations: %.2f PLN\n", totalRevenue);

        Student topStudent = students.get(0);
        for (Student s : students) {
            if (s.getLoyaltyPoints() > topStudent.getLoyaltyPoints()) {
                topStudent = s;
            }
        }
        System.out.println("Top student: " + topStudent.getFullName() + " with " + topStudent.getLoyaltyPoints() + " points.");
    }
}