CREATE KEYSPACE "trms" WITH REPLICATION = {'class': 'SingleRegionStrategy'};

CREATE TABLE trms.employee (
	id int,
	name text, 
	email text,
	password text,
	supervisor int,
	role text,
	primary key (id, name, email)
);

CREATE TABLE trms.reimbursement (
	id int,
	employee int,
	assignedTo int,
	eventDate text,
	eventLocation text,
	eventDescription text,
	eventCost double,
	eventGradingFormat text,
	eventType text,
	eventAttachments list<text>,
	passingGrade double,
	justification text,
	status text,
	dateSubmitted text,
	dateUpdated text,
	preApprovalEmail text,
	preApprovalType text,
	isUrgent boolean,
	proofOfPassing text,
	projectedAmount double,
	awardedAmount double,
	workTimeMissed text,
	message text,
	exceededEmployeesAvailableFunds boolean,
	finalReport text,
	primary key (id, employee, dateSubmitted)
);

CREATE TABLE trms.upload (
	employeeId int,
	s3Key text,
	fileURL text,
	primary key (employeeId, s3Key)
);

insert into trms.employee (id, name, password, email, role, supervisor) 
values (1, 'Dakota', 'password', 'dakota.clark@revature.net', 'CEO', 1)

insert into trms.employee (id, name, password, email, role, supervisor) 
values (2, 'Asuka', 'password', 'asuka@gmail.com', 'SUPER_DEPTHEAD', 1)

insert into trms.employee (id, name, password, email, role, supervisor) 
values (3, 'Mari', 'password', 'mari@email.com', 'SUPERVISOR', 2)

insert into trms.employee (id, name, password, email, role, supervisor) 
values (4, 'Leah', 'password', 'Leah@emial.com', 'ASSOCIATE', 3)

insert into trms.employee (id, name, password, email, role, supervisor) 
values (5, 'R2D2', 'password', 'R2D2@gmail.com', 'SUPER_DEPTHEAD', 1)

insert into trms.employee (id, name, password, email, role, supervisor) 
values (6, 'Shinji', 'password', 'shinji@email.com', 'SUPERVISOR', 5)

insert into trms.employee (id, name, password, email, role, supervisor) 
values (7, 'Ana', 'password', 'ana@email.com', 'BENCO', 6)

insert into trms.employee (id, name, password, email, role, supervisor) 
values (8, 'Tsuneko', 'password', 'Tsuneko@gmail.com', 'BENCO', 6)