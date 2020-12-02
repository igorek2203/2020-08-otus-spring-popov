package ru.ilpopov.otus.simple.library.config;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.ilpopov.otus.simple.library.dao.BookDao;

@EnableMongock
@EnableMongoRepositories(basePackageClasses = BookDao.class)
@Configuration
public class MongoConfig {

}
