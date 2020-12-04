package com.cisco.demo.service;

import java.util.Date;

import com.cisco.demo.model.UserModel;
import com.cisco.demo.model.UserResponse;

public interface UserService {

	/**
     * This method is used for handling user creation
     * 
     * @param request
     * @return
     */
    public UserResponse createUser(UserModel request);
    /**
     * This method returns user specific user based on id passed as parameter
     * 
     * @param id
     * @return
     */
	public UserResponse getUser(long id);
	/**
	 * This method returns user specific user based on email passed as parameter
	 * 
	 * @param email
	 * @return
	 */
	public UserResponse getUserByEmail(String email);
	/**
	 * This method returns date range specific bookmark list along with page number and page size
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param size
	 * @return
	 */
	public UserResponse getUsersByDateRange(Date startDate, Date endDate, int page, int size);
	/**
	 * This method is used for returning all users
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	public UserResponse getAllUsers(int page, int size);
	/**
	 * This method returns user specific bookmark list based on user id passed as parameter
	 * 
	 * @param userId
	 * @return
	 */
	public UserResponse getBookmarks(long userId);
	/**
	 * This method updates specific user based on id passed as parameter
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	public UserResponse updateUser(long id, UserModel request);
	/**
	 * This method updates specific user based on email id passed as parameter
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	public UserResponse updateUserByEmail(String email, UserModel request);
	/**
	 * This method deletes a particular user with id passed as parameter
	 * 
	 * @param userId
	 * @return
	 */
	public UserResponse deleteUser(long id);
}
