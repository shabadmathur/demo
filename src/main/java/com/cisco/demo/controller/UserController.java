package com.cisco.demo.controller;

import java.sql.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.demo.model.UserModel;
import com.cisco.demo.model.UserResponse;
import com.cisco.demo.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	/**
     * This method is used for creating user
     * 
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/")
    @ResponseBody
    public UserResponse create(@Valid @RequestBody UserModel request){
    	return userServiceImpl.createUser(request);
    }
    /**
     * This method is used for returns a particular bookmark as per bookmark id passed as parameter
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/{id}")
    @ResponseBody
    public UserResponse getUser(@PathVariable (name = "id") long id){
       return userServiceImpl.getUser(id);
    }
    /**
     * This method is used for returns filtered bookmarks having a particular user id, page number and page size passed as parameter
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/email/{email}")
    @ResponseBody
    public UserResponse getUserByEmail (@PathVariable(name = "email") String email){
       return userServiceImpl.getUserByEmail(email);
    }
    /**
     * This method is used for returns filtered bookmarks having a particular date range passed as parameter
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/startDate/{startDate}/endDate/{endDate}/page/{page}/size/{size}")
    @ResponseBody
    public UserResponse getUsersByDateRange (@PathVariable(name = "startDate") String startDate, @PathVariable(name = "endDate") String endDate, @Valid @PathVariable(name = "page") int page, @PathVariable(name = "size") int size){
       return userServiceImpl.getUsersByDateRange(Date.valueOf(startDate), Date.valueOf(endDate), page, size);
    }
    /**
     * This method is used for returning all bookmarks as per the page number and size of the page
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/page/{page}/size/{size}")
    @ResponseBody
    public UserResponse getAllUsers(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size){
       return userServiceImpl.getAllUsers(page, size);
    }
    /**
     * This method is used for returning all bookmarks for a particular user as per the page number and size of the page
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/{id}/bookmark")
    @ResponseBody
    public UserResponse getBookmarks(@PathVariable(name = "id") long id){
       return userServiceImpl.getBookmarks(id);
    }
    /**
	 * This method updates user object in DB
	 * 
	 * @param request
	 * @return
	 */
    @PatchMapping(value = "/{id}")
    @ResponseBody
    public UserResponse updateUser(@PathVariable (name = "id") long id, @Valid @RequestBody UserModel request){
       return userServiceImpl.updateUser(id, request);
    }
    /**
	 * This method updates user object in DB
	 * 
	 * @param request
	 * @return
	 */
    @PatchMapping(value = "/email/{email}")
    @ResponseBody
    public UserResponse updateUserByEmail(@PathVariable (name = "email") String email, @Valid @RequestBody UserModel request){
       return userServiceImpl.updateUserByEmail(email, request);
    }
    /**
     * This method is used for deleting user
     * 
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public UserResponse deleteUser (@PathVariable(name = "id") long id){
       return userServiceImpl.deleteUser(id);
    }
}
