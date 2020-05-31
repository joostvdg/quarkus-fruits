package com.github.joostvdg.demo.jx.quarkusfruits;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FruitRepository extends CrudRepository<Fruit, Long> {

    List<Fruit> findByColor(String color);
}
