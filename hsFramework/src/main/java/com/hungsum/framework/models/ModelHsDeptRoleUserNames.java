package com.hungsum.framework.models;

import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.hungsum.framework.others.HsSaxHandler;

public class ModelHsDeptRoleUserNames extends ModelBase<ArrayList<String>>
{
	private static final long serialVersionUID = -1230553920523138743L;

	@Override
	protected HsSaxHandler<ArrayList<String>> getHandler()
	{
		return new HsRoleUsersDataHandler();
	}

	protected class HsRoleUsersDataHandler extends HsSaxHandler<ArrayList<String>>
	{
		private String mCurrentFlag = "U";
		
		@Override
		protected ArrayList<String> getNewInstance()
		{
			return new ArrayList<String>();
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException
		{
			super.endElement(uri, localName, qName);

			if(localName.equals("Flag"))
			{
				if(sb.toString().equals("SYSUSER"))
				{
					mCurrentFlag = "U";
				}else if(sb.toString().equals("SYSROLE"))
				{
					mCurrentFlag = "R";
				}else if (sb.toString().equals("SYSDEPT"))
				{
					mCurrentFlag = "D";
				}
			}else if(localName.equals("Name"))
			{
				this.returnObject.add(mCurrentFlag + ":" + sb.toString());
			}
		}
	}
}
