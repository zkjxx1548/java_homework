package exception;

public class InvalidTicketException extends RuntimeException {
    public InvalidTicketException() {
        super("很抱歉，无法通过您提供的停车券为您找到相应的车辆，请您再次核对停车券是否有效！");
    }
}
