package com.cisco.demo.service;

import java.util.Date;

import com.cisco.demo.model.BookmarkModel;
import com.cisco.demo.model.BookmarkResponse;

public interface BookmarkService {

	/**
     * This method is used for creating / saving bookmark object
     * 
     * @param request
     * @return
     */
    public BookmarkResponse create(BookmarkModel request);
    /**
     * This method is used for returning a single bookmark object based on bookmark id passed as parameter
     * 
     * @param id
     * @return
     */
	public BookmarkResponse getBookmark(long id);
	/**
	 * This method returns specific bookmark based on bookmark name passed as parameter along with page number and page size passed as parameter
	 * 
	 * @param bookmarkName
	 * @param page
	 * @param size
	 * @return
	 */
	public BookmarkResponse getBookmarksByName(String bookmarkName, int page, int size);
	/**
	 * This method returns date range specific bookmark list as per the page number and page size passed as parameter
	 * 
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param size
	 * @return
	 */
	public BookmarkResponse getBookmarksByDateRange(Date startDate, Date endDate, int page, int size);
	/**
     * This method is used for returning all bookmarks as per the page and the size of the page passed as parameter
     * 
     * @param page
     * @param size
     * @return
     */
	public BookmarkResponse getAllBookmarks(int page, int size);
	/**
	 * This method returns user specific bookmark list based on user id passed as parameter
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	public BookmarkResponse updateBookmark(long id, BookmarkModel request);
	/**
	 * This method deletes a particular bookmark with id passed as parameter
	 * 
	 * @param id
	 * @return
	 */
	public BookmarkResponse deleteBookmark(long id);
}
