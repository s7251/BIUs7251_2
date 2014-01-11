package com.example.biu7251;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class Biu7251LoginView extends CustomComponent implements View,
		Button.ClickListener {

	public static final String NAME = "login";
	private final TextField user;
	
	private final PasswordField password;
	private final Button loginButton;

	public Biu7251LoginView() {
		setSizeFull();

		// login
		
		user = new TextField("Login:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Wpisz Twój pesel jako login (89051011492)");

		user.setInvalidAllowed(false);

		// hasło
		password = new PasswordField("Hasło:");
		password.setWidth("300px");
		password.addValidator(new PasswordValidator());
		password.setRequired(true);
		password.setValue("");
		password.setNullRepresentation("");

		loginButton = new Button("Zaloguj", this);

		// Panel logowania
		VerticalLayout fields = new VerticalLayout(user, password, loginButton);
		fields.setCaption("### Witamy w elektronicznym systemie pacjenta! ###");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// Layout
		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
		user.focus();
	}

	//
	// Walidator hasła
	//
	private static final class PasswordValidator extends
			AbstractValidator<String> {

		public PasswordValidator() {
			super("Hasło jest nieprawidłowe");
		}

		@Override
		protected boolean isValidValue(String value) {
			
			// Hasło musi się składać z co najmniej 5 znaków
			
			if (value != null && (value.length() < 5 )) {
				return false;
			}
			return true;
		}

		@Override
		public Class<String> getType() {
			return String.class;
		}
	}

	public void buttonClick(ClickEvent event) {

		
		// Walidacja pól
		if (!user.isValid() || !password.isValid()) {
			Notification.show("Hasło musi posiadać minimum 5 znaków", "",
					Notification.Type.ERROR_MESSAGE);
			this.password.setValue(null);
			this.password.focus();
			return;
		}

		String username = user.getValue();
		String password = this.password.getValue();

		//
		// Walidacja loginu i hasła
		//
		boolean isValid = username.equals("89051011492") && password.equals("wpuscmnie");

		if (isValid) {
			Notification.show("Zalogowano",
					"Witamy w elektronicznym systemie pacjenta",
					Notification.Type.HUMANIZED_MESSAGE);

			// zapisz w sesji
			getSession().setAttribute("user", username);

			// przejście do głównego widoku
			getUI().getNavigator().navigateTo(Biu7251MainView.NAME);

		} else {
			Notification.show("Podaj poprawne dane", "",
					Notification.Type.ERROR_MESSAGE);
			
			// czyszczenie pól po nieprawidłowym logowaniu
			this.password.setValue(null);
			this.password.focus();
		}
	}
}
