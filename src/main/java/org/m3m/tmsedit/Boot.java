package org.m3m.tmsedit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Boot extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(
				Boot.class.getResource("tmsedit.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 748, 480);
		stage.setTitle("TMSEdit");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}