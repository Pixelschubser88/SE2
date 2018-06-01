package com.businessapp.fxgui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.EquipmentDataIntf;
import com.businessapp.pojos.Product;
//import com.businessapp.pojos.Product.CustomerStatus;
import com.businessapp.pojos.LogEntry;

/**
 * FXML Controller class for Product.fxml
 * 
 */
public class EquipmentFXMLController implements FXMLControllerIntf {
	private EquipmentDataIntf DS;

	/**
	 * FXML skeleton defined as:
	 * AnchorPane > GridPane > TableView	- GridPane as resizable container for TableView
	 * AnchorPane > HBox > Button			- buttons in footer area
	 * 
	 * Defined CSS style classes:
	 *   .tableview-customer-column-id
	 *   .tableview-customer-column-name
	 *   .tableview-customer-column-status
	 *   .tableview-customer-column-contacts
	 *   .tableview-customer-column-notes
	 *   .tableview-customer-column-notes-button
	 *   .tableview-customer-hbox
	 */

	@FXML
	private AnchorPane fxCustomer_AnchorPane;

	@FXML
	private GridPane fxCustomer_GridPane;

	@FXML
	private TableView<Product> fxCustomer_TableView;

	@FXML
	private TableColumn<Product,String> fxCustomer_TableCol_ID;


	@FXML
	private HBox fxCustomer_HBox;	// Bottom area container for buttons, search box, etc.

	/*
	 * TableView model.
	 */
	private final ObservableList<Product> cellDataObservable = FXCollections.observableArrayList();

	private final String LABEL_ID		= "SKU";
	private final String LABEL_PUBLISHER= "Firmer";
	private final String LABEL_TYPE		= "Typ";
	private final String LABEL_STATUS	= "Status";
	private final String LABEL_NOTES	= "Anmerk.";
	//private final String LABEL_CONTACT	= "Kontakt";


	@Override
	public void inject( ControllerIntf dep ) {
		this.DS = (EquipmentDataIntf)dep;
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
		// Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
		fxCustomer_HBox.prefWidthProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).widthProperty().subtract( 12 ) );
		fxCustomer_HBox.prefHeightProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).heightProperty() );

		fxCustomer_GridPane.prefWidthProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).widthProperty().subtract( 16 ) );
		fxCustomer_GridPane.prefHeightProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).heightProperty().subtract( 70 ) );

		/*
		 * Bottom area HBox extends from the top across the entire AnchorPane hiding
		 * GridPane/TableView underneath (depending on z-stacking order). This prevents
		 * Mouse events from being propagated to TableView.
		 * 
		 * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
		 * to the underlying GridPane/TableView layer (Mouse event "transparency").
		 */
		fxCustomer_HBox.setPickOnBounds( false );

		/*
		 * Visualize resizing propagation by colored bounding boxes.
		 */
		//fxCustomer_GridPane.setStyle( "-fx-border-color: red;" );
		//fxCustomer_HBox.setStyle( "-fx-border-color: blue;" );

		fxCustomer_HBox.getStyleClass().add( "tableview-customer-hbox" );


		/*
		 * Construct TableView columns.
		 * 
		 * TableView presents a row/column cell rendering of an ObservableList<Object>
		 * model. Each cell computes a "value" from the associated object property that
		 * defines how the object property is visualized in a TableView.
		 * See also: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
		 * 
		 * TableView columns define how object properties are visualized and cell values
		 * are computed.
		 * 
		 * In the simplest form, cell values are bound to object properties, which are
		 * public getter-names of the object class, and visualized in a cell as text.
		 * 
		 * More complex renderings such as with graphical elements, e.g. buttons in cells,
		 * require overloading of the built-in behavior in:
		 *   - CellValueFactory - used for simple object property binding.
		 *   - CellFactory - overriding methods allows defining complex cell renderings. 
		 * 
		 * Constructing a TableView means defining
		 *   - a ObservableList<Object> model
		 *   - columns with name, css-style and Cell/ValueFactory.
		 * 
		 * Variation 1: Initialize columns defined in FXML.
		 *  - Step 1: associate a .css class with column.
		 *  - Step 2: bind cell value to object property (must have public property getters,
		 *            getId(), getName()).
		 */
		fxCustomer_TableCol_ID.getStyleClass().add( "tableview-customer-column-id" );
		fxCustomer_TableCol_ID.setText( LABEL_ID );
		fxCustomer_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "sku" ) );

		/*
		 * Variation 2: Programmatically construct TableView columns.
		 */

		TableColumn<Product,String> tableCol_VORNAME = new TableColumn<>( LABEL_PUBLISHER );
		tableCol_VORNAME.getStyleClass().add( "tableview-customer-column-publisher" );
		tableCol_VORNAME.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Product c = cellData.getValue();
			observable.set( c.getPublisher() );
			return observable;
		});


		TableColumn<Product,String> tableCol_NAME = new TableColumn<>( LABEL_TYPE );
		tableCol_NAME.getStyleClass().add( "tableview-customer-column-type" );
		tableCol_NAME.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Product c = cellData.getValue();
			observable.set( c.getType() );
			return observable;
		});

		TableColumn<Product,String> tableCol_STATUS = new TableColumn<>( LABEL_STATUS );
		tableCol_STATUS.getStyleClass().add( "tableview-customer-column-status" );
		tableCol_STATUS.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			// Render status as 3-letter shortcut of Customer state enum.
			Product c = cellData.getValue();
			observable.set( c.getStatus().name().substring( 0, 3 ) );
			return observable;
		});

	/*	TableColumn<Customer,String> tableCol_CONTACT = new TableColumn<>( LABEL_CONTACT );
		tableCol_CONTACT.getStyleClass().add( "tableview-customer-column-contacts" );

		tableCol_CONTACT.setCellValueFactory( cellData -> {
			StringProperty observable = new SimpleStringProperty();
			Customer c = cellData.getValue();
			List<String> conL = c.getContacts();
			// Show contacts with "[number-of-contacts]" indicator followed by first contact
			// in list or "-" if no contacts exist.
			String show = "-";
			if( conL.size() > 0 ) {
				//show = "[" + cL.size() + "] " + cL.get( 0 );
				show = contactsToString( conL );
			}
			observable.set( show );
			return observable;
		});
*/
		// TableColumn<Customer,String> tableCol_NOTES = new TableColumn<>( "Notes" );
		TableColumn<Product,String> tableCol_NOTES = new TableColumn<>( LABEL_NOTES );
		tableCol_NOTES.getStyleClass().add( "tableview-customer-column-notes" );

		tableCol_NOTES.setCellFactory(

			// Complex rendering of Notes column as clickable button with number of notes indicator.
			new Callback<TableColumn<Product,String>, TableCell<Product, String>>() {

				@Override
				public TableCell<Product, String> call( TableColumn<Product, String> col ) {

					col.setCellValueFactory( cellData -> {
						Product c = cellData.getValue();
						StringProperty observable = new SimpleStringProperty();
						observable.set( c.getId() );
						return observable;
					});

					TableCell<Product, String> tc = new TableCell<Product, String>() {
						final Button btn = new Button();

						@Override public void updateItem( final String item, final boolean empty ) {
							super.updateItem( item, empty );
							int rowIdx = getIndex();
							ObservableList<Product> cust = fxCustomer_TableView.getItems();

							if( rowIdx >= 0 && rowIdx < cust.size() ) {
								Product product = cust.get( rowIdx );
								setGraphic( null );		// always clear, needed for refresh
								if( product != null ) {
									btn.getStyleClass().add( "tableview-product-column-notes-button" );
									List<LogEntry> nL = product.getNotes();
									btn.setText( "notes: " + nL.size() );
									setGraphic( btn );	// set button as rendering of cell value
	
									//Event updateEvent = new ActionEvent();
									btn.setOnMouseClicked( event -> {
										String n = product.getType();
										String label = ( n==null || n.length()==0 )? product.getId() : n;
	
										PopupNotes popupNotes = new PopupNotes( label, nL );
	
										popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
											// Notification that List<Note> has been updated.
											// update button label [note: <count>]
											btn.setText( "notes: " + product.getNotes().size() );
											// -> save node
											DS.updateProduct( product );
										});
	
										popupNotes.show();
									});
								}
							} else {
								//System.out.println( "OutOfBounds rowIdx() ==> " + rowIdx );
								setGraphic( null );		// reset button in other rows
							}
						}
					};
					return tc;
				}
			});

		// Add programmatically generated columns to TableView. Columns appear in order.
		fxCustomer_TableView.getColumns().clear();
		fxCustomer_TableView.getColumns().addAll( Arrays.asList(
			fxCustomer_TableCol_ID,
			tableCol_STATUS,
			tableCol_NOTES,
			tableCol_VORNAME,
			tableCol_NAME
			//tableCol_CONTACT
		));

		/*
		 * Define selection model that allows to select multiple rows.
		 */
		fxCustomer_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

		/*
		 * Allow horizontal column squeeze of TableView columns. Column width can be fixed
		 * with -fx-pref-width: 80px;
		 */
		fxCustomer_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


		/*
		 * Double-click on row: open update dialog.
		 */
		fxCustomer_TableView.setRowFactory( tv -> {
			TableRow<Product> row = new TableRow<>();
			row.setOnMouseClicked( event -> {
				if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
					// Customer rowData = row.getItem();
					// fxCustomer_TableView.getSelectionModel().select( row.getIndex() );
					//table.getSelectionModel().select( Math.min( i, size - 1 ) );
					fxCustomer_Update();
				}
			});
			return row;
		});

		/*
		 * Load objects into TableView model.
		 */
		fxCustomer_TableView.getItems().clear();
		Collection<Product> col = DS.findAllProducts();
		if( col != null ) {
			cellDataObservable.addAll( col );
		}
		fxCustomer_TableView.setItems( cellDataObservable );
	}

	@Override
	public void stop() {
	}


	@FXML
	void fxCustomer_Delete() {
		ObservableList<Product> selection = fxCustomer_TableView.getSelectionModel().getSelectedItems();
		List<Product> toDel = new ArrayList<Product>();
		List<String> ids = new ArrayList<String>();
		for( Product c : selection ) {
			toDel.add( c );
		}
		fxCustomer_TableView.getSelectionModel().clearSelection();
		for( Product c : toDel ) {
			ids.add( c.getId() );
			// should not alter cellDataObservable while iterating over selection
			cellDataObservable.remove( c );
		}
		DS.deleteProducts( ids );
	}

	@FXML
	void fxCustomer_New() {
		Product product = DS.newProduct(null,null);
		openUpdateDialog( product, true );
	}

	@FXML
	void fxCustomer_Update() {
		Product product = fxCustomer_TableView.getSelectionModel().getSelectedItem();
		if( product != null ) {
			openUpdateDialog( product, false );
		//} else {
		//	System.err.println( "nothing selected." );
		}
	}

	@FXML
	void fxCustomer_Exit() {
		App.getInstance().stop();
	}


	/*
	 * Private helper methods.
	 */
	private final String SEP = ";";		// separates contacts in externalized String

	private void openUpdateDialog( Product c, boolean newItem ) {
		List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
		String n = c.getPublisher();
		String label = ( n==null || n.length()==0 )? c.getId() : n;

		PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
			new StringTestUpdateProperty( LABEL_ID, c.getId(), false ),
			new StringTestUpdateProperty( LABEL_TYPE, c.getType(), true ),
			new StringTestUpdateProperty( LABEL_STATUS, c.getStatus().name(), true )
			//new StringTestUpdateProperty( LABEL_CONTACT, contactsToString( c.getContacts() ), true )
		));

		// called when "OK" button in EntityEntryDialog is pressed
		dialog.addEventHandler( ActionEvent.ACTION, event -> {
			updateObject( c, altered, newItem );
		});

		dialog.show();
	}

	private void updateObject( Product product, List<StringTestUpdateProperty> altered, boolean newItem ) {
		for( StringTestUpdateProperty dp : altered ) {
			String pName = dp.getName();
			String alteredValue = dp.getValue();
			//System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

			if( pName.equals( LABEL_TYPE ) ) {
				product.setType( alteredValue );
			}
			if( pName.equals( LABEL_STATUS ) ) {
				String av = alteredValue.toUpperCase();
				if( av.startsWith( "ACT" ) ) {
					product.setStatus( Product.ProductStatus.ACTIVE );
				}
				if( av.startsWith( "SUS" ) ) {
					product.setStatus( Product.ProductStatus.SUSPENDED);
				}
				if( av.startsWith( "TER" ) ) {
					product.setStatus( Product.ProductStatus.TERMINATED );
				}
			}
			/*if( pName.equals( LABEL_CONTACT ) ) {
				customer.getContacts().clear();
				for( String s : alteredValue.split( SEP ) ) {
					if( s != null ) {
						s = s.trim();
						if( s.length() > 0 ) {
							customer.getContacts().add( s );
						}
					}
				}
			}*/
		}
		if( altered.size() > 0 ) {
			DS.updateProduct( product );	// update object in persistent store
			if( newItem ) {
				int last = cellDataObservable.size();
				cellDataObservable.add( last, product );
			}
			// refresh TableView (trigger update
			fxCustomer_TableView.getColumns().get(0).setVisible(false);
			fxCustomer_TableView.getColumns().get(0).setVisible(true);

			altered.clear();	// prevent double save if multiple events fire
		}
	}

	private String contactsToString( List<String> con ) {
		StringBuffer sb = new StringBuffer();
		for( int i=0; i < con.size(); i++ ) {
			sb.append( con.get( i ) );
			if( i < con.size() - 1 ) {
				sb.append( SEP ).append( " " );
			}
		}
		return sb.toString();
	}

}
