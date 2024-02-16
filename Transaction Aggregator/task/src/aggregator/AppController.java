package aggregator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
public class AppController {

    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/aggregate")
    public ResponseEntity<List<TransactionDTO>> getAggregate(
            @RequestParam("account") String account
    ) {
        List<TransactionDTO> transactions = new ArrayList<>();

        String url1 = "http://localhost:8888/transactions?account=" + account;
        String url2 = "http://localhost:8889/transactions?account=" + account;
        try {
            transactions.addAll(queryServer(url1));
            transactions.addAll(queryServer(url2));

            transactions.sort(Comparator.comparing(TransactionDTO::timestamp).reversed());

            return ResponseEntity.ok(transactions);

        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }

    }

    private List<TransactionDTO> queryServer(String url) {
        List<TransactionDTO> transactions = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            try {
                TransactionDTO[] response = restTemplate.getForObject(url, TransactionDTO[].class);
                if (response != null) {
                    transactions.addAll(Arrays.asList(response));
                }
                break;
            } catch (HttpServerErrorException e) {
                if (e.getStatusCode().is5xxServerError() && i == 5) {
                    throw e;
                }
            }
        }
        return transactions;
    }

}
