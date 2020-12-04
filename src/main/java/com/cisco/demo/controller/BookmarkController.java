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

import com.cisco.demo.model.BookmarkModel;
import com.cisco.demo.model.BookmarkResponse;
import com.cisco.demo.service.BookmarkServiceImpl;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
	
	@Autowired
	private BookmarkServiceImpl bookmarkServiceImpl;
	/**
	 * This method handles bookmark creation
	 * 
	 * @param request
	 * @return
	 */
    @PostMapping(value = "/")
    @ResponseBody
    public BookmarkResponse createBookmark(@Valid @RequestBody BookmarkModel request){
    	return bookmarkServiceImpl.create(request);
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
    public BookmarkResponse getBookmark(@PathVariable (name = "id") long id){
       return bookmarkServiceImpl.getBookmark(id);
    }
    /**
     * This method is used for returns filtered bookmarks having a particular name, page number and page size passed as parameter
     * 
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/name/{name}/page/{page}/size/{size}")
    @ResponseBody
    public BookmarkResponse getBookmarksByName(@PathVariable(name = "name") String bookmarkName, @PathVariable(name = "page") int page, @PathVariable(name = "size") int size){
    	return bookmarkServiceImpl.getBookmarksByName(bookmarkName, page, size);
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
    public BookmarkResponse getBookmarksByDateRange (@PathVariable(name = "startDate") String startDate, @PathVariable(name = "endDate") String endDate, @Valid @PathVariable(name = "page") int page, @PathVariable(name = "size") int size){
       return bookmarkServiceImpl.getBookmarksByDateRange(Date.valueOf(startDate), Date.valueOf(endDate), page, size);
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
    public BookmarkResponse getAllBookmarks(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size){
       return bookmarkServiceImpl.getAllBookmarks(page, size);
    }
    /**
     * This method is used for returns filtered bookmarks having a particular user id, page number and page size passed as parameter
     * 
     * @param page
     * @param size
     * @return
     */
    @PatchMapping(value = "/{id}")
    @ResponseBody
    public BookmarkResponse updateBookmark (@PathVariable (name = "id") long id, @Valid @RequestBody BookmarkModel request){
       return bookmarkServiceImpl.updateBookmark(id, request);
    }
    /**
     * This method is used for deleting a particular bookmark based on id passed as parameter
     * 
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public BookmarkResponse deleteBookmark (@PathVariable (name = "id") long id){
       return bookmarkServiceImpl.deleteBookmark(id);
    }
}
