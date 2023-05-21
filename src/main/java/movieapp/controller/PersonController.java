package movieapp.controller;


import java.util.List;
import java.util.Optional;

import movieapp.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import movieapp.dto.PersonSimple;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/persons")
public class PersonController {
	
	@Autowired
	PersonService personService;
	
	@GetMapping("/{id}")
	@ResponseBody
	PersonSimple getById(@PathVariable("id") int id){
		return personService.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("Person not found with id <%d>",
								id)));
	}
	
	@GetMapping("/byName")
	@ResponseBody
	List<PersonSimple> getByName(@RequestParam("n") String name){
		return personService.getByName(name);
	}
	
	@PostMapping
	@ResponseBody
	PersonSimple add(@RequestBody @Valid PersonSimple personSimple) {
		return personService.add(personSimple);
	}
	

}
