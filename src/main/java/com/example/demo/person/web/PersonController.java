package com.example.demo.person.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.person.Person;

/**
 * Controls pages related to persons, including showing a list, editing, adding and deleting persons.
 * @author Richard Barabé
 *
 */
@Controller
public class PersonController {
	
	private static List<Person> persons;
	static {
		Person p1 = new Person();
		p1.setId(1);
		p1.setFirstname("Chuck");
		p1.setLastname("Norris");
		
		Person p2 = new Person();
		p2.setId(2);
		p2.setFirstname("John");
		p2.setLastname("Wick");
		
		persons = List.of(p1, p2);
	}
	
	/**
	 * Load all the persons and show the persons view.
	 * @param model The model passed to the page.  This model will contain the list of "persons".
	 * @return
	 */
	@GetMapping({"/", "/persons"})
	public String showPersonsPage(Model model) {
		model.addAttribute("persons", persons);
		return "listPersons";
		
	}
	
	/**
	 * Load a person and show and edit page for that person.
	 * @param id Path variable identifying the person to edit.
	 * @param model The model passed to the page.  This model will contain the "person"
	 * @return the name of the view used to edit that person.
	 */
	@GetMapping("/person/edit/{id}")
	public String showEditPersonPage(@PathVariable("id") long id, Model model) {
		Person personToEdit = persons.stream().filter(p -> id == p.getId()).findFirst().orElse(null);
		model.addAttribute("person", personToEdit);
		return "editPerson";
	}
	
	/**
	 * Updates the received person and return to the persons page.
	 * @param id Id of the person to update.
	 * @param personToUpdate The modified person to update.
	 * @return
	 */
	@PostMapping("/person/update/{id}")
	public String updatePerson(@PathVariable("id") long id, Person personToUpdate, RedirectAttributes redirAttrs) {
		// We don't get the id from the form so we populate id here.
		personToUpdate.setId(id);
		
		persons = persons.stream().map(p -> p.getId() == id ? personToUpdate:p).collect(Collectors.toList());
		
		String message = String.format("Bravo ! : %s %s as been successfully MODIFIED !", 
				personToUpdate.getFirstname(), 
				personToUpdate.getLastname());
		redirAttrs.addFlashAttribute("successMessage", message);
		return "redirect:/persons";
	}
	
	/**
	 * Show the form for creating a new person.
	 * @param model An empty person will be present in the model.
	 * @return the page containing the form used to create a new person.
	 */
	@GetMapping("/person/addNew")
	public String showAddNewPaersonPage(Model model) {
		model.addAttribute("person", new Person());
		return "addNewPerson";
	}
	
	/**
	 * Create the specified person, generating it's id, and return to the "persons" page.
	 * @param personToCreate The new person.  Should not contain an id.
	 * @param redirAttrs Used to add a success message
	 */
	@PostMapping("/person/create")
	public String createPerson(Person personToCreate, RedirectAttributes redirAttrs) {
		// Random number between 100 and 100 000 000
	    long generatedId = 100 + (long) (Math.random() * (100_000_000 - 100));
	    personToCreate.setId(generatedId);
	    
	    persons = new ArrayList<>(persons);
	    persons.add(personToCreate);
	    
	    String message = String.format("Bravo ! : %s %s as been successfully CREATED !", 
				personToCreate.getFirstname(), 
				personToCreate.getLastname());
		redirAttrs.addFlashAttribute("successMessage", message);
	    
	    return "redirect:/persons";
	}
	
	/**
	 * Delete the person identified by the specified id, and redirect to the "persons" page.
	 * @param id Unique id of the person to delete.
	 * @param redirAttrs Used to add a success message in the returned page.
	 */
	@GetMapping("/person/delete/{id}")
	public String deletePerson(@PathVariable("id") long id, RedirectAttributes redirAttrs) {
		Person personToDelete = persons.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
		persons = persons.stream().filter(p -> p.getId() != id).collect(Collectors.toList());
		
		if(personToDelete != null) {
			String message = String.format("Bravo ! : %s %s as been successfully DELETED !", 
					personToDelete.getFirstname(), 
					personToDelete.getLastname());
			redirAttrs.addFlashAttribute("successMessage", message);
		}
		
		return "redirect:/persons";
		
	}
	
}