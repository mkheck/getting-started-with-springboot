package com.thehecklers.gettingstartedwithspringboot;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.CrudRepository;
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
			List.of(
				new Coffee("Cafe Ganador"), 
				new Coffee("Cafe Cereza"), 
				new Coffee("Cafe Lareno"),
				new Coffee("Cafe Tres Pontas"))
		);
	}
}

@AllArgsConstructor
@RestController
@RequestMapping("/coffees")
class CoffeeController {
	private final CoffeeRepository repo;

	@GetMapping
	Iterable<Coffee> getAllCoffees() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(String id) {
		return repo.findById(id);
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		return repo.save(coffee);
	}

	@PutMapping("/{id}")
	ResponseEntity<?> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		return (repo.existsById(id))
			? new ResponseEntity<>(repo.save(coffee), HttpStatus.OK)
			: new ResponseEntity<>(repo.save(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffeeById(@PathVariable String id) {
		repo.deleteById(id);
	}
}

interface CoffeeRepository extends CrudRepository<Coffee, String> {
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