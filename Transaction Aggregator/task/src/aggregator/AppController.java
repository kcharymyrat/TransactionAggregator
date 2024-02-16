package aggregator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AppController {

    @GetMapping("/aggregate")
    public ResponseEntity<String> getAggregate() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8889/ping";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return ResponseEntity.ok(response.getBody());
    }

}
