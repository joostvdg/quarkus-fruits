package com.github.joostvdg.demo.jx.quarkusfruits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

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
    @Counted(name = "fruit_get_all_count", description = "How many times all Fruits have been retrieved.")
    @Timed(name = "fruit_get_all_timer", description = "A measure of how long it takes to retrieve all Fruits.", unit = MetricUnits.MILLISECONDS)
    public Iterable<Fruit> findAll() {
        var it = fruitRepository.findAll();
        List<Fruit> fruits = new ArrayList<Fruit>();
        it.forEach(fruits::add);
        fruits.sort(Comparator.comparing(Fruit::getId));
        return fruits;
    }

    @DeleteMapping("{id}")
    @Counted(name = "fruit_delete_count", description = "How many times a Fruit has been deleted.")
    @Timed(name = "fruit_delete_timer", description = "A measure of how long it takes to delete a Fruit.", unit = MetricUnits.MILLISECONDS)
    public void delete(@PathVariable(value = "id") long id) {
        fruitRepository.deleteById(id);
    }

    @PostMapping("/name/{name}/color/{color}")
    @Counted(name = "fruit_new_count", description = "How many times a Fruit has been saved.")
    @Timed(name = "fruit_new_timer", description = "A measure of how long it takes to save a Fruit.", unit = MetricUnits.MILLISECONDS)
    public Fruit create(@PathVariable(value = "name") String name, @PathVariable(value = "color") String color) {
        return fruitRepository.save(new Fruit(name, color));
    }

    @PutMapping("/id/{id}/color/{color}")
    @Counted(name = "fruit_update_count", description = "How many times a Fruit has been updated.")
    @Timed(name = "fruit_dupdate_timer", description = "A measure of how long it takes to update a Fruit.", unit = MetricUnits.MILLISECONDS)
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
    @Counted(name = "fruit_getbycolor_count", description = "How many times Fruits have been retrieved by color.")
    @Timed(name = "fruit_getbycolor_timer", description = "A measure of how long it takes to retrieve Fruits by color.", unit = MetricUnits.MILLISECONDS)
    public List<Fruit> findByColor(@PathVariable(value = "color") String color) {
        return fruitRepository.findByColor(color);
    }
}
