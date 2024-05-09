package org.m3m.tmsedit.source;

import java.io.*;

public interface DataSource {

	InputStream get() throws IOException;
	OutputStream put() throws IOException;
}