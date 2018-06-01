package com.businessapp.logic;

import java.util.Collection;

import com.businessapp.ControllerIntf;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Product;

/**
 * Public interface to Product data.
 *
 */
public interface EquipmentDataIntf extends ControllerIntf {

	/**
	 * Factory method that returns a Product data source.
	 * @return new instance of Product data source.
     * @param katalog
     * @param persistenceProvider
	 */
	public static EquipmentDataIntf getController(String katalog, PersistenceProviderIntf persistenceProvider) {
		return new EquipmentDataMockImpl();
	}

	/**
	 * Public access methods to Product data.
	 */
	Product findProductById( String id );

	public Collection<Product> findAllProducts();

	public Product newProduct( String firstname, String name );

	public void updateProduct( Product c );

	public void deleteProducts( Collection<String> ids );

}
