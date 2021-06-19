package com.example.keycloak.service.impl;

import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.example.keycloak.service.KeycloakInterface;

@Service
public class KeycloakService implements KeycloakInterface {

	String serverUrl = "http://localhost:8091/auth";
	// Realm name
	String realm = "";
	//ClientId - case sensitive 
	String clientId = "";
	//Client Secret
	String clientSecret = "";

	/**
	 * @author antobernad
	 * Creates a User, sets temporary password and assigns the provided role
	 */
	@Override
	public void createUser() {
		/*
		 * Auth Keycloak, User needs to have user creation, password change, role
		 * assignment access
		 */
		Keycloak keycloak = KeycloakBuilder.builder().serverUrl(serverUrl).realm(realm)
				.grantType(OAuth2Constants.PASSWORD).clientId(clientId).clientSecret(clientSecret).username("test")
				.password("secretAdmin").build();
		UserRepresentation user = new UserRepresentation();
		// Set User is enabled
		user.setEnabled(true);
		// Set User Name
		user.setUsername("bernad");
		/*
		 * // Set First Name user.setFirstName("First"); // Set Last Name
		 * user.setLastName("Last"); // Set Email ID
		 * user.setEmail("tom+tester1@tdlabs.local");
		 */

		// Selecting the realm
		RealmResource realmResource = keycloak.realm(realm);
		UsersResource usersRessource = realmResource.users();
		// Create User (requires manage-users role)
		Response response = usersRessource.create(user);
		// Get User ID for Role and Password setup
		String userId = CreatedResponseUtil.getCreatedId(response);

		// Define password credential
		CredentialRepresentation passwordCred = new CredentialRepresentation();
		/*
		 * Set Temp. password is true, On Login user will be prompt to provide new
		 * password
		 */
		passwordCred.setTemporary(true);
		passwordCred.setType(CredentialRepresentation.PASSWORD);
		passwordCred.setValue("secretAdmin");

		// Get specific user by Id
		UserResource userResource = usersRessource.get(userId);

		// Set password credential
		userResource.resetPassword(passwordCred);

		// Get realm role "tester" (requires view-realm role)
		RoleRepresentation realmRole = realmResource.roles()//
				.get("tester").toRepresentation();

		// Assign realm role tester to user
		userResource.roles().realmLevel().add(Arrays.asList(realmRole));
	}

}
