package dk.unf.software.aar2013.gruppe7;

import android.net.Uri;

public class Entry {

	/*
	 * skal indeholde data
	 * i form af title, text og billed uri.
	 * 
	 */
	
	private String text;
	private String title;
	private Uri photo;
	
	Entry(){
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Uri getPhoto() {
		return photo;
	}

	public void setPhoto(Uri photo) {
		this.photo = photo;
	}
	
	
	
}


