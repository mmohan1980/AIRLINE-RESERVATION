import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Airline {
    private static final int TOTAL_SEATS = 10;
    private static boolean[] seats = new boolean[TOTAL_SEATS];
    private static String[] names = new String[TOTAL_SEATS];
    private static String[] phoneNumbers = new String[TOTAL_SEATS];
    private static String[] bookingIds = new String[TOTAL_SEATS];

    public static void main(String[] args) {
        JFrame frame = new JFrame("Airline Reservation System");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Components for booking
        JPanel bookingPanel = new JPanel();
        bookingPanel.setLayout(new FlowLayout());
        JLabel seatLabel = new JLabel("Enter seat number (1-10):");
        JTextField seatField = new JTextField(2);
        JLabel nameLabel = new JLabel("Enter your name:");
        JTextField nameField = new JTextField(10);
        JLabel phoneLabel = new JLabel("Enter your phone number:");
        JTextField phoneField = new JTextField(10);
        JButton bookButton = new JButton("Book");
        bookingPanel.add(seatLabel);
        bookingPanel.add(seatField);
        bookingPanel.add(nameLabel);
        bookingPanel.add(nameField);
        bookingPanel.add(phoneLabel);
        bookingPanel.add(phoneField);
        bookingPanel.add(bookButton);

        // Components for displaying available seats
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new FlowLayout());
        JButton displayButton = new JButton("Display Available Seats");
        JTextArea displayArea = new JTextArea(5, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayPanel.add(displayButton);
        displayPanel.add(scrollPane);

        // Components for canceling reservation
        JPanel cancelPanel = new JPanel();
        cancelPanel.setLayout(new FlowLayout());
        JLabel cancelLabel = new JLabel("Enter Booking ID to cancel:");
        JTextField cancelField = new JTextField(5);
        JButton cancelButton = new JButton("Cancel");
        cancelPanel.add(cancelLabel);
        cancelPanel.add(cancelField);
        cancelPanel.add(cancelButton);

        // Exit button
        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new FlowLayout());
        JButton exitButton = new JButton("Exit");
        exitPanel.add(exitButton);

        // Add panels to frame
        frame.add(bookingPanel);
        frame.add(displayPanel);
        frame.add(cancelPanel);
        frame.add(exitPanel);

        // Add action listeners
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int seatNumber = Integer.parseInt(seatField.getText());
                    String name = nameField.getText();
                    String phoneNumber = phoneField.getText();
                    if (seatNumber < 1 || seatNumber > TOTAL_SEATS) {
                        displayArea.setText("Invalid seat number. Please try again.");
                    } else if (seats[seatNumber - 1]) {
                        displayArea.setText("Seat " + seatNumber + " is already booked.");
                    } else if (name.isEmpty() || phoneNumber.isEmpty() || !isValidPhoneNumber(phoneNumber)) {
                        displayArea.setText("Please enter a valid name and 10-digit phone number.");
                    } else {
                        String bookingId = generateBookingId();

                        seats[seatNumber - 1] = true;
                        names[seatNumber - 1] = name;
                        phoneNumbers[seatNumber - 1] = phoneNumber;
                        bookingIds[seatNumber - 1] = bookingId;

                        // Display booking details in a separate dialog box
                        JOptionPane.showMessageDialog(frame,
                                "Seat " + seatNumber + " has been successfully booked for " + name + ".\nBooking ID: " + bookingId,
                                "Booking Confirmation",
                                JOptionPane.INFORMATION_MESSAGE);

                        displayArea.setText("Seat " + seatNumber + " has been successfully booked for " + name + ".\nBooking ID: " + bookingId);
                    }
                    seatField.setText("");
                    nameField.setText("");
                    phoneField.setText("");
                } catch (NumberFormatException ex) {
                    displayArea.setText("Please enter a valid seat number.");
                }
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder availableSeats = new StringBuilder("Available Seats:\n");
                boolean available = false;
                for (int i = 0; i < TOTAL_SEATS; i++) {
                    if (!seats[i]) {
                        availableSeats.append((i + 1)).append(" ");
                        available = true;
                    }
                }
                if (!available) {
                    availableSeats.append("No seats available.");
                }
                displayArea.setText(availableSeats.toString());
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bookingId = cancelField.getText();
                boolean found = false;
                for (int i = 0; i < TOTAL_SEATS; i++) {
                    if (bookingIds[i] != null && bookingIds[i].equals(bookingId)) {
                        seats[i] = false;
                        names[i] = null;
                        phoneNumbers[i] = null;
                        bookingIds[i] = null;
                        displayArea.setText("Reservation for seat " + (i + 1) + " has been canceled.");
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    displayArea.setText("Invalid Booking ID. Please try again.");
                }
                cancelField.setText("");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private static String generateBookingId() {
        Random random = new Random();
        int id;
        do {
            id = 10000 + random.nextInt(90000); // Generate a number between 10000 and 99999
        } while (isBookingIdExists(String.valueOf(id)));
        return String.valueOf(id);
    }

    private static boolean isBookingIdExists(String id) {
        for (String existingId : bookingIds) {
            if (id.equals(existingId)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }
}