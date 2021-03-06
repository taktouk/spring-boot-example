package com.example.demo.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.person.Person;
import com.example.demo.person.PersonRepository;

@RunWith(SpringRunner.class)
@DataJdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value=Repository.class))
public class PersonRepositoryImplTest {

	@Autowired
	PersonRepository personRepo;
	
	Person richard;
	
	@Before
	public void before() {
		richard = new Person();
		richard.setFirstname("Richard");
		richard.setLastname("Barabé");
	}
	
	@Test
	public void createAndLoad() {
		assertNull(richard.getId());
		personRepo.create(richard);
		assertNotNull(richard.getId());
		
		Person createdPerson = personRepo.findById(richard.getId()).orElseThrow();
		assertEquals("Richard", createdPerson.getFirstname());
		assertEquals("Barabé", createdPerson.getLastname());
	}
	
	@Test
	public void findById_missing() {
		Optional<Person> optionalPerson = personRepo.findById(1);
		assertTrue(optionalPerson.isEmpty());
	}
	
	@Test
	public void findById_existing() {
		personRepo.create(richard);
		Optional<Person> result = personRepo.findById(richard.getId());
		assertTrue(result.isPresent());
	}

}
