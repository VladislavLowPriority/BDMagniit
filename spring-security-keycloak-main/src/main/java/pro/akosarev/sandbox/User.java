package pro.akosarev.sandbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User {
	@Id
	private String id;

	@Column(name = "identification_number", length = 255)
	private String identificationNumber;

	@Column(name = "full_name", length = 255)
    private String fullName;
	
	@Column(name = "work_phone", length = 255)
  	private String workPhone;

	@Column(name = "cell_phone", length = 255)
	private String cellPhone;
	
	@Column(name = "direction", length = 255)
    private String direction;
	
	@Column(name = "department", length = 255)
    private String department;
	
	@Column(name = "branch", length = 255)
    private String branch;
		
	@Column(name = "office", length = 255)
    private String office;
		
	@Column(name = "job_title", length = 255)
    private String jobTitle;
	
	
	// ID
	public String getId() {
		return this.id;
}

public void setId(String id) {
		this.id = id;
}

// Identification Number
public String getIdentificationNumber() {
	return this.identificationNumber;
}

public void setIdentificationNumber(String identificationNumber) {
	this.identificationNumber = identificationNumber;
}

// Full Name
public String getFullName() {
		return this.fullName;
}

public void setFullName(String fullName) {
		this.fullName = fullName;
}

// Work Phone
public String getWorkPhone() {
		return this.workPhone;
}

public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
}
// Cell Phone

public String getCellPhone() {
	return this.cellPhone;
}

public void setCellPhone(String cellPhone) {
	this.cellPhone = cellPhone;
}
// Direction
public String getDirection() {
		return this.direction;
}

public void setDirection(String direction) {
		this.direction = direction;
}

// Department
public String getDepartment() {
		return this.department;
}

public void setDepartment(String department) {
		this.department = department;
}

// Branch
public String getBranch() {
		return this.branch;
}

public void setBranch(String branch) {
		this.branch = branch;
}

// Office
public String getOffice() {
		return this.office;
}

public void setOffice(String office) {
		this.office = office;
}

// Job Title
public String getJobTitle() {
		return this.jobTitle;
}

public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
}


}
