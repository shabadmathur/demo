package com.cisco.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cisco.demo.constants.AppConstant;
import com.cisco.demo.entity.Bookmark;
import com.cisco.demo.entity.User;
import com.cisco.demo.model.BookmarkModel;
import com.cisco.demo.model.UserModel;
import com.cisco.demo.model.UserResponse;
import com.cisco.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepo;
    
    /**
     * This method is used for handling user creation
     * 
     * @param request
     * @return
     */
    public UserResponse createUser(UserModel request) {
    	try {
    		Assert.notNull(request, "User request Object cannot be empty or null");
	    	if (userRepo.findByEmail(request.getEmail()).isPresent()) {
	    		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.FAIL_USER_CREATE, null);
	        }
	    	
	    	Date creationDate 	= request.getCreationDate();
	    	if(creationDate == null) {
	    		request.setCreationDate(new Date());
	    	}
	    	
	    	User user	= new User();
	    	BeanUtils.copyProperties(request, user);
	    	User userSaved	= userRepo.saveAndFlush(user);
	    	if(userSaved.getEmail().equals(request.getEmail())) {
	    		UserModel usrModel	= new UserModel();
	    		BeanUtils.copyProperties(userSaved, usrModel);
	    		return getResponseObject(HttpStatus.CREATED, AppConstant.SUCCESS_USER_CREATE, Arrays.asList(usrModel));
	    	}
	    	return getResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, AppConstant.SYS_FAIL_USER_CREATE, null);
    	} catch (IllegalArgumentException ie) {
    		return getResponseObject(HttpStatus.BAD_REQUEST, ie.getMessage(), null);
    	}
    }
    /**
     * This method returns user specific user based on id passed as parameter
     * 
     * @param id
     * @return
     */
	public UserResponse getUser(long id) {
		Optional<User> value = userRepo.findById(id);
		
		if (value.isPresent()) {
			UserModel usrModel	= new UserModel();
    		BeanUtils.copyProperties(value.get(), usrModel);
			return getResponseObject(HttpStatus.OK, AppConstant.USR_FOUND, Arrays.asList(usrModel));
		}
		
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_USR_ID, null);
	}
	/**
	 * This method returns user specific user based on email passed as parameter
	 * 
	 * @param email
	 * @return
	 */
	public UserResponse getUserByEmail(String email) {
		try {
			Assert.hasText(email, "Email cannot be empty");
			Optional<User> value = userRepo.findByEmail(email);
			
			if (value.isPresent()) {
				UserModel usrModel	= new UserModel();
	    		BeanUtils.copyProperties(value.get(), usrModel);
				return getResponseObject(HttpStatus.OK, AppConstant.USR_FOUND, Arrays.asList(usrModel));
			}
		} catch (IllegalArgumentException ie) {
			return getResponseObject(HttpStatus.BAD_REQUEST, ie.getMessage(), null);
		}
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_USR_EMAIL, null);
	}
	/**
	 * This method returns date range specific bookmark list along with page number and page size
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param size
	 * @return
	 */
	@Cacheable(value = "user", key = "#startdate#endDate")
	public UserResponse getUsersByDateRange(Date startDate, Date endDate, int page, int size) {
		System.out.println("Retriving from DB start date: "+startDate+" end Date: "+endDate);
		Pageable pageable = PageRequest.of(page, size);
		
		Optional<List<User>> value = userRepo.findAllByCreationDateBetween(startDate, endDate, pageable);
		if (value.isPresent()) {
			
			List<User> users	= value.get();
			
			List<UserModel> userModels	= new ArrayList<>();
	    	// iterating over the user Entity object list and copying bean attributes to user model object 
	    	// adding to user model list
	    	for (User user: users ) {
	    		UserModel userModel	= new UserModel();
	            BeanUtils.copyProperties(user , userModel);
	            userModels.add(userModel);
	         }
			
			return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_FOUND, userModels);
		}
		
		return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_NOT_FOUND, null);
	}
	/**
	 * This method is used for returning all users
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@Cacheable(value = "user", key = "#allUser#page#size")
	public UserResponse getAllUsers(int page, int size){
		try {
			Pageable paging 	= PageRequest.of(page, size);
			Page<User> userPage	= userRepo.findAll(paging);
			List<User> users	= userPage.getContent();
	    	Assert.notEmpty(users, AppConstant.USER_NOT_FOUND);
	    	
	    	List<UserModel> usrModels	= new ArrayList<>();
	    	// iterating over the user Entity object list and copying bean attributes to user model object 
	    	// adding to user model list
	    	for (User user: users ) {
	    		UserModel usrModel	= new UserModel();
	            BeanUtils.copyProperties(user , usrModel);
	            usrModels.add(usrModel);
	         }
	    	
	    	return getResponseObject(HttpStatus.OK, AppConstant.USER_FOUND, usrModels);
	    	
		} catch (IllegalArgumentException ie) {
			return getResponseObject(HttpStatus.BAD_REQUEST, ie.getMessage(), null);
		}
	}
	/**
	 * This method returns user specific bookmark list based on user id passed as parameter
	 * 
	 * @param userId
	 * @return
	 */
	@Cacheable(value = "user", key = "bookmarks#userId")
	public UserResponse getBookmarks(long userId) {
		Optional<User> value = userRepo.findById(userId);
		
		if (value.isPresent()) {
			User user	= value.get();
			try {
				List<Bookmark> bookmarks	= user.getBookmarks();
				Assert.notEmpty(bookmarks, AppConstant.BOOKMARK_NOT_FOUND);
				
				UserModel userModel	= new UserModel();
				BeanUtils.copyProperties(user, userModel);
				
				List<BookmarkModel> bookmarkModels	= new ArrayList<>();
		    	// iterating over the user Entity object list and copying bean attributes to user model object 
		    	// adding to user model list
		    	for (Bookmark bookmark: bookmarks ) {
		    		BookmarkModel bookmarkModel	= new BookmarkModel();
		            BeanUtils.copyProperties(bookmark , bookmarkModel);
		            bookmarkModels.add(bookmarkModel);
		         }
				
		    	userModel.setBookmark(bookmarkModels);
		    	
				return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_FOUND, Arrays.asList(userModel));
			} catch (IllegalArgumentException ie) {
				return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.BOOKMARK_NOT_FOUND, null);
			}
		}
		
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_USR_ID, null);
	}
	/**
	 * This method updates specific user based on id passed as parameter
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	public UserResponse updateUser(long id, UserModel request) {
		Optional<User> value = userRepo.findById(id);
		
		if (value.isPresent()) {
			User user		= value.get();
			BeanUtils.copyProperties(request, user);
			User usrSaved	= userRepo.saveAndFlush(user);
			if(userRepo.findById(usrSaved.getId()).isPresent()) {
				UserModel usrModel	= new UserModel();
	    		BeanUtils.copyProperties(value.get(), usrModel);
				return getResponseObject(HttpStatus.OK, AppConstant.SUCCESS_USER_UPDATE, Arrays.asList(usrModel));
			}
			return getResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, AppConstant.FAIL_USER_UPDATE, null);
		}
		
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_USR_ID, null);
	}
	/**
	 * This method updates specific user based on email id passed as parameter
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	public UserResponse updateUserByEmail(String email, UserModel request) {
		Optional<User> value = userRepo.findByEmail(email);
		
		if (value.isPresent()) {
			User user		= value.get();
			BeanUtils.copyProperties(request, user);
			User usrSaved	= userRepo.saveAndFlush(user);
			if(userRepo.findByEmail(usrSaved.getEmail()).isPresent()) {
				UserModel usrModel	= new UserModel();
	    		BeanUtils.copyProperties(value.get(), usrModel);
				return getResponseObject(HttpStatus.OK, AppConstant.SUCCESS_USER_UPDATE, Arrays.asList(usrModel));
			}
			return getResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, AppConstant.FAIL_USER_UPDATE, null);
		}
		
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_USR_ID, null);
	}
	/**
	 * This method deletes a particular user with id passed as parameter
	 * 
	 * @param userId
	 * @return
	 */
	public UserResponse deleteUser(long id) {
		userRepo.deleteById(id);
		Optional<User> value = userRepo.findById(id);
		if (value.isPresent()) {
			return getResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, AppConstant.FAIL_USER_DELETE, null);
		}
		
		return getResponseObject(HttpStatus.OK, AppConstant.SUCCESS_USER_DELETE, null);
	}
	/**
	 * Utility method to create user response object
	 * 
	 * @param isSuccess
	 * @param statusCode
	 * @param message
	 * @param users
	 * @return
	 */
	private UserResponse getResponseObject(HttpStatus statusCode, String message, List<UserModel> users) {
		UserResponse userResponse	= new UserResponse();
		userResponse.setStatusCode(statusCode.value());
		userResponse.setMessage(message);
		if(users != null) {
			userResponse.setUser(users);
		}
		return userResponse;
	}
}
