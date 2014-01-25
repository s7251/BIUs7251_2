package com.example.biu7251;

import java.sql.SQLException;

import com.vaadin.annotations.Title;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Title("Addressbook")
public class Biu7251MainView extends CustomComponent implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Baza
	
	
	
	
//	container.getItem(container.addItem()).getItemProperty("FNAME").setValue("asdA");
	
	// Zdefiniowanie komponentów dla widoku
	
	private Table contactList = new Table();
	private TextField searchField = new TextField();
	private Button addNewContactButton = new Button("Nowy");
	private Button removeContactButton = new Button("Usuń Kontakt");
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();
	
	public static final String NAME = "";
	private static final String FNAME = "Imię";
	private static final String LNAME = "Nazwisko";
	private static final String TELEPHONE = "Telefon";
	private static final String[] fieldNames = new String[] { FNAME, LNAME,
		TELEPHONE };

	Button logoutButton = new Button("Wyloguj", new Button.ClickListener() {

	        @Override
	        public void buttonClick(ClickEvent event) {
	            // Wylogowanie
	            getSession().setAttribute("user", null);
	            Notification.show("Wylogowano",
	                    "Do widzenia!",
	                    Notification.Type.HUMANIZED_MESSAGE);
	            // Przejście na panel logowania
	            getUI().getNavigator().navigateTo(NAME);
	        }
	    });
	
	/*
	 * Any component can be bound to an external data source. This example uses
	 * just a dummy in-memory list, but there are many more practical
	 * implementations.
	 */
	IndexedContainer contactContainer = createDummyDatasource();
	SQLContainer sqlCont;
	

	public Biu7251MainView(){
//		initLayout();
//		initContactList();
//		initEditor();
//		initSearch();
//		initAddRemoveButtons();
		sqlCont=new DatabaseHelper().getPersonContainer();
		
		
		
		
	}

	private void initLayout() {
		// inicjacja layoutu dla komponentów widoku
		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		setCompositionRoot(splitPanel);
		
		// budowanie drzewa komponentów
		VerticalLayout leftLayout = new VerticalLayout ();
		splitPanel.addComponent(leftLayout);
		splitPanel.addComponent(editorLayout);
		leftLayout.addComponent(contactList);
		HorizontalLayout bottomLeftLayout = new HorizontalLayout();
		leftLayout.addComponent(bottomLeftLayout);
		bottomLeftLayout.addComponent(searchField);
		bottomLeftLayout.addComponent(addNewContactButton);
		bottomLeftLayout.addComponent(logoutButton);

		// ustawienie max rozmiaru dla lewej części layoutu w splitPanelu
		leftLayout.setSizeFull();

		// ustawienie max rozmiaru dla listy kontaktów
		leftLayout.setExpandRatio(contactList, 1);
		contactList.setSizeFull();

		// ustawienie 100% szerokości dla pola wyszukiwania po dodaniu przycisków Dodaj i Wyloguj; wysokość ustawiana taka jak najwyższy komponent
		bottomLeftLayout.setWidth("100%");
		searchField.setWidth("100%");
		bottomLeftLayout.setExpandRatio(searchField, 1);

		// ustawienie małego marginesu po prawej stronie edytora
		editorLayout.setMargin(true);
		editorLayout.setVisible(false);
	}

	private void initEditor() {

		editorLayout.addComponent(removeContactButton);

		/* User interface can be created dynamically to reflect underlying data. */
		for (String fieldName : fieldNames) {
			TextField field = new TextField(fieldName);
			editorLayout.addComponent(field);
			field.setWidth("100%");

			/*
			 * We use a FieldGroup to connect multiple components to a data
			 * source at once.
			 */
			editorFields.bind(field, fieldName);
		}

		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
		editorFields.setBuffered(false);
	}
	
	private void initSearch() {

		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
		searchField.setInputPrompt("Search contacts");

		/*
		 * Granularity for sending events over the wire can be controlled. By
		 * default simple changes like writing a text in TextField are sent to
		 * server with the next Ajax call. You can set your component to be
		 * immediate to send the changes to server immediately after focus
		 * leaves the field. Here we choose to send the text over the wire as
		 * soon as user stops writing for a moment.
		 */
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(final TextChangeEvent event) {

				/* Reset the filter for the contactContainer. */
				contactContainer.removeAllContainerFilters();
				contactContainer.addContainerFilter(new ContactFilter(event
						.getText()));
			}
		});
	}

	/*
	 * A custom filter for searching names and companies in the
	 * contactContainer.
	 */
	private class ContactFilter implements Filter {
		private String needle;

		public ContactFilter(String needle) {
			this.needle = needle.toLowerCase();
		}

		public boolean passesFilter(Object itemId, Item item) {
			String haystack = ("" + item.getItemProperty(FNAME).getValue()
					+ item.getItemProperty(LNAME).getValue() + item
					.getItemProperty(TELEPHONE).getValue()).toLowerCase();
			return haystack.contains(needle);
		}

		public boolean appliesToProperty(Object id) {
			return true;
		}
	}

	private void initAddRemoveButtons() {
		addNewContactButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

				/*
				 * Rows in the Container data model are called Item. Here we add
				 * a new row in the beginning of the list.
				 */
				contactContainer.removeAllContainerFilters();
				Object contactId = contactContainer.addItemAt(0);

				/*
				 * Each Item has a set of Properties that hold values. Here we
				 * set a couple of those.
				 */
				contactList.getContainerProperty(contactId, FNAME).setValue(
						"New");
				contactList.getContainerProperty(contactId, LNAME).setValue(
						"Contact");

				/* Lets choose the newly created contact to edit it. */
				contactList.select(contactId);
			}
		});

		removeContactButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				Object contactId = contactList.getValue();
				contactList.removeItem(contactId);
			}
		});
	}

	private void initContactList() {
		contactList.setContainerDataSource(contactContainer);
		
		contactList.setSelectable(true);
		contactList.setImmediate(true);

		contactList.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object contactId = contactList.getValue();

				// Uaktywnienie edytora gdy contactId != null 
				if (contactId != null)
					editorFields.setItemDataSource(contactList
							.getItem(contactId));
				
				editorLayout.setVisible(contactId != null);
			}
		});
	}

	/*
	 * Generate some in-memory example data to play with. In a real application
	 * we could be using SQLContainer, JPAContainer or some other to persist the
	 * data.
	 */
	@SuppressWarnings("unchecked")
	private static IndexedContainer createDummyDatasource() {
		IndexedContainer ic = new IndexedContainer();

		for (String p : fieldNames) {
			ic.addContainerProperty(p, String.class, "");
		}

		// Dodawanie użytkowników do listy
			
	
	
             Object id = ic.addItem();
             ic.getContainerProperty(id, FNAME).setValue("Krystian");
             ic.getContainerProperty(id, LNAME).setValue("Kulas");
             
     

		return ic;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		VerticalLayout vl = new VerticalLayout();
		setCompositionRoot(vl);
		Table t = new Table();
	
		t.setContainerDataSource(sqlCont );
		vl.addComponent(t);
		
		final TextField name = new TextField("imie");
		name.setInputPrompt("podaj imię");
		vl.addComponent(name);
		
		Button b = new Button("dodaj");
		
		
		b.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if (name.getValue().length()>0)
				{
					addPerson(name.getValue(), "jakis", "64654");
					
				}
				
			}
		});
		
		vl.addComponent(b);
		
//		Object itemId = sqlCont.addItem();
//		Item item = sqlCont.getItem(itemId);
		
		}
	
	private void addPerson(String fname, String sname, String tel){
		  try {
			  sqlCont.rollback();
		  } catch (SQLException ignored) {
		  }
		  Object tempItemId = sqlCont.addItem();
		  
		  sqlCont.getContainerProperty(tempItemId, "FNAME").setValue(fname);
		  sqlCont.getContainerProperty(tempItemId, "SNAME").setValue(sname); 
		  sqlCont.getContainerProperty(tempItemId, "TELEPHONE").setValue(tel); 
	      try {
	    	  sqlCont.commit();
		} catch (UnsupportedOperationException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
		
	

}


