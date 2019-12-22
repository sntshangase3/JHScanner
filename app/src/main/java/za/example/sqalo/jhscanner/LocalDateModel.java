/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.example.sqalo.jhscanner;

/**
 * {@link LocalDateModel} is the output produced by {@link Parser}
 * 
 * @author Vaibhav Singh
 * 
 */
public class LocalDateModel {

	/**
	 * original text identified in the source text
	 **/
	private String originalText;
	/**
	 * formatted date time found in the text, this is fully qualified formatted
	 * string representation of date
	 * */
	private String dateTimeString;
	/**
	 * format in which {@linkplain dateTimeString} is represented
	 **/
	private String conDateFormat;
	/**
	 * format in which date is present
	 */
	private String identifiedDateFormat;
	/**
	 * start of identified text fragment in the source text
	 */
	private int start;
	/**
	 * end of identified text fragment in the source text
	 */
	private int end;

	public String getOriginalText() {
		return originalText;
	}

	protected void setOriginalText(String originalText) {
		this.originalText = originalText;
	}

	public String getDateTimeString() {
		return dateTimeString;
	}

	protected void setDateTimeString(String dateTimeString) {
		this.dateTimeString = dateTimeString;
	}

	public String getConDateFormat() {
		return conDateFormat;
	}

	protected void setConDateFormat(String conDateFormat) {
		this.conDateFormat = conDateFormat;
	}

	public int getStart() {
		return start;
	}

	protected void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	protected void setEnd(int end) {
		this.end = end;
	}
	
	
	public String getIdentifiedDateFormat() {
		return identifiedDateFormat;
	}

	public void setIdentifiedDateFormat(String identifiedDateFormat) {
		this.identifiedDateFormat = identifiedDateFormat;
	}

	@Override
	public String toString() {
		return "You've selected ( " + originalText + " ), dateTimeString=" + dateTimeString + ", conDateFormat=" + conDateFormat + ", identifiedDateFormat="+ identifiedDateFormat + ", start=" + start + ", end=" + end + "]";
		//return originalText;
	}

	

}
