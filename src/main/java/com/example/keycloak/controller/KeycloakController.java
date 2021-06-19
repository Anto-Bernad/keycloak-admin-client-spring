package com.example.keycloak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.keycloak.service.KeycloakInterface;

@RestController
@RequestMapping("/keycloak")
public class KeycloakController {

	@Autowired
	KeycloakInterface keycloakInterface;
	
	/**
	 * @author antobernad
	 * @throws Exception
	 */
	
	@PostMapping("/user")
	public void saveWorkflowTaskDetails()
			throws Exception {
		keycloakInterface.createUser();
		return;
	}
}
