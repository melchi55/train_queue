import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class TrainStation extends Application{

    private static final int SEATING_CAPACITY = 42;
    private  Passenger[] waitingRoom ;
    private final Scanner scanner = new Scanner(System.in);
    private  String route ;
    private  String date ;
    private  PassengerQueue trainQueue ;
    private ArrayList<String> bookedNamesOnly;
    private String[] bookedNames;

    //main
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        selectingTheRoute();
    }

    //selecting the Route
    public void selectingTheRoute(){
        System.out.print(" \n...Denuwara Menike train ..." +
                " \nColombo to Badulla       (C)" +
                " \nBadulla to Colombo       (B)" +
                " \nSelect the Route            " +
                " \n:- ");
        String input = scanner.next().toLowerCase();

        switch (input){
            case "c":
                route = "colomboToBadulla";
                break;
            case "b" :
                route = "badullaToColombo";
                break;
            default:
                System.out.println("Invalid input");
                selectingTheRoute();
        }
        trainQueue = new PassengerQueue();
        waitingRoom = new Passenger[SEATING_CAPACITY];
        selectThedate();
        System.out.println("Please wait .... loading the booked details from database..");
        addToWaiting();
        System.out.println("Data loaded from the Database");
        menu();
    }

    //selecting the FutureDate
    public  void selectFutureDate()
    {
        int fdate = 0;
        int fmonth= 0;
        int fyear = 0;


        System.out.print("Enter a date (dd/MM/yy) : ");
        String futureDate = scanner.next().trim();
        try {
            fdate = Integer.parseInt(futureDate.substring(0, 2));
            fmonth = Integer.parseInt(futureDate.substring(3, 5));
            fyear = Integer.parseInt(futureDate.substring(6, 8));
        }
        catch (Exception e){
            System.out.println("Invalid Date or Check the Date format.");
        }

        if(fdate <= 31 & fmonth <=12 ) {

            DateFormat df = new SimpleDateFormat("dd/MM/yy");
            Date dateobj = new Date();
            String todayDate = (df.format(dateobj));

            int todate = Integer.parseInt(todayDate.substring(0, 2));
            int tomonth = Integer.parseInt(todayDate.substring(3, 5));
            int toyear = Integer.parseInt(todayDate.substring(6, 8));

            if ((fdate > todate) & fmonth >= tomonth & fyear >= toyear) {
                date = futureDate;
            } else {
                System.out.println("--- Enter a Future date");
                selectFutureDate();
            }
        }
        else{
            System.out.println("Invalid Date Found , Select a correct Date.");
            selectThedate();
        }
    }

    //get current date
    public void getCurrentDate(){
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateobj = new Date();
        date = (df.format(dateobj));
    }


    //selecting the date
    public void selectThedate(){
        System.out.print("\nToday  Date   (T) " +
                "\nFuture Date   (F) " +
                "\nSelected Date (S) " +
                "\n:- ");
        String input = scanner.next().toLowerCase().trim();
        switch (input){
            case "t":
                getCurrentDate();
                break;
            case "f":
                selectFutureDate();
                break;
            case "s":
                if(date==null){
                    System.out.println("Date is not selected..");
                    selectThedate();
                }
                break;
            default:
                System.out.println("Invalid input..");
                selectThedate();
        }
    }


    //menu
    private void menu(){
        int queueCount;
        System.out.print("\nAdd a passenger to the trainQueue     (A)" +
                "\nView the trainQueue                   (V)" +
                "\nDelete passenger from the trainQueue  (D)" +
                "\nStore trainQueue data                 (S)" +
                "\nLoad data back from the file          (L)" +
                "\nRun the simulation and produce report (R)" +
                "\nSelect the Route                      (SR)" +
                "\nQuit the Programme                    (Q)  "+
                "\n:- " );

        String input = scanner.next().trim().toLowerCase();
        switch (input){
            case "a":
                int count = waitingListPassengerCount();
                if(count!=0) {
                    addPassengersByRandom();
                    count = waitingListPassengerCount();
                    if (count == 0) {
                        viewTheTrainQueue();
                    } else {
                        addPassengerToTrainQueue();
                    }
                }
                else {
                    System.out.println("Waiting room is Empty ...");
                    menu();
                }
                break;
            case "v":
                queueCount = trainQueue.queueCount();
                if(queueCount != 0) {
                    viewTheTrainQueue();
                }
                else {
                    System.out.println("Train Queue slots are Empty..");
                    menu();
                }
                break;
            case "d":
                queueCount = trainQueue.queueCount();
                if(queueCount != 0) {
                    deletePassenger();
                }
                else {
                    System.out.println("Train Queue slots are Empty..");
                    menu();
                }
                break;
            case "s":
                queueCount = trainQueue.queueCount();
                if(queueCount != 0) {
                    storeTrainQueue();
                }
                else {
                    System.out.println("Train Queue slots are Empty..");
                    menu();
                }
                break;
            case "l":
                loadTrainQueue();
                break;
            case "r":
                queueCount = trainQueue.queueCount();
                if(queueCount != 0) {
                    simulateTheReport();
                }
                else {
                    System.out.println("Train Queue slots are Empty..");
                    menu();
                }
                break;
            case "q":
                System.exit(0);
                break;
            case "sr":
                selectingTheRoute();
                break;
            default:
                System.out.println("invalid Input");
                menu();
        }
    }

    //Load from the database then add to the Waiting Passenger list
    public void loadTheDataFromDatabase(){
        Database database = new Database(date,route);
        bookedNames =  database.load();
    }

    //Load from the database then add to the Waiting Passenger list
    public void addToWaiting(){
        loadTheDataFromDatabase();
        for(int x = 0; x < SEATING_CAPACITY;x++){
            waitingRoom[x] = new Passenger();
            waitingRoom[x].setName(bookedNames[x]);
            waitingRoom[x].setSeatNo(x+1);
            waitingRoom[x].setSecondsInQueue(generateWaitingTime());
        }
    }



    //display the waiting room and trainQueue details
    public void addPassengerToTrainQueue(){

        Stage window = new Stage();
        window.setMaxHeight(1500);
        window.setMaxWidth(1800);
        window.setTitle("Add Passenger to the Train Queue");
        Label label1 = new Label("Remaining Passengers in Waiting room");
        label1.setPrefHeight(70);
        label1.setPrefWidth(500);
        window.setResizable(false);
        label1.setFont(new Font("Verdana", 15));
        label1.setAlignment(Pos.CENTER);
        int count = waitingListPassengerCount();
        Label[] waitingListNames = new Label[count];
        for(int x = 0 ;x < count;x++){
            waitingListOnlyBookedNames();
            waitingListNames[x] = new Label();
            waitingListNames[x].setPrefHeight(20);
            waitingListNames[x].setPrefWidth(200);
            waitingListNames[x].styleProperty().set("-fx-background-color:LightGray");
            waitingListNames[x].setFont(new Font("Verdana", 10));
            waitingListNames[x].setText( (x+1) + ". "+ bookedNamesOnly.get(x) + " " + getSeatNumber(bookedNamesOnly.get(x)));
        }

        GridPane grid1 = new GridPane();
        int r = 0;
        int c = 0;
        for (Label label : waitingListNames) {
            if (c == 3) {
                r++;
                c = 0;
            }
            c++;
            grid1.add(label, c, r);
        }

        GridPane grid2 = new GridPane();
        Label label2 = new Label("Passenger Queue Slots According to Seat");
        label2.setPrefHeight(70);
        label2.setPrefWidth(500);
        label2.setFont(new Font("Verdana", 20));
        label2.setAlignment(Pos.CENTER);
        Label[] queueLabels = new Label[SEATING_CAPACITY];
        for(int x = 0;x < SEATING_CAPACITY;x++){
            queueLabels[x] = new Label();
            queueLabels[x].setText((x+1) + ".  "+ trainQueue.getname(x) + " " + trainQueue.getSeatNumber(x));
            queueLabels[x].setPrefHeight(50);
            queueLabels[x].setPrefWidth(260);
            queueLabels[x].styleProperty().set("-fx-background-color:LightGray");
            queueLabels[x].setFont(new Font("Verdana", 14));
        }
        int r1 = 0;
        int c1 = 0;
        for (Label label : queueLabels) {
            if (c1 == 3) {
                r1++;
                c1 = 0;
            }
            c1++;
            grid2.add(label, c1, r1);
        }
        Button goToMenu = new Button("Go To Menu");
        goToMenu.setStyle("-fx-background-color:White");
        goToMenu.setOnAction(e ->{
            window.close();
            menu();
        });
        grid1.setHgap(12);
        grid1.setVgap(4);
        grid2.setHgap(12);
        grid2.setVgap(7);
        VBox box1 = new VBox(25);
        box1.getChildren().addAll(label1,grid1,goToMenu);
        box1.setAlignment(Pos.CENTER);
        grid1.setAlignment(Pos.CENTER);

        VBox box2 = new VBox(25);
        box2.getChildren().addAll(label2,grid2);
        box2.setAlignment(Pos.CENTER);
        grid2.setAlignment(Pos.CENTER);

        HBox box3 = new HBox(40);
        box3.setAlignment(Pos.CENTER);
        box3.getChildren().addAll(box1,box2);
        box3.styleProperty().set("-fx-background-color:Gray");
        Scene scene = new Scene(box3,1800,1500);
        window.setScene(scene);
        window.show();
    }

    //train queue GUI
    public void viewTheTrainQueue(){
        Stage window = new Stage();
        window.setTitle("View the Train Queue");
        window.setMaxHeight(1000);
        window.setMaxWidth(1200);
        Label label1 = new Label("Train Queue Slots - Name and Seat Number ");
        label1.setFont(new Font("Verdana", 20));
        Button goToMenu = new Button("Go To Menu");
        Label[] queueLabels = new Label[SEATING_CAPACITY];
        GridPane grid = new GridPane();
        for(int x = 0;x < SEATING_CAPACITY;x++){
            queueLabels[x] = new Label();
            queueLabels[x].setText((x+1) + ". "+ trainQueue.getname(x) + trainQueue.getSeatNumber(x));
            queueLabels[x].setPrefHeight(50);
            queueLabels[x].setPrefWidth(260);
            queueLabels[x].styleProperty().set("-fx-background-color:LightGray");
            queueLabels[x].setFont(new Font("Verdana", 14));
        }
        int r1 = 0;
        int c1 = 0;
        for (Label label : queueLabels) {
            if (c1 == 3) {
                r1++;
                c1 = 0;
            }
            c1++;
            grid.add(label, c1, r1);
        }

        grid.setHgap(12);
        grid.setVgap(7);

        goToMenu.setOnAction(e ->{
            window.close();
            menu();
        });
        goToMenu.setStyle("-fx-background-color:White");
        label1.setAlignment(Pos.CENTER);
        goToMenu.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER);
        VBox box1 = new VBox(30);
        box1.getChildren().addAll(label1,grid,goToMenu);
        box1.setAlignment(Pos.CENTER);
        box1.styleProperty().set("-fx-background-color:Gray");
        Scene scene = new Scene(box1,1200,1000);
        window.setScene(scene);
        window.show();
    }

    //count for waiting list
    public int waitingListPassengerCount(){
        int count = 0;
        for(int x = 0;x < SEATING_CAPACITY;x++){
            if(waitingRoom[x].getName() != null){
                count++;
            }
        }
        return count;
    }

    //waiting list only booked names
    public void waitingListOnlyBookedNames(){
        bookedNamesOnly = new ArrayList<>();
        for(int x = 0;x < SEATING_CAPACITY;x++){
            if(waitingRoom[x].getName() != null){
                bookedNamesOnly.add(waitingRoom[x].getName());
            }
        }
    }

    //add passengers randomly to the queue from waiting list
    public void addPassengersByRandom(){
        Random random = new Random();
        int randPassengerCount = (random.nextInt(6) + 1);
        for(int y = 0 ; y < randPassengerCount;y++) {
            for (int x = 0; x < SEATING_CAPACITY; x++) {
                if (waitingRoom[x].getName() != null) {
                    trainQueue.addPassenger(waitingRoom[x]);
                    waitingRoom[x] = new Passenger();
                    break;
                }
            }
        }

        System.out.println("Passengers added to the Train Queue");
    }

    //get seat number for names
    public String getSeatNumber(String name) {
        String seatNum = "";
        for(int x = 0 ;x < SEATING_CAPACITY;x++) {
            if (name.equals(waitingRoom[x].getName())) {
                seatNum =  waitingRoom[x].getSeatNo();
            }
        }
        return seatNum;
    }

    //delete customer from queue
    public void deletePassenger(){
        int count = 0;
        System.out.print("Enter Passenger name : ");
        String name = scanner.next().toLowerCase().trim();
        for(int x = 0;x < SEATING_CAPACITY;x++){
            if(trainQueue.getname(x).equals(name)){
                count++;
                trainQueue.deletePassenger(x);
                System.out.println(name + " " + "has Deleted from the Train Queue");
                break;
            }
        }
        if (count == 0){
            System.out.println("Passenger Name not Found ..");
        }
        menu();
    }

    //store the train queue data to mongoDB
    public void storeTrainQueue(){
        String database ;
        if(route.equals("colomboToBadulla")){
            database = "colomboToBadullaQueue";
        }
        else {
            database = "badullaToColomboQueue";
        }
        trainQueue.storeTrainQueue(date,database);
        menu();
    }


    //load from mongodb(train queue details)
    public void loadTrainQueue(){
        for(int x = 0;x < SEATING_CAPACITY;x++){
            waitingRoom[x] = new Passenger();
        }
        String database ;
        if(route.equals("colomboToBadulla")){
            database = "colomboToBadullaQueue";
        }
        else {
            database = "badullaToColomboQueue";
        }
        trainQueue.loadTrainQueue(date,database);
        menu();
    }


    //generate waiting time for each passengers
    public int generateWaitingTime(){
        Random random = new Random();
        int waitingTime = 0;
        for(int x = 0 ;x < 3 ;x++) {
            int sec = (random.nextInt(6) + 1);
            waitingTime += sec;
        }
        return waitingTime;
    }

    //simulate the report
    public void simulateTheReport(){
        trainQueue.processTimeList();
        simulationAndReport();
    }

    //simulate the report
    public void simulationAndReport() {
        int count = trainQueue.queueCount();
        if(count != 0){
            System.out.println("\n--------- Please wait Ticket is getting checked for " +
                    trainQueue.getname(0)+"------------" );
            try{Thread.sleep(trainQueue.getSecInQue(0) * 1000);}
            catch(InterruptedException ignored){}
            System.out.println(trainQueue.getname(0) + " " +
                          "removed from the Passenger Queue and boarded to the Train ");
            trainQueue.deletePassenger(0);
            simulationAndReport();
        }
        else {
            System.out.println("\n-------- Simulation Report is ready ---------- ");
            makeReportGui();
        }
    }

    //simulate the report
    public void makeReportGui(){
        ArrayList<String> report = trainQueue.getReportArray();
        Stage window = new Stage();
        window.setTitle("Train simulation Report");
        Label labelHead = new Label("Simulation details");
        window.setResizable(true);
        labelHead.setPrefHeight(70);
        labelHead.setPrefWidth(500);
        labelHead.setFont(new Font("Verdana", 20));
        labelHead.setAlignment(Pos.CENTER);
        Label[] lines = new Label[report.size()];
        GridPane gridPane = new GridPane();
        for(int x = 0;x < report.size();x++){
            lines[x] = new Label();
            lines[x].setText(report.get(x));
            lines[x].setPrefHeight(70);
            lines[x].setPrefWidth(300);
            lines[x].styleProperty().set("-fx-background-color:LightGray");
            lines[x].setFont(new Font("Verdana", 14));
        }
        int r1 = 0;
        int c1 = 0;
        for (Label label : lines) {
            if (c1 == 3) {
                r1++;
                c1 = 0;
            }
            c1++;
            gridPane.add(label, c1, r1);
        }
        gridPane.setHgap(12);
        gridPane.setVgap(7);
        Button goToMenu = new Button("Go to menu");
        goToMenu.setStyle("-fx-background-color:white");
        goToMenu.setOnAction(e ->{
            window.close();
            menu();
        });
        gridPane.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(30);
        vBox.getChildren().addAll(labelHead,gridPane,goToMenu);
        vBox.setAlignment(Pos.CENTER);
        vBox.styleProperty().set("-fx-background-color:Gray");
        Scene scene = new Scene(vBox,1000,500);
        window.setScene(scene);
        window.show();

    }

}
