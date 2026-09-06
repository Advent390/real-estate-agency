package com.metrazh.agency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входу застосунку.
 * Аналог `if __name__ == '__main__': app.run(...)` з оригінального app.py.
 */
@SpringBootApplication
public class RealEstateAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealEstateAgencyApplication.class, args);
    }
}
