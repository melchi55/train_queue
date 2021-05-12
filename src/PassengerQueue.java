import java.io.*;
import java.util.ArrayList;

public class PassengerQueue {
    private Passenger[] queueArray = new Passenger[42];
    private int[] processTime ;
    private int maxWaitingTime;
    private int minWaitingTime;
    private int averageWaitingTime;
    private ArrayList<String> report;
    private ArrayList<Integer> secondsInQueue ;

    //constructor
    public PassengerQueue(){
        for(int x = 0 ;x < 42;x++){
            queueArray[x] = new Passenger();
        }
    }

    //add passenger to the queue
    public void addPassenger(Passenger passengerDetail){
        for(int x = 0 ;x < 42 ;x++){
            if(queueArray[x].getName()==null){
                queueArray[x] = passengerDetail;
                break;
            }
        }
    }

    //get the name using index
    public String getname(int index){
        String name = queueArray[index].getName();
        if (name == null){
            return "Empty";
        }
        else{
            return name;
        }
    }

    //get seat number using index
    public String getSeatNumber(int index){
        return queueArray[index].getSeatNo();
    }

    //passenger queue passenger count
    public int queueCount(){
        int count = 0;
        for(int x = 0 ;x < 42 ;x++){
            if(queueArray[x].getName() != null){
                count++;
            }
        }
        return count;
    }

    //delete customer from queue
    public void deletePassenger(int index){
        queueArray[index] = new Passenger();
        int count = queueCount();
        for(int x = 0 ; x < count ;x++ ){
            if(queueArray[x].getName() == null){
                queueArray[x] = queueArray[x+1];
                queueArray[x+1] = new Passenger();
            }
        }
    }

    //clear train queue
    public void clearTrainQueue(){
        queueArray = new Passenger[42];
    }

    //store data to database
    public void storeTrainQueue(String date , String datab){
        System.out.println("Data is getting stored...");
        Database database = new Database(date,datab);
        database.clear();
        database.addQueueData(queueArray);
        System.out.println("Data stored ....");
    }

    //load train queue details from database
    public void loadTrainQueue(String date , String datab){
        clearTrainQueue();
        System.out.println("Data is getting downloaded...");
        Database database = new Database(date,datab);
        queueArray = database.loadQueueData();
        System.out.println("Data downloaded ....");

    }

    //process time methods
    public void processTimeList(){
        int count= queueCount();
        processTime = new int[count];
        for(int x = 0;x < count;x++){
            processTime[x] = queueArray[x].getSecondsInQueue();
        }
        setSecondsInQueue();
        setAverageWaitingTime();
        setMaxWaitingTime();
        setMinWaitingTime();
        makeReport();
    }
    //total waiting time
    public int totalWaitingTime(){
        int count= queueCount();
        int total = 0;
        for(int x = 0;x < count;x++){
            total += processTime[x];
        }
        return total;
    }
    //average duration
    public int getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public void setAverageWaitingTime() {
        int total = totalWaitingTime();
        int count = queueCount();
        this.averageWaitingTime = total/count;
    }

    //max waiting time
    public int getMaxWaitingTime() {
        return maxWaitingTime;
    }

    //set max waiting time
    public void setMaxWaitingTime() {
        int max = processTime[0];
        for(int i = 1; i < processTime.length;i++)
        {
            if(processTime[i] > max)
            {
                max = processTime[i];
            }
        }
        maxWaitingTime = max;
    }
    //get max waiting time
    public int getMinWaitingTime() {
        return minWaitingTime;
    }

    //set min waiting time
    public void setMinWaitingTime() {
        int min = processTime[0];
        for(int i = 1; i < processTime.length;i++)
        {
            if(processTime[i] < min)
            {
                min = processTime[i];
            }
        }
        minWaitingTime = min;
    }

    //make report text file and list
    public void makeReport(){
        report =  new ArrayList<>();
        int count = queueCount();
        File file = new File("trainQueueReport.txt");
        try {
            PrintWriter output = new PrintWriter(file);
            for(int x = 0;x < count;x++){
                output.println("\nName :"+queueArray[x].getName()+"  "+
                        queueArray[x].getSeatNo()+"\nDuration in Queue : "+
                        getSecondsInQueue(x)+"sec" + "\nIndividual Boarding Duration : "+
                        queueArray[x].getSecondsInQueue()+"sec");
                report.add("\nName :"+queueArray[x].getName()+"  "+
                        queueArray[x].getSeatNo()+"\nDuration in Queue : "+
                        getSecondsInQueue(x)+"sec" + "\nIndividual Boarding Duration : "+
                        queueArray[x].getSecondsInQueue()+"sec");
            }
            output.println("\nMaximum length of the queue attained : " + queueCount());
            report.add("\nMaximum length of the queue attained : " + queueCount());
            output.println("Maximum Waiting time : " + getMaxWaitingTime());
            report.add("Maximum Waiting time : " + getMaxWaitingTime());
            output.println("Minimum waiting time : "+getMinWaitingTime());
            report.add("Minimum waiting time : "+getMinWaitingTime());
            output.println("Average waiting time : "+getAverageWaitingTime());
            report.add("Average waiting time : "+getAverageWaitingTime());
            output.close();
        }
        catch (IOException ex){
            System.out.printf("Error: %s\n",ex);
        }
    }

    //sec in queue
    public int getSecInQue(int index){
        int secInQue;
        secInQue = queueArray[index].getSecondsInQueue();
        return secInQue;
    }
    //get report array
    public ArrayList<String> getReportArray(){
        return report;
    }

    //set seconds in queue
    public void setSecondsInQueue(){
        secondsInQueue = new ArrayList<>();
        int count = queueCount();
        int tot = 0;
        for(int x = 0;x < count;x++){
            tot += queueArray[x].getSecondsInQueue();
            secondsInQueue.add(tot);
        }
    }

    //get seconds in queue
    public int getSecondsInQueue(int index){
        return secondsInQueue.get(index);
    }

}
