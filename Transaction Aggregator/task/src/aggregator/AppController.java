package aggregator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class AppController {

    private final TransactionService service;
    public AppController(TransactionService service) {
        this.service = service;
    }


    @GetMapping("/aggregate")
    public List<TransactionDTO> getAggregate(
            @RequestParam("account") String account
    ) throws ExecutionException, InterruptedException {
        return service.getTransactions(account);
    }

}
