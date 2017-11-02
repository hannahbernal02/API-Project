package egr327project;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;


@Component
public class MyTasks {

    RestTemplate restTemplate = new RestTemplate();
    private int id = 1;
    private Random rand;

    //get request
    private void getGreeting() {
        String getUrl = "http://localhost:8080/greeting";
        Greeting g = restTemplate.getForObject(getUrl, Greeting.class);
        System.out.println(g.getContent());
    }


    //put request
    @Scheduled(cron="*/4 * * * * *")
    public void updateGreeting() {
        String url = "http://localhost:8080/updateGreeting";
        restTemplate.put(url, "Bye World", Greeting.class);
        getGreeting();
    }

    @Scheduled(cron="*/5 * * * * *")
    public void secondUpdateGreeting() {
        String url = "http://localhost:8080/updateGreeting";
        restTemplate.put(url, "Hello World", Greeting.class);
        getGreeting();
    }

    //post
    @Scheduled (cron="*/3 * * * * *")
    public void addVehicle() {
        String url = "http://localhost:8080/addVehicle";
        rand = new Random();
        //97-122
        String randModel = Integer.toString(rand.nextInt() * 122 + 97);
        restTemplate.put(url, new Vehicle(++id, randModel, 1986, 15000));
    }

    //delete
    @Scheduled (cron="*/3 * * * * *")
    public void deleteVehicle() {
        //remember to add an id in the url
        String url = "http://localhost:8080/deleteVehicle";
        restTemplate.delete(url);
    }

    //put
    @Scheduled (cron="*/3 * * * * *")
    public void updateVehicle() {
        String url = "http://localhost:8080/updateVehicle";
        restTemplate.put(url, new Vehicle(1, "Updated Vehicle", 99999, 123456), Vehicle.class);
        //remember same id
        restTemplate.getForObject("http://localhost:8080/getVehicle", Vehicle.class);
    }

    @Scheduled(cron="*/3 * * * * *")
    public void latestVehiclesReport() {
        //remember id
        List<Vehicle> vehicleList = restTemplate.getForObject("http://localhost:8080/getLatestVehicles", List.class);
        System.out.println("latest is: ");
        //loop to print
        System.out.println(vehicleList.get(0));
    }
}