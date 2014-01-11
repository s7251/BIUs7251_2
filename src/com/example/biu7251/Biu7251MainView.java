package com.example.biu7251;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

public class Biu7251MainView extends CustomComponent implements View {
	

	private static final long serialVersionUID = 1L;
	
	final HorizontalLayout buttonBox = new HorizontalLayout();
	final VerticalLayout mainBox = new VerticalLayout();
	
	

    public static final String NAME = "";
    Label text = new Label();

       
    Button logoutButton = new Button("Wyloguj", new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
            // Wylogowanie
            getSession().setAttribute("user", null);
            Notification.show("Wylogowano",
                    "Do widzenia pacjencie!",
                    Notification.Type.HUMANIZED_MESSAGE);
            // Przejście na panel logowania
            getUI().getNavigator().navigateTo(NAME);
        }
    });

    public Biu7251MainView() {
    	
		
		buttonBox.addComponent(logoutButton);
    	buttonBox.setMargin(true);
		
	   	
		mainBox.addComponent(buttonBox);
	
        
        setCompositionRoot(new CssLayout(mainBox));
    }

   	@Override
    public void enter(ViewChangeEvent event) {
        // Pobieranie danych z sesji
        String username = String.valueOf(getSession().getAttribute("user"));
    }
}
