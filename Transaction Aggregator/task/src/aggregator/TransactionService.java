package aggregator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@EnableAsync
public class TransactionService {
    private final String URL1 = "http://localhost:8888/transactions?account=";
    private final String URL2 = "http://localhost:8889/transactions?account=";

    private final TransactionRequests requests;
    public TransactionService(@Autowired TransactionRequests requests) {
        this.requests = requests;
    }

    @Cacheable(cacheNames = "transactions", key = "#account")
    public List<TransactionDTO> getTransactions(String account) throws ExecutionException, InterruptedException {
        List<TransactionDTO> transactions = new ArrayList<>();

        CompletableFuture<List<TransactionDTO>> future1 = requests.queryServer(URL1, account);
        CompletableFuture<List<TransactionDTO>> future2 = requests.queryServer(URL2, account);
        CompletableFuture.allOf(future1, future2).join();

        transactions.addAll(Objects.requireNonNullElse(future1.get(), List.of()));
        transactions.addAll(Objects.requireNonNullElse(future2.get(), List.of()));

        transactions.sort(Comparator.comparing(TransactionDTO::timestamp).reversed());

        System.out.printf("\n\n\nTransations = %s\n\n\n", transactions);

        return transactions;
    }


}
