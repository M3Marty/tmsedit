module org.m3m.tmsedit {
	requires static lombok;

	requires javafx.controls;
	requires javafx.fxml;
	requires java.xml;

	opens org.m3m.tmsedit to javafx.fxml, javafx.graphics;
	opens org.m3m.tmsedit.source to javafx.fxml;
	opens org.m3m.tmsedit.history to javafx.fxml;

	exports org.m3m.tmsedit.logging;
	exports org.m3m.tmsedit.source;
	exports org.m3m.tmsedit.history;
	exports org.m3m.tmsedit.editors;
	exports org.m3m.tmsedit.documentation;
}