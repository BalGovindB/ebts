package etbs;

public class Ticket {
    private int ticketId;
    private int quantity;
    private double price;

    public Ticket(int ticketId, int quantity, double price) {
        this.ticketId = ticketId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
