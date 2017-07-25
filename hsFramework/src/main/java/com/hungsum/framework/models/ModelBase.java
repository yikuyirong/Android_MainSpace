package com.hungsum.framework.models;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.hungsum.framework.others.HsSaxHandler;

public abstract class ModelBase<T extends Serializable> implements Serializable
{
	private static final long serialVersionUID = -3514845349000537252L;

	protected HsSaxHandler<T> handler;
	
	protected abstract HsSaxHandler<T> getHandler();

	public ModelBase()
	{
		this.handler = getHandler();
	}
	
	public T Create(String value) throws SAXException, ParserConfigurationException, IOException
	{
		XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		reader.setContentHandler(handler);
		reader.parse(new InputSource(new StringReader(value)));
		return handler.getReturnObject();
	}
}
