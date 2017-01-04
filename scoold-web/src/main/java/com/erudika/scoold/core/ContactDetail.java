/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.erudika.scoold.core;

import com.erudika.scoold.utils.AppConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Alex Bogdanovski [alex@erudika.com]
 */
public class ContactDetail {

    private String subType;
    private String value;

	public static final String SEPARATOR = ";";

	public static enum ContactDetailType{
		UNKNOWN, WEBSITE, ADDRESS, FACEBOOK, TWITTER, SKYPE,
		MSN, AIM, GTALK, QQ, YAHOO, ICQ, EBUDDY;

		public String toString() {
			switch (this) {
				case WEBSITE: return "Website";
				case ADDRESS: return "Address";
				case FACEBOOK: return "Facebook";
				case TWITTER: return "Twitter";
				case SKYPE: return "Skype";
				case MSN: return "Live Messenger";
				case AIM: return "AOL IM";
				case GTALK: return "Google Talk";
				case QQ: return "Tencent QQ";
				case YAHOO: return "Yahoo!";
				case ICQ: return "ICQ";
				case EBUDDY: return "eBuddy";
				default: return UNKNOWN.toString().toLowerCase();
			}
		}
	};

	public ContactDetail() {
		this(ContactDetailType.UNKNOWN.toString(), "");
    }

	public ContactDetail(String type, String value) {
		this.setSubType(type);
		this.value = value;
	}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = getContactDetailType(value).toString();
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = getContactDetailType(subType).name();
    }

	public static String getContactsFromParamsMap(Map<String, String[]> params) {
		if (params == null)return "";
		String contacts = "";
		if (params.containsKey("contacts")) {
			String[] values = params.get("contacts");
			//a special case of multi-value param - contacts
			int max = (values.length > AppConfig.MAX_CONTACT_DETAILS)
					? AppConfig.MAX_CONTACT_DETAILS : values.length;
			for (int i = 0; i < max; i++) {
				String contact = values[i];
				if (!StringUtils.isBlank(contact)) {
					String[] tuParts = contact.split(",");
					if (tuParts.length == 2) {
						tuParts[1] = tuParts[1].replaceAll(";", "");
						contacts = contacts.concat(tuParts[0]).concat(",").
								concat(tuParts[1]).concat(";");
					}
				}
			}
			contacts = StringUtils.removeEnd(contacts, ";");
		}
		return contacts;
	}
	
	public static List<ContactDetail> toContactsList(String details) {
		ArrayList<ContactDetail> list = new ArrayList<ContactDetail>();
		if (StringUtils.isBlank(details)) return list;
		for (String detail : details.split(SEPARATOR)) {
			String[] twoParts = detail.split(",");
			if (twoParts.length == 2) {
				list.add(new ContactDetail(twoParts[0], twoParts[1]));
			}
		}
		return list;
	}

	private ContactDetailType getContactDetailType(String type) {
		if (type == null) return ContactDetailType.UNKNOWN;
		try{
			return ContactDetailType.valueOf(type.toUpperCase());
        }catch(IllegalArgumentException e) {
            //oh shit!
			return ContactDetailType.UNKNOWN;
        }
	}

	public String toString() {
		if (this.subType == null || this.value == null) 
			this.subType = ContactDetailType.UNKNOWN.name();
		return subType+","+value;
	}

    public boolean equals(Object obj) {
        if (this == obj)
                return true;
        if ((obj == null) || (obj.getClass() != this.getClass()))
                return false;
        ContactDetail other = (ContactDetail) obj;

        return (this.value.equals(other.getValue()) &&
				this.subType.equals(other.getSubType()));
    }

    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

}
