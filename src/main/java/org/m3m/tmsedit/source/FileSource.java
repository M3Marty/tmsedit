package org.m3m.tmsedit.source;

import lombok.*;

import java.io.*;

@Getter
@ToString
@AllArgsConstructor
public class FileSource implements DataSource {

	private File source;

	@Override
	public OutputStream put() throws FileNotFoundException {
		return new FileOutputStream(source);
	}

	@Override
	public InputStream get() throws FileNotFoundException {
		return new FileInputStream(source);
	}
}