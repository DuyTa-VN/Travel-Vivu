package vn.duyta.Travel_Vivu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TravelVivuApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelVivuApplication.class, args);
	}

}
