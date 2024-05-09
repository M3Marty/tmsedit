module org.m3m.tmsedit {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.xml;
	requires static lombok;

	opens org.m3m.tmsedit to javafx.fxml;
	exports org.m3m.tmsedit;
	exports org.m3m.tmsedit.logging;
	exports org.m3m.tmsedit.source;
	opens org.m3m.tmsedit.source to javafx.fxml;
}