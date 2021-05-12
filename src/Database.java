import com.mongodb.BasicDBObject;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {

    public static MongoClient mongoClient;
    public static MongoDatabase mongoDatabase;
    public static MongoCollection<Document> mongolCollection;

    public Database(String date, String database){

        mongoClient = MongoClients.create("mongodb+srv://zedek:1234@cluster0-xhkmw.mongodb.net/test?retryWrites=true&w=majority");
        MongoCredential.createCredential("zedek",database,"1234".toCharArray());
        mongoDatabase = mongoClient.getDatabase(database);
        mongolCollection = mongoDatabase.getCollection(date);
        Logger mLog = Logger.getLogger("org.mongodb.driver");
        mLog.setLevel(Level.WARNING);
    }

    //add queue details to database
    public void addQueueData(Passenger[] queue){
        for(int x = 0;x < 42;x++){
            Document document = new Document();
            document.append("name",queue[x].getName());
            document.append("seatNo",queue[x].getSeat());
            document.append("secondsInQueue",queue[x].getSecondsInQueue());
            mongolCollection.insertOne(document);
        }
    }

    //loading the names from the database to array
    public Passenger[] loadQueueData(){
        Passenger[] queue = new Passenger[42];
        String name;
        int seat;
        int secondsInQueue;
        int x = 0;
        for (Document document : mongolCollection.find()){
            queue[x] = new Passenger();
            name = document.getString("name");
            queue[x].setName(name);
            seat = document.getInteger("seatNo");
            queue[x].setSeatNo(seat);
            secondsInQueue = document.getInteger("secondsInQueue");
            queue[x++].setSecondsInQueue(secondsInQueue);
        }
        return queue;
    }

    //loading the names from the database to array
    public String[] load(){
        String[] datas = new String[42];
        int x = 0;
        for (Document document : mongolCollection.find()){
            datas[x++] = document.getString("name");
        }
        return datas;
    }

    //clearing the collection
    public void clear(){
        BasicDBObject document = new BasicDBObject();
        mongolCollection.deleteMany(document);
    }

}
