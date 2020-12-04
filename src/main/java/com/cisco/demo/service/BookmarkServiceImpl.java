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
import com.cisco.demo.model.BookmarkModel;
import com.cisco.demo.model.BookmarkResponse;
import com.cisco.demo.repository.BookmarkRepository;

@Service
public class BookmarkServiceImpl implements BookmarkService{

    @Autowired
    private BookmarkRepository bookmarkRepo;
	
    /**
     * This method is used for creating / saving bookmark object
     * 
     * @param request
     * @return
     */
    public BookmarkResponse create(BookmarkModel request){
    	try {
	    	Assert.notNull(request, AppConstant.INVALID_BOOKMARK_REQ);
	    	Assert.notEmpty(request.getUser(), AppConstant.INVALID_USR_LST);
	    	
	    	Date creationDate 	= request.getCreationDate();
	    	
	    	if(creationDate == null) {
	    		request.setCreationDate(new Date());
	    	}
	    	
	    	Bookmark bookmark	= new Bookmark();
	    	BeanUtils.copyProperties(request, bookmark);
	    	
	    	Bookmark savedBookmark 	= bookmarkRepo.save(bookmark);
	    	if (bookmarkRepo.findById(savedBookmark.getId()).isPresent()) {
	    		BookmarkModel bookmarkModel	= new BookmarkModel();
	    		BeanUtils.copyProperties(savedBookmark, bookmarkModel);
	    		
	    		return getResponseObject(HttpStatus.CREATED, AppConstant.SUCCESS_BOOKMARK_CREATE, Arrays.asList(bookmarkModel));
	    	}
	    	
	    	return getResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, AppConstant.FAIL_BOOKMARK_CREATE , null);
	    	
    	} catch(IllegalArgumentException ie) {
    		return getResponseObject(HttpStatus.BAD_REQUEST, ie.getMessage(), null);
    	}
     }
    /**
     * This method is used for returning a single bookmark object based on bookmark id passed as parameter
     * 
     * @param id
     * @return
     */
	public BookmarkResponse getBookmark(long id) {
		Optional<Bookmark> value 		= bookmarkRepo.findById(id);
		if (value.isPresent()) {
			BookmarkModel bookmarkModel	= new BookmarkModel();
    		BeanUtils.copyProperties(value.get(), bookmarkModel);
			return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_FOUND, Arrays.asList(bookmarkModel));
		}
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_BOOKMARK_ID, null);
	}
	/**
	 * This method returns specific bookmark based on bookmark name passed as parameter along with page number and page size passed as parameter
	 * 
	 * @param bookmarkName
	 * @param page
	 * @param size
	 * @return
	 */
	@Cacheable(value = "bookmark", key = "#bookmarkName#page#size")
	public BookmarkResponse getBookmarksByName(String bookmarkName, int page, int size){
		Pageable paging 				= PageRequest.of(page, size);
		Optional<List<Bookmark>> value 	= bookmarkRepo.findByName(bookmarkName, paging);
		if(value.isPresent()) {
			List<Bookmark> bookmarks	= value.get();
			try {
				Assert.notEmpty(bookmarks, AppConstant.BOOKMARK_NOT_FOUND);
				
				List<BookmarkModel> bookmarkModels	= new ArrayList<>();
		    	// iterating over the user Entity object list and copying bean attributes to user model object 
		    	// adding to user model list
		    	for (Bookmark bookmark: bookmarks ) {
		    		BookmarkModel bookmarkModel	= new BookmarkModel();
		            BeanUtils.copyProperties(bookmark , bookmarkModel);
		            bookmarkModels.add(bookmarkModel);
		         }
				
				return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_FOUND, bookmarkModels);
			} catch (IllegalArgumentException ie) {
				return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_BOOKMARK_NAME, null);
			}
		}
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_BOOKMARK_ID, null);
	}
	/**
	 * This method returns date range specific bookmark list as per the page number and page size passed as parameter
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param size
	 * @return
	 */
	@Cacheable(value = "bookmark", key = "#startDate#endDate#size")
	public BookmarkResponse getBookmarksByDateRange(Date startDate, Date endDate, int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Optional<List<Bookmark>> value = bookmarkRepo.findAllByCreationDateBetween(startDate, endDate, pageable);
		if (value.isPresent()) {
			
			List<Bookmark> bookmarks	= value.get();
			
			List<BookmarkModel> bookmarkModels	= new ArrayList<>();
	    	// iterating over the user Entity object list and copying bean attributes to user model object 
	    	// adding to user model list
	    	for (Bookmark bookmark: bookmarks ) {
	    		BookmarkModel bookmarkModel	= new BookmarkModel();
	            BeanUtils.copyProperties(bookmark , bookmarkModel);
	            bookmarkModels.add(bookmarkModel);
	         }
			
			return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_FOUND, bookmarkModels);
		}
		
		return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_NOT_FOUND, null);
	}
    /**
     * This method is used for returning all bookmarks as per the page and the size of the page passed as parameter
     * 
     * @param page
     * @param size
     * @return
     */
	@Cacheable(value = "bookmark", key = "#all#page#size")
	public BookmarkResponse getAllBookmarks(int page, int size){
		try {
			Pageable paging = PageRequest.of(page, size);
			Page<Bookmark> bookmarkPage	= bookmarkRepo.findAll(paging);
			List<Bookmark> bookmarks	= bookmarkPage.getContent();
	    	Assert.notEmpty(bookmarks, AppConstant.BOOKMARK_NOT_FOUND);
	    	
	    	List<BookmarkModel> bookmarkModels	= new ArrayList<>();
	    	// iterating over the user Entity object list and copying bean attributes to user model object 
	    	// adding to user model list
	    	for (Bookmark bookmark: bookmarks ) {
	    		BookmarkModel bookmarkModel	= new BookmarkModel();
	            BeanUtils.copyProperties(bookmark , bookmarkModel);
	            bookmarkModels.add(bookmarkModel);
	         }
	    	
	    	return getResponseObject(HttpStatus.OK, AppConstant.BOOKMARK_FOUND, bookmarkModels);
	    	
		} catch (IllegalArgumentException ie) {
			return getResponseObject(HttpStatus.BAD_REQUEST, ie.getMessage(), null);
		}
	}
	/**
	 * This method returns user specific bookmark list based on user id passed as parameter
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	public BookmarkResponse updateBookmark(long id, BookmarkModel request) {
		Optional<Bookmark> value = bookmarkRepo.findById(id);
		
		if (value.isPresent()) {
			Bookmark bookmark	= value.get();
			BeanUtils.copyProperties(request, bookmark);
			Bookmark bookmarkSaved	= bookmarkRepo.saveAndFlush(bookmark);
			if(bookmarkRepo.findById(bookmarkSaved.getId()).isPresent()) {
				BookmarkModel bookmarkModel	= new BookmarkModel();
	            BeanUtils.copyProperties(bookmark , bookmarkModel);
				return getResponseObject(HttpStatus.OK, AppConstant.SUCCESS_BOOKMARK_UPDATE, Arrays.asList(bookmarkModel));
			}
			return getResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, AppConstant.FAIL_BOOKMARK_UPDATE , null);
		}
		
		return getResponseObject(HttpStatus.BAD_REQUEST, AppConstant.INVALID_BOOKMARK_ID, null);
	}
	/**
	 * This method deletes a particular bookmark with id passed as parameter
	 * 
	 * @param id
	 * @return
	 */
	public BookmarkResponse deleteBookmark(long id) {
		bookmarkRepo.deleteById(id);
		Optional<Bookmark> value = bookmarkRepo.findById(id);
		if (value.isPresent()) {
			return getResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, AppConstant.FAIL_BOOKMARK_DELETE, null);
		}
		
		return getResponseObject(HttpStatus.OK, AppConstant.SUCCESS_BOOKMARK_DELETE, null);
	}
	/**
	 * Utility method to create bookmark response object
	 * 
	 * @param isSuccess
	 * @param statusCode
	 * @param message
	 * @param bookmarks
	 * @return
	 */
	private BookmarkResponse getResponseObject(HttpStatus statusCode, String message, List<BookmarkModel> bookmarks) {
		BookmarkResponse bookmarkResponse	= new BookmarkResponse();
		bookmarkResponse.setStatusCode(statusCode.value());
		bookmarkResponse.setMessage(message);
		if(bookmarks != null) {
			bookmarkResponse.setBookmark(bookmarks);
		}
		return bookmarkResponse;
	}
}
