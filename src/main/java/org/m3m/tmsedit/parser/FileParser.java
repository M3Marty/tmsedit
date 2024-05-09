package org.m3m.tmsedit.parser;

import org.m3m.tmsedit.documentation.Suite;

import java.io.*;

public interface FileParser extends Parser {

	default Suite read(File file) throws IOException {
		return read(new FileInputStream(file));
	}

	default void write(File file) throws IOException {
		write(new FileOutputStream(file));
	}
}