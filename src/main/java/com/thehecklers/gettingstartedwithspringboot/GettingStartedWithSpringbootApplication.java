package com.thehecklers.gettingstartedwithspringboot;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GettingStartedWithSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GettingStartedWithSpringbootApplication.class, args);
	}

}

@AllArgsConstructor
@Component
class DataLoader {
	private final CoffeeRepository repo;

	@PostConstruct
	public void loadData() {
		repo.saveAll(
			Flux.just(
				new Coffee("Cafe Ganador"), 
				new Coffee("Cafe Cereza"), 
				new Coffee("Cafe Lareno"),
				new Coffee("Cafe Tres Pontas"))
		)
		.subscribe();
	}
}

@AllArgsConstructor
@RestController
@RequestMapping("/coffees")
class CoffeeController {
	private final CoffeeRepository repo;

	@GetMapping
	Flux<Coffee> getAllCoffees() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	Mono<Coffee> getCoffeeById(String id) {
		return repo.findById(id);
	}

	@PostMapping
	Mono<Coffee> postCoffee(@RequestBody Coffee coffee) {
		return repo.save(coffee);
	}

	@PutMapping("/{id}")
	Mono<ResponseEntity<?>> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		return repo.existsById(id)
			.map(exists -> exists
				? ResponseEntity.status(HttpStatus.OK).body(repo.save(coffee))
				: ResponseEntity.status(HttpStatus.CREATED).body(repo.save(coffee))
			);
	}

	@DeleteMapping("/{id}")
	Mono<Void> deleteCoffeeById(@PathVariable String id) {
		return repo.deleteById(id);
	}
}

interface CoffeeRepository extends ReactiveCrudRepository<Coffee, String> {
}

@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
// @AllArgsConstructor
class Coffee {
	@Id
	private String id;
	@NonNull
	private String name;

	// public Coffee(String name) {
	// this(UUID.randomUUID().toString(), name);
	// }
}