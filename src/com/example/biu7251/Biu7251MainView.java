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
import com.vaadin.ui.Alignment;
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

	public static final String NAME = "";

	SQLContainer sqlCont;

	public Biu7251MainView() {

		sqlCont = new DatabaseHelper().getPersonContainer();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		VerticalLayout vl = new VerticalLayout();
		setCompositionRoot(vl);

		Button logoutButton = new Button("Wyloguj", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// Wylogowanie
				getSession().setAttribute("user", null);
				Notification.show("Wylogowano", "Do widzenia!",
						Notification.Type.HUMANIZED_MESSAGE);
				// Przejście na panel logowania
				getUI().getNavigator().navigateTo(NAME);
			}
		});

		vl.addComponent(logoutButton);
		vl.setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);

		Table t = new Table();
		t.setContainerDataSource(sqlCont);
		vl.addComponent(t);
		vl.setComponentAlignment(t, Alignment.MIDDLE_CENTER);

		final TextField FNAME = new TextField("Imię");
		FNAME.setInputPrompt("Podaj imię");
		vl.addComponent(FNAME);

		final TextField SNAME = new TextField("Nazwisko");
		SNAME.setInputPrompt("Podaj nazwisko");
		vl.addComponent(SNAME);

		final TextField TELEPHONE = new TextField("Telefon");
		TELEPHONE.setInputPrompt("Podaj numer telefonu");
		vl.addComponent(TELEPHONE);

		Button b = new Button("Dodaj");

		b.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (FNAME.getValue().length() > 0
						&& SNAME.getValue().length() > 0
						&& TELEPHONE.getValue().length() > 0) {
					addPerson(FNAME.getValue(), SNAME.getValue(),
							TELEPHONE.getValue());

				}

			}
		}

		);

		vl.addComponent(b);
		vl.setComponentAlignment(FNAME, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(SNAME, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(TELEPHONE, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(b, Alignment.MIDDLE_CENTER);

	}

	private void addPerson(String fname, String sname, String tel) {
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
