public class Passenger {

    private String name;
    private int secondsInQueue;
    private int seatNo;

    //get name
    public String getName() {
        return name;
    }

    //set name
    public void setName(String name) {
        this.name = name;
    }

    public int getSecondsInQueue() {
        return secondsInQueue;
    }

    public void setSecondsInQueue(int secondsInQueue) {
        this.secondsInQueue = secondsInQueue;
    }

    public String getSeatNo() {
        if(name == null){
            return "";
        }
        else {
            return " Seat number : " + seatNo ;
        }
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public int getSeat(){
        return seatNo;
    }
}
