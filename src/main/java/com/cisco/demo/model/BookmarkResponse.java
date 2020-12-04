package com.cisco.demo.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class BookmarkResponse{
	
	private Date timestamp = new Date();
    private int statusCode;
    private String message;
    private List<BookmarkModel> bookmark;
}