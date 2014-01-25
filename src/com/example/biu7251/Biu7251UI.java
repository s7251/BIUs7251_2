package com.example.biu7251;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
public class Biu7251UI extends UI {

	@WebServlet(value = {"/ui/*", "/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Biu7251UI.class)
	public static class Servlet extends VaadinServlet {
	}
	
	@Override
	protected void init(VaadinRequest request) {
		      

        
        new Navigator(this, this);// stworzenie nowego nawigatora
        getNavigator().addView(Biu7251LoginView.NAME, Biu7251LoginView.class); // panel logowania jako główny
        getNavigator().addView(Biu7251MainView.NAME,  Biu7251MainView.class); //dodanie głównego widoku
                       
        
        // użycie obsługi zmian widoku aby zapewnić przekierowanie do widoku logowania jeśli user nie zalogowany
        
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            
            public boolean beforeViewChange(ViewChangeEvent event) {
                
                // Weryfikacja zalogowania
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof Biu7251LoginView;

                if (!isLoggedIn && !isLoginView) {
                    // gdy nie jest zalogowany to przekieruj na logowanie
                    getNavigator().navigateTo(Biu7251LoginView.NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // jeżeli zalogowany - nie wpuszczaj do logowania
                    return false;
                }

                return true;
            }
            
            public void afterViewChange(ViewChangeEvent event) {
                
            }
        });
    }

}