package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ProjectInit implements CommandLineRunner{
	@Autowired
	private UserRepository userRepository;


	@Override
	public void run(String... args) throws Exception {



	}
}
