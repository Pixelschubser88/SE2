package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Product;

import java.io.IOException;
import java.util.Collection;

public class EquipmentDataSource implements EquipmentDataIntf {



        private final GenericEntityContainer<Product> Products;
        private PersistenceProviderIntf persistenceProvider = null;

        /**
         *      * Factory method that returns a CatalogItem data source.
         *      * @return new instance of data source.
         *      
         */
        public static EquipmentDataIntf getController(String name, PersistenceProviderIntf persistenceProvider) {
            EquipmentDataIntf cds = new EquipmentDataSource(name);
            cds.inject(persistenceProvider);
            return cds;
        }

        /**
         *      * Private constructor.
         *      
         */
        private EquipmentDataSource(String name) {
            this.Products = new GenericEntityContainer<Product>(name, Product.class);
        }

        @Override
        public void inject(ControllerIntf dep) {
            if (dep instanceof PersistenceProviderIntf) {
                this.persistenceProvider = (PersistenceProviderIntf) dep;
            }
        }

        @Override
        public void inject(Component parent) {

        }

        @Override


        public void start() {

            if (persistenceProvider != null) {
                try {
/*
   * Attempt to load container from persistent storage.
   */
                    persistenceProvider.loadInto(Products.getId(),entity -> {this.Products.store((Product) entity);return true;
                    });
                } catch (IOException e) {
                    System.out.print(", ");
                    System.err.print("No data: " + Products.getId());
                }
            }
        }

        @Override
        public void stop() {

        }

        @Override
        public Product findProductById(String id) {
            return Products.findById(id);
        }

        @Override
        public Collection<Product> findAllProducts() {
            return Products.findAll();
        }

        @Override
        public Product newProduct(String firstname, String name) {
            Product Product = new Product(null,firstname,name);
            Products.store(Product);
            if( persistenceProvider != null) persistenceProvider.save(Products, Products.getId());
            return Product;
        }

        @Override
        public void updateProduct(Product c) {
            Products.update(c);
            if( persistenceProvider != null) persistenceProvider.save(Products, Products.getId());
        }

        @Override
        public void deleteProducts(Collection<String> ids) {
            Products.delete(ids);
            if( persistenceProvider != null) persistenceProvider.save(Products, Products.getId());

        }
// TODO: remaining methods of ProductDataIntf.java
    }


