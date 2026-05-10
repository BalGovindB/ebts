package gui;

import etbs.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;

public class BookingUI extends Application {
    private EventCSVManager eventManager;
    private BookingCSVManager bookingManager;
    private List<Event> events;

    @Override
    public void start(Stage primaryStage) {
        eventManager = new EventCSVManager("events.csv");
        bookingManager = new BookingCSVManager("bookings.csv");
        events = eventManager.loadEvents();

        primaryStage.setTitle("ETBS - Event Ticket Booking System");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root-pane");

        Label titleLabel = new Label("Upcoming Events");
        titleLabel.getStyleClass().add("title-label");

        ListView<String> eventListView = new ListView<>();
        for (Event e : events) {
            eventListView.getItems().add(formatEventString(e));
        }
        eventListView.getStyleClass().add("event-list");

        GridPane formPane = new GridPane();
        formPane.setHgap(15);
        formPane.setVgap(15);
        formPane.setAlignment(Pos.CENTER_LEFT);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name");
        nameField.getStyleClass().add("text-field");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter your phone");
        phoneField.getStyleClass().add("text-field");

        TextField ageField = new TextField();
        ageField.setPromptText("Enter your age");
        ageField.getStyleClass().add("text-field");

        TextField seatField = new TextField();
        seatField.setPromptText("Enter seat number");
        seatField.getStyleClass().add("text-field");

        formPane.add(new Label("Name:"), 0, 0);
        formPane.add(nameField, 1, 0);
        formPane.add(new Label("Phone:"), 0, 1);
        formPane.add(phoneField, 1, 1);
        formPane.add(new Label("Age:"), 0, 2);
        formPane.add(ageField, 1, 2);
        formPane.add(new Label("Seat:"), 0, 3);
        formPane.add(seatField, 1, 3);

        Button bookButton = new Button("Book Ticket");
        bookButton.getStyleClass().add("book-button");
        bookButton.setMaxWidth(Double.MAX_VALUE);

        bookButton.setOnAction(e -> {
            int selectedIdx = eventListView.getSelectionModel().getSelectedIndex();
            if (selectedIdx < 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select an event.");
                return;
            }
            try {
                String name = nameField.getText();
                if (name == null || name.trim().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter your name.");
                    return;
                }
                
                int phone = Integer.parseInt(phoneField.getText());
                int age = Integer.parseInt(ageField.getText());
                int seat = Integer.parseInt(seatField.getText());

                Event selectedEvent = events.get(selectedIdx);
                if (selectedEvent.getSeats() <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Sold Out", "No seats available for this event.");
                    return;
                }

                User user = new User(name, phone, age);
                String bookingId = String.valueOf(System.currentTimeMillis() % 100000);
                Booking booking = new Booking(bookingId, user, selectedEvent, seat, selectedEvent.getPrice());

                booking.bookTicket();
                bookingManager.addBooking(bookingId, user.getName(), selectedEvent.getEventId(), seat, selectedEvent.getPrice());
                eventManager.saveEvents(events);

                events = eventManager.loadEvents(); 
                eventListView.getItems().set(selectedIdx, formatEventString(events.get(selectedIdx)));

                showAlert(Alert.AlertType.INFORMATION, "Success", "Booking confirmed! ID: " + bookingId);
                
                nameField.clear(); 
                phoneField.clear(); 
                ageField.clear(); 
                seatField.clear();

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values for phone, age, and seat.");
            }
        });

        root.getChildren().addAll(titleLabel, eventListView, formPane, bookButton);

        Scene scene = new Scene(root, 480, 650);
        
        File cssFile = new File("src/gui/styles.css");
        if (cssFile.exists()) {
            scene.getStylesheets().add(cssFile.toURI().toString());
        }
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private String formatEventString(Event e) {
        return String.format("[%s] %-20s at %-15s - %-10d ($%.2f)", 
                e.getType(), e.getEventName(), e.getVenue(), e.getSeats(), e.getPrice());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
