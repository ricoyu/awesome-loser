package com.loserico.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * https://howtodoinjava.com/lucene/lucene-index-search-examples/
 * <p>
 * Copyright: (C), 2020/1/8 16:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class LuceneWriteIndexExample {
	
	/**
	 * Once you execute above code in your computer, you will see lucene indexes created in configured folder path.
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//To write lucene documents to index, use IndexWriter.addDocuments(documents) method.
		IndexWriter writer = createWriter();
		
		List<Document> documents = new ArrayList<>();
		Document doc1 = createDocument(1, "Lokesh", "Gupta", "howtodoinjava.com");
		documents.add(doc1);
		Document doc2 = createDocument(2, "Brian", "Schultz", "example.com");
		documents.add(doc2);
		
		//Let's clean everything first
		writer.deleteAll();
		
		writer.addDocuments(documents);
		writer.commit();
		writer.close();
	}
	
	/**
	 * org.apache.lucene.index.IndexWriter class provides functionality to create and manage index.
	 * It’s constructor takes two arguments: FSDirectory and IndexWriterConfig.
	 * Please note that after the writer is created, the given configuration instance cannot be passed to another writer.
	 *
	 * @return IndexWriter
	 */
	private static IndexWriter createWriter() throws IOException {
		FSDirectory dir = FSDirectory.open(Paths.get(System.getProperty("user.home")));
		IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter indexWriter = new IndexWriter(dir, config);
		return indexWriter;
	}
	
	/**
	 * org.apache.lucene.document.Document class represent Lucene indexed document.
	 *
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param website
	 * @return Document
	 */
	private static Document createDocument(Integer id, String firstName, String lastName, String website) {
		Document document = new Document();
		document.add(new StringField("id", id.toString(), Field.Store.YES));
		document.add(new TextField("firstName", firstName, Field.Store.YES));
		document.add(new TextField("lastName", lastName, Field.Store.YES));
		document.add(new TextField("website", website, Field.Store.YES));
		return document;
	}
}
