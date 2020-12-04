package com.cisco.demo.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserModel {

	private long id;
	
	@NotEmpty(message = "User name cannot be empty")
    private String name;
    
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    private String email;
    
    @DateTimeFormat(pattern = "MM/dd/YYYY")
    private Date creationDate;
    
    private List<BookmarkModel> bookmark;
}
