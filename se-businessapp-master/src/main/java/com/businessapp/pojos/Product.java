package com.businessapp.pojos;

import java.util.ArrayList;
import java.util.List;

import com.businessapp.logic.IDGen;


/**
 * Product is an entity that represents a person (or a business)
 * to which a business activity can be associated.
 *
 */
public class Product implements EntityIntf  {
	private static final long serialVersionUID = 1L;

	private static IDGen IDG = new IDGen( "C.", IDGen.IDTYPE.AIRLINE, 6 );

	// Product states.
	public enum ProductStatus { ACTIVE, SUSPENDED, TERMINATED };


	/*
	 * Properties.
	 */
	private String id = null;

	private String publisher = null;

	private String type = null;

	//private List<String> contacts = new ArrayList<String>();

	private List<LogEntry> notes = new ArrayList<LogEntry>();

	private ProductStatus status = ProductStatus.ACTIVE;


	/**
	 * Private default constructor (required by JSON deserialization).
	 */
	@SuppressWarnings("unused")
	private Product() { }

	/**
	 * Public constructor.
	 * @param id if Product id is null, an id is generated for the new Product object.
	 * @param type Product.
	 */
	public Product( String id, String publisher, String type ) {
		this.id = id==null? IDG.nextId() : id;
		this.publisher=publisher;
		this.type = type;
		this.notes.add( new LogEntry( "Product record created." ) );
	}


	/**
	 * Public getter/setter methods.
	 */
	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getPublisher() {
		return publisher;
	}

//	public List<String> getContacts() {
//		return contacts;
//	}

	public List<String> getNotesAsStringList() {
		List<String>res = new ArrayList<String>();
		for( LogEntry n : notes ) {
			res.add( n.toString() );
		}
		return res;
	}

	public List<LogEntry> getNotes() {
		return notes;
	}

	public ProductStatus getStatus() {
		return status;
	}

	public Product setType( String type ) {
		this.type = type;
		return this;
	}

	public Product setPublisher( String publisher ) {
		this.publisher = publisher;
		return this;
	}

/*	public Product addContact( String contact ) {
		contacts.add( contact );
		return this;
	}*/

	public Product setStatus( ProductStatus status ) {
		this.status = status;
		return this;
	}

}
