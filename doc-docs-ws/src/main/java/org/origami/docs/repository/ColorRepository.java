package org.origami.docs.repository;

import org.origami.docs.entity.Color;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ColorRepository extends MongoRepository<Color, String> {
}
