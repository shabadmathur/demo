package com.cisco.demo.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty(message = "User name cannot be empty")
    @Column(name = "name")
    private String name;
    
    
    @Column(name = "email", unique = true)
    @Email(message = "Email must be a valid email address")
    private String email;
    
    @NotEmpty(message = "Creation date cannot be empty")
    @Column(name = "creationDate")
    @DateTimeFormat(pattern = "MM/dd/YYYY")
    private String creationDate;
    
    @ManyToMany(targetEntity = Bookmark.class, cascade = {CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name = "user_bookmark_mapping", 
    		   joinColumns = @JoinColumn( name="user_id", referencedColumnName="id"),
    		   inverseJoinColumns = @JoinColumn(name="bookmark_id", referencedColumnName="id"))
    private List<Bookmark> bookmarks;
}
