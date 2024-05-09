package org.m3m.tmsedit.parsers;

import org.m3m.tmsedit.documentation.Suite;

import java.io.*;

public interface Parser {
	Suite read(InputStream from) throws IOException;
	void write(OutputStream to) throws IOException;
}