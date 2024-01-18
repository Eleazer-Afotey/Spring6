package eleazer.springframework.spring6restmvc;

import jakarta.persistence.EntityListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class Spring6RestMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring6RestMvcApplication.class, args);
	}

}
