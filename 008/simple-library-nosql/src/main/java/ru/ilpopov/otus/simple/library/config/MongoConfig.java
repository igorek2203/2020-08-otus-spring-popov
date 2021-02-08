package ru.ilpopov.otus.simple.library.config;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.ilpopov.otus.simple.library.repository.BookRepository;

@EnableMongock
@EnableMongoRepositories(basePackageClasses = BookRepository.class)
@Configuration
public class MongoConfig {

}
