package com.inghubs.brokage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inghubs.brokage.entity.BrokageAsset;
import com.inghubs.brokage.entity.BrokageCustomer;
import com.inghubs.brokage.repository.AssetRepository;
import com.inghubs.brokage.repository.CustomerRepository;

@Configuration
public class LoadDatabase {
  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(AssetRepository repository, CustomerRepository customerRepository) {

    return args -> {
      log.info("Preloading " + repository.save(new BrokageAsset(1L, "TRY", 100, 100)));
      log.info("Preloading " + repository.save(new BrokageAsset(2L, "TRY", 100, 100)));
      log.info("Preloading " + repository.save(new BrokageAsset(3L, "TRY", 100, 100)));
      log.info("Preloading " + repository.save(new BrokageAsset(4L, "TRY", 100, 100)));
      log.info("Preloading " + repository.save(new BrokageAsset(5L, "TRY", 100, 100)));
      log.info("Preloading " + customerRepository.save(new BrokageCustomer("admin", "$2a$12$4leyAR.ClA1VZo.k7800NObq8ZRd5L13/5CeotFoHCZ1UH1BqClsa")));
    };
  }
}
