import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DiscountPolicy discountPolicy = new LoyaltyDiscountPolicy();
        ReservationService service = new ReservationService(discountPolicy);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to the MediaLab Reservation System");

        while (running) {
            System.out.println("\n1. Display students\n2. Display equipment\n3. Create reservation");
            System.out.println("4. Return equipment\n5. Show active reservations\n6. Show report\n0. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        service.displayStudents();
                        break;
                    case "2":
                        service.displayEquipment();
                        break;
                    case "3":
                        System.out.print("Enter student id: ");
                        String sId = scanner.nextLine();
                        System.out.print("Enter equipment id: ");
                        String eId = scanner.nextLine();
                        System.out.print("Enter number of days: ");
                        int days = Integer.parseInt(scanner.nextLine());
                        service.createReservation(sId, eId, days);
                        break;
                    case "4":
                        System.out.print("Enter reservation id: ");
                        String rId = scanner.nextLine();
                        service.returnEquipment(rId);
                        break;
                    case "5":
                        service.displayActiveReservations();
                        break;
                    case "6":
                        service.printReport();
                        break;
                    case "0":
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format.");
            } catch (ReservationException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
