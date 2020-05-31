package com.github.joostvdg.demo.jx.quarkusfruits;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/fruits")
public class FruitResource {

    private final FruitRepository fruitRepository;

    public FruitResource(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    @GetMapping("/")
    @Produces("application/json")
    public Iterable<Fruit> findAll() {
        var it = fruitRepository.findAll();
        List<Fruit> fruits = new ArrayList<Fruit>();
        it.forEach(fruits::add);
        return fruits;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable(value = "id") long id) {
        fruitRepository.deleteById(id);
    }

    @PostMapping("/name/{name}/color/{color}")
    public Fruit create(@PathVariable(value = "name") String name, @PathVariable(value = "color") String color) {
        return fruitRepository.save(new Fruit(name, color));
    }

    @PutMapping("/id/{id}/color/{color}")
    public Fruit changeColor(@PathVariable(value = "id") Long id, @PathVariable(value = "color") String color) {
        Optional<Fruit> optional = fruitRepository.findById(id);
        if (optional.isPresent()) {
            Fruit fruit = optional.get();
            fruit.setColor(color);
            return fruitRepository.save(fruit);
        }

        throw new IllegalArgumentException("No Fruit with id " + id + " exists");
    }

    @GetMapping("/color/{color}")
    public List<Fruit> findByColor(@PathVariable(value = "color") String color) {
        return fruitRepository.findByColor(color);
    }
}
