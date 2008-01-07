package tutorial.spring25.common;

public class ParseError {
	private Integer line;
	private String text;
	private String reason;
	
	public ParseError() {
	}
	
	
	public ParseError(Integer line, String text, String reason) {
		this.line = line;
		this.text = text;
		this.reason = reason;
	}


	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
