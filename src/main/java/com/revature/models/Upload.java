package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Upload {

	private int employeeID;
	private String s3Key;
	private String fileURL;
	
	public Upload() {
		super();
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public String getS3Key() {
		return s3Key;
	}

	public void setS3Key(String s3Key) {
		this.s3Key = s3Key;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + employeeID;
		result = prime * result + ((fileURL == null) ? 0 : fileURL.hashCode());
		result = prime * result + ((s3Key == null) ? 0 : s3Key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Upload other = (Upload) obj;
		if (employeeID != other.employeeID)
			return false;
		if (fileURL == null) {
			if (other.fileURL != null)
				return false;
		} else if (!fileURL.equals(other.fileURL))
			return false;
		if (s3Key == null) {
			if (other.s3Key != null)
				return false;
		} else if (!s3Key.equals(other.s3Key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Upload [employeeID=" + employeeID + ", s3Key=" + s3Key + ", fileURL=" + fileURL + "]";
	}

}
