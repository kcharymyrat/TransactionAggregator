package aggregator;

import org.springframework.cache.annotation.CachePut;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionRequests {

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    @CachePut(value = "transactions", key = "#account")
    public CompletableFuture<List<TransactionDTO>> queryServer(String url, String account) {
        url += account;
        ResponseEntity<List<TransactionDTO>> response = null;

        for (int i = 1; i <= 5; i++) {
            try {
                response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {});
                if (response.getStatusCode().is2xxSuccessful())
                    break;
            } catch (Exception ignored) {
                response = new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
            }
        }
        return CompletableFuture.completedFuture(response.getBody());
    }
}
