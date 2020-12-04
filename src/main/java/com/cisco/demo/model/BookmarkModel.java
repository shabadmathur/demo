package com.cisco.demo.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BookmarkModel {

    private long id;
	
	@NotEmpty(message = "Bookmark name cannot be empty")
    private String name;
    
    @NotEmpty(message = "Url cannot be empty")
    private String url;
    
    @NotEmpty(message = "Bookmark type cannot be empty")
    private String type;
    
    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;
    
    @DateTimeFormat(pattern = "mm/dd/YYY")
    private Date creationDate;

    @NotEmpty(message = "Atleast one user is required")
	private List<UserModel> user;
}