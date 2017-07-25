package com.hungsum.framework.others;

import java.io.Serializable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class HsSaxHandler<T extends Serializable> extends DefaultHandler
{

	protected StringBuffer sb = new StringBuffer();
	
	protected T returnObject;
	
	public T getReturnObject()
	{
		return returnObject;
	}

	@Override
	public void startDocument() throws SAXException
	{
		super.startDocument();
		
		returnObject = getNewInstance();
	}

	protected abstract T getNewInstance();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		super.startElement(uri, localName, qName, attributes);

		sb.delete(0, sb.length());
	}
	
	

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException
	{
		super.characters(ch, start, length);
		
		sb.append(ch, start, length);
	}

	

}
