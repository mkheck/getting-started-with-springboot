package com.thehecklers.gettingstartedwithspringboot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@SpringBootApplication
public class GettingStartedWithSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(GettingStartedWithSpringbootApplication.class, args);
	}

}

@RestController
@RequestMapping("/coffees")
class CoffeeController {
	private List<Coffee> coffees = new ArrayList<>();

	public CoffeeController() {
		coffees.addAll(
			List.of(new Coffee("Cafe Ganador"),
					new Coffee("Cafe Cereza"),
					new Coffee("Cafe Lareno"),
					new Coffee("Cafe Tres Pontas"))
			);
	}

	@GetMapping
	Iterable<Coffee> getAllCoffees() {
		return coffees;
	}

	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(String id) {
		for (final Coffee coffee : coffees) {
			if (coffee.getId().equals(id)) {
				return Optional.of(coffee);
			}
		}

		return Optional.empty();
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		coffees.add(coffee);
		return coffee;
	}

	@PutMapping("/{id}")
	ResponseEntity<?> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		for (final Coffee c : coffees) {
			if (c.getId().equals(id)) {
				coffees.set(coffees.indexOf(c), coffee);
				return new ResponseEntity<>(coffee, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffeeById(@PathVariable String id) {
		coffees.removeIf(c -> c.getId().equals(id));
	}
}

@Data
@AllArgsConstructor
class Coffee {
	private String id;
	private String name;

	public Coffee(String name) {
		this(UUID.randomUUID().toString(), name);
	}
}