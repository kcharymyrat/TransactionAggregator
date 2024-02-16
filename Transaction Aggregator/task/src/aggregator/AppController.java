package aggregator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
public class AppController {

    @GetMapping("/aggregate")
    public ResponseEntity<List<TransactionDTO>> getAggregate(
            @RequestParam("account") String account
    ) {
        RestTemplate restTemplate = new RestTemplate();

        // Query data from server 1
        String url1 = "http://localhost:8888/transactions?account=" + account;
        TransactionDTO[] res1 = restTemplate.getForObject(url1, TransactionDTO[].class);

        // Query data from server 2
        String url2 = "http://localhost:8889/transactions?account=" + account;
        TransactionDTO[] res2 = restTemplate.getForObject(url2, TransactionDTO[].class);

        // Combine data from both servers into a single list
        List<TransactionDTO> transactions = new ArrayList<>();
        if (res1 != null) {
            transactions.addAll(Arrays.asList(res1));
        }
        if (res2 != null) {
            transactions.addAll(Arrays.asList(res2));
        }

        // Sort transactions by timestamp in descending order
        transactions.sort(Comparator.comparing(TransactionDTO::timestamp).reversed());

        return ResponseEntity.ok(transactions);
    }

}
