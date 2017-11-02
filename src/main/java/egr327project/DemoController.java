package egr327project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


    @RestController
    public class DemoController {

        private static int id = 1;

        @RequestMapping(value = "/greeting", method = RequestMethod.GET)
        public Greeting greeting() throws IOException {
            //read from text file and deserialize it into Greeting object
            ObjectMapper mapper = new ObjectMapper();
            Greeting greeting = mapper.readValue(new File("./message.txt"), Greeting.class);
            return greeting;
        }

        @RequestMapping(value = "/createGreeting", method = RequestMethod.POST)
        public Greeting createGreeting(@RequestBody String name) throws IOException {
            if (name == null) {
                return null;
            }
            //create a greeting, serialize it into text file
            ObjectMapper mapper = new ObjectMapper();
            Greeting newGreeting = new Greeting(id++, name);
            mapper.writeValue(new File("./message.txt"), newGreeting);
            return newGreeting;
        }

        @RequestMapping(value = "/updateGreeting", method = RequestMethod.PUT)
        public Greeting updateGreeting(@RequestBody String newMessage) throws IOException {
            if (newMessage == null) {
                return null;
            }
            //deserialize from file, read as Greeting, update greeting, serialize it back into file
            ObjectMapper mapper = new ObjectMapper();
            String message = FileUtils.readFileToString(new File("./message.txt"), StandardCharsets.UTF_8.name());
            Greeting greeting = mapper.readValue(message, Greeting.class);
            greeting.setContent(newMessage);
            mapper.writeValue(new File("./message.txt"), greeting);
//        mapper.writeValue(new File("./message.txt"), newMessage);
            return greeting;
        }

        @RequestMapping(value = "/deleteGreeting", method = RequestMethod.DELETE)
        public void deletedGreeting(@RequestBody int id) throws IOException {
            //deserialize; if greeting key = given key
            ObjectMapper mapper = new ObjectMapper();
            String message = FileUtils.readFileToString(new File("./message.txt"), StandardCharsets.UTF_8.name());
            Greeting greeting = mapper.readValue(message, Greeting.class);
            if (greeting.getId() == id) {
                FileUtils.writeStringToFile(new File("./message.txt"), "", StandardCharsets.UTF_8.name());
            }
        }







        /**PART 1**/

        /**WORKS!**/
        @RequestMapping(value="/addVehicle", method=RequestMethod.POST)
        public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            FileWriter output = new FileWriter("./inventory.txt", true);
            mapper.writeValue(output, newVehicle);
            FileUtils.writeStringToFile(new File("./inventory.txt"), System.lineSeparator(), StandardCharsets.UTF_8.name(), true);
            return newVehicle;
        }

        /**WORKS!**/
        @RequestMapping(value="/getVehicle/{id}", method=RequestMethod.GET)
        public Vehicle getVehicle(@PathVariable("id") int id) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            //iterate local file line-by-line
            LineIterator it = FileUtils.lineIterator(new File("./inventory.txt"), StandardCharsets.UTF_8.name());
            while (it.hasNext()) {
                Vehicle someVehicle = mapper.readValue(it.next(), Vehicle.class);
                //check if id matches
                if (someVehicle.getId() == id) {
                    //if match return vehicle object
                    return someVehicle;
                }
            }
            return null;
        }


        @RequestMapping(value="/updateVehicle", method=RequestMethod.PUT)
        public Vehicle updateVehicle(@RequestBody Vehicle newVehicle) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            //iterate local fine line by line
            LineIterator it = FileUtils.lineIterator(new File("./inventory.txt"), StandardCharsets.UTF_8.name());
            while (it.hasNext()) {
                String thisLine = it.next();
                Vehicle someVehicle = mapper.readValue(thisLine, Vehicle.class);
                //check if current line's vehicle's id matches vehicle id that is passed in
                if (someVehicle.getId() == newVehicle.getId()) {
                    //if match, update current line w/ vehicle that was passed in
                    String jsonVehicle = mapper.writeValueAsString(newVehicle);
                    String fileString = FileUtils.readFileToString(new File("./inventory.txt"), StandardCharsets.UTF_8.name());
                    String finalString = "";
                    if (it.hasNext()) {
                        finalString = fileString.replace(thisLine + "\r\n", jsonVehicle + "\r\n");
                    } else {
                        finalString = fileString.replace(thisLine, jsonVehicle);
                    }
                    FileUtils.writeStringToFile(new File("./inventory.txt"), finalString, StandardCharsets.UTF_8.name());

                    return newVehicle;
                }
            }
            return null;
        }


        @RequestMapping(value="/deleteVehicle/{id}", method=RequestMethod.DELETE)
        public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            LineIterator it = FileUtils.lineIterator(new File("./inventory.txt"), StandardCharsets.UTF_8.name());
            while (it.hasNext()) {
                String thisLine = it.next();
                Vehicle someVehicle = mapper.readValue(thisLine, Vehicle.class);
                if (someVehicle.getId() == id) {
                    String fileString = FileUtils.readFileToString(new File("./inventory.txt"), StandardCharsets.UTF_8.name());
                    String finalString = "";
                    if (it.hasNext()) {
                        finalString = fileString.replace(thisLine + "\r\n", "");
                    } else {
                        finalString = fileString.replace(thisLine, "");
                    }
                    FileUtils.writeStringToFile(new File("./inventory.txt"), finalString, StandardCharsets.UTF_8.name());
                    return new ResponseEntity("Deleted", HttpStatus.OK);
                }
            }
            return new ResponseEntity("Not found", HttpStatus.BAD_REQUEST);

        }

        @RequestMapping(value="/getLatestVehicles", method=RequestMethod.GET)
        public List<Vehicle> getLatestVehicles() throws IOException {
            List<Vehicle> latestVehiclesList = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            LineIterator it = FileUtils.lineIterator(new File("./inventory.txt"), StandardCharsets.UTF_8.name());
            while (latestVehiclesList.size() <= 10 && it.hasNext()) {
                String check = it.next();
                System.out.println(check);
                Vehicle someVehicle = mapper.readValue(check, Vehicle.class);
                latestVehiclesList.add(someVehicle);
            }
            return latestVehiclesList;
        }
    }
