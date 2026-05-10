package etbs;

public class Booking implements Bookable {
    private String bookingId;
    private User user;
    private Event event;
    private Ticket ticket;
    private int numTickets;

    public Booking(String bookingId, User user, Event event, int numTickets, double totalPrice) {
        this.bookingId = bookingId;
        this.user = user;
        this.event = event;
        this.numTickets = numTickets;
        this.ticket = new Ticket(Integer.parseInt(bookingId), numTickets, totalPrice);
    }

    @Override
    public void bookTicket() {
        if (event.getSeats() >= numTickets) {
            event.decreaseSeats(numTickets);
            System.out.println("Booking successful!");
        } else {
            System.out.println("Not enough seats available!");
        }
    }

    public void cancelBooking() {
        event.increaseSeats(numTickets);
        System.out.println("Booking cancelled");
    }

    public int getNumTickets() {
        return numTickets;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

    public Ticket getTicket() {
        return ticket;
    }
}
