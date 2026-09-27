# RbcTcsWorld PharmaPack QMS

## Pharmaceutical Packaging Quality Management System

A full-stack **Pharmaceutical Packaging Quality Management System (QMS)** designed to demonstrate real-world pharmaceutical packaging workflows, quality controls, traceability, electronic signatures, audit trails, and QA automation.

This project combines **Java 21, Spring Boot, Spring Data JPA, MySQL, JWT Security, REST APIs, Maven, Selenium, Cucumber, and automated API/UI testing** into an enterprise-style QA automation project.

---

## 📌 Project Overview

The **RbcTcsWorld PharmaPack QMS** is designed around pharmaceutical packaging operations where every production activity must be traceable from:

```text
Product
   ↓
Batch
   ↓
Packaging Line
   ↓
Equipment
   ↓
Production Run
   ↓
Production Entries
   ↓
AQL Inspection
   ↓
Deviation
   ↓
CAPA
   ↓
QA Review
   ↓
Reconciliation
   ↓
Electronic Signature
   ↓
Audit Trail
```

The system demonstrates how QA, production, packaging, and quality records can be managed through a centralized application.

---

# 🎯 Project Objectives

The primary objectives are to:

* Digitize pharmaceutical packaging QMS workflows
* Maintain complete batch traceability
* Manage packaging products and master data
* Manage production runs and production quantities
* Track good and rejected quantities
* Support AQL inspections
* Record deviations
* Manage CAPA activities
* Perform QA reviews
* Perform production reconciliation
* Implement electronic signatures
* Maintain audit trails
* Secure REST APIs using JWT
* Provide automated API and UI testing
* Demonstrate CI/CD-ready QA automation architecture
* Provide a portfolio project suitable for QA Automation Engineer roles

---

# 🏭 Pharmaceutical Packaging Workflow

```text
                 ┌──────────────────┐
                 │      Product     │
                 └────────┬─────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │      Batch       │
                 └────────┬─────────┘
                          │
                          ▼
              ┌───────────────────────┐
              │ Packaging Line        │
              │ + Equipment           │
              └───────────┬───────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │ Production Run   │
                 └────────┬─────────┘
                          │
                          ▼
                 ┌──────────────────┐
                 │ Production Entry │
                 │ Good / Reject    │
                 └────────┬─────────┘
                          │
             ┌────────────┼─────────────┐
             ▼            ▼             ▼
          AQL QC       Deviation       QA Review
             │            │             │
             │            ▼             │
             │           CAPA           │
             │            │             │
             └────────────┼─────────────┘
                          ▼
                   Reconciliation
                          │
                          ▼
                  Electronic Signature
                          │
                          ▼
                     Audit Trail
```

---

# 🧰 Technology Stack

## Backend

| Technology         | Purpose                      |
| ------------------ | ---------------------------- |
| Java 21            | Application development      |
| Spring Boot        | REST API/backend             |
| Spring Data JPA    | Database persistence         |
| Hibernate          | ORM                          |
| Maven              | Build/dependency management  |
| Jakarta Validation | Request validation           |
| JWT                | Authentication/authorization |
| BCrypt             | Password hashing             |
| Lombok             | Boilerplate reduction        |
| MySQL              | Relational database          |

## QA Automation

| Technology          | Purpose                    |
| ------------------- | -------------------------- |
| Selenium WebDriver  | UI automation              |
| Cucumber            | BDD                        |
| Java                | Automation language        |
| JUnit               | Test execution             |
| REST API testing    | Backend validation         |
| Maven               | Automation build           |
| Page Object Model   | UI automation architecture |
| Data-driven testing | Test data management       |
| Smoke testing       | Critical-path validation   |
| Regression testing  | Full functional validation |

## Planned/Integrated DevOps

* Git
* GitHub
* Jenkins
* Maven CI/CD
* Automated regression execution
* Test reporting
* Cross-browser testing

---

# 🏗️ Project Architecture

```text
RbcTcsWorld_PharmaPackQMS_v2/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── pharmapack/
│   │   │   │           └── qms/
│   │   │   │               ├── auth/
│   │   │   │               ├── batch/
│   │   │   │               ├── product/
│   │   │   │               ├── production/
│   │   │   │               ├── master/
│   │   │   │               ├── aql/
│   │   │   │               ├── deviation/
│   │   │   │               ├── capa/
│   │   │   │               ├── qareview/
│   │   │   │               ├── reconciliation/
│   │   │   │               ├── esignature/
│   │   │   │               ├── audit/
│   │   │   │               └── common/
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   │
│   └── pom.xml
│
├── automation/
│   ├── src/
│   │   ├── test/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   │       └── features/
│   │   └── main/
│   │
│   └── pom.xml
│
├── frontend/
│
├── database/
│   ├── schema/
│   ├── seed/
│   └── scripts/
│
├── docs/
│   ├── architecture/
│   ├── test-plans/
│   └── test-reports/
│
├── pom.xml
└── README.md
```

---

# 🔐 Authentication & Security

The backend uses **JWT-based stateless authentication**.

## Authentication Flow

```text
Client
  │
  │ POST /api/auth/login
  ▼
Authentication Controller
  │
  ▼
User Repository
  │
  ▼
BCrypt Password Verification
  │
  ▼
JWT Token
  │
  ▼
Client
  │
  │ Authorization: Bearer <JWT>
  ▼
JWT Filter
  │
  ▼
Spring Security Context
  │
  ▼
Protected API
```

### Authentication APIs

```text
POST /api/auth/login
GET  /api/auth/me
```

Protected endpoints require:

```http
Authorization: Bearer <JWT>
```

---

# 📦 QMS Modules

## 1. Product Management

Manage pharmaceutical packaging products.

Capabilities:

* Create product
* View products
* View individual product
* Update product
* Delete product
* Duplicate product-code validation
* Required-field validation

Endpoint:

```text
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}
```

Example product:

```json
{
  "productCode": "PMP-001",
  "productName": "Demo Product 10 mg",
  "strength": "10 mg",
  "dosageForm": "Tablet",
  "packSize": "30 tablets",
  "status": "ACTIVE"
}
```

---

# 2. Master Data Management

Master data includes:

### Materials

```text
GET /api/master/materials
```

Examples:

* Printed Carton
* Blister Foil
* Label Roll
* Leaflet

### Equipment

```text
GET /api/master/equipment
```

Examples:

* Cartoner
* Labeler
* Vision System

### Packaging Lines

```text
GET /api/master/lines
```

Examples:

* LINE-01
* LINE-02

---

# 3. Batch Management

Batch management provides complete production-batch traceability.

Capabilities:

* Create batch
* Retrieve batch
* List batches
* Duplicate batch-number validation
* Batch status management
* Audit trail generation

Endpoints:

```text
GET   /api/batches
GET   /api/batches/{id}
POST  /api/batches
PATCH /api/batches/{id}/status
```

Example:

```json
{
  "batchNumber": "QA-BATCH-002",
  "productId": 1,
  "lotSize": 1000,
  "batchStatus": "RELEASED"
}
```

Supported status changes include controlled transitions such as:

```text
CREATED
RELEASED
```

---

# 4. Production / Packaging Run Management

Production traceability is one of the core QMS modules.

A production run connects:

```text
Batch
   +
Packaging Line
   +
Equipment
```

Endpoint:

```text
GET  /api/production/runs
POST /api/production/runs
```

Example:

```json
{
  "batchId": 17,
  "lineId": 1,
  "equipmentId": 1
}
```

Example traceability:

```text
Batch #17
QA-BATCH-002
       ↓
LINE-01
Packaging Line 01
       ↓
EQ-001
Cartoner 01
       ↓
Production Run #14
```

---

# 5. Production Entries

Production entries record packaging quantities.

Endpoint:

```text
POST /api/production/entries
```

Example:

```json
{
  "runId": 14,
  "produced": 1000,
  "good": 980,
  "reject": 20,
  "operatorId": 1,
  "remarks": "QA production entry test"
}
```

Validation includes:

```text
Produced >= 0
Good >= 0
Reject >= 0
Good + Reject <= Produced
Production Run must exist
Batch must exist
```

The project also implements cumulative batch production control against the batch lot size.

---

# 6. AQL Inspection

The QMS supports Acceptance Quality Limit inspection workflows.

Endpoints:

```text
GET  /api/aql/plans
GET  /api/aql/inspections
POST /api/aql/inspections
```

Example AQL plan:

```text
Plan       : AQL-II-1.0
Inspection : Level II
AQL        : 1.0
Sample Size: 125
Acceptance : 3
Rejection  : 4
```

Example inspection:

```json
{
  "sampleSize": 125,
  "defectsFound": 1,
  "result": "ACCEPT"
}
```

---

# 7. Deviation Management

Deviation records document quality or production deviations.

Endpoints:

```text
GET  /api/deviations
POST /api/deviations
```

Example:

```text
Deviation Number : DEV-001
Severity         : MINOR
Status           : OPEN
```

Features:

* Unique deviation number
* Severity
* Status
* Batch association
* Investigation workflow support
* Auditability

---

# 8. CAPA Management

Corrective and Preventive Action management is linked with deviations.

Endpoints:

```text
GET  /api/capa
POST /api/capa
```

Example:

```json
{
  "actionType": "CORRECTIVE",
  "actionDescription": "Automated CAPA test action",
  "ownerId": 1,
  "status": "OPEN"
}
```

Workflow:

```text
Deviation
    ↓
Investigation
    ↓
CAPA
    ↓
Corrective / Preventive Action
    ↓
QA Review
```

---

# 9. QA Review

QA review provides quality approval/review of controlled records.

Endpoints:

```text
GET  /api/qa-reviews
POST /api/qa-reviews
```

Example:

```text
Review Type : IPQC
Decision    : PASS
Reviewer    : QA User
```

---

# 10. Reconciliation

Reconciliation validates production quantities.

The system calculates:

```text
Reconciled Quantity =
Good Quantity
+ Reject Quantity
+ Unused Quantity
```

and determines:

```text
RECONCILED
```

or:

```text
INVESTIGATION_REQUIRED
```

Endpoint:

```text
GET  /api/reconciliations
POST /api/reconciliations
```

Example:

```json
{
  "startingQuantity": 100,
  "goodQuantity": 80,
  "rejectQuantity": 10,
  "unusedQuantity": 10
}
```

Result:

```text
Reconciled Quantity = 100
Variance             = 0
Status               = RECONCILED
```

---

# 11. Electronic Signatures

Electronic signatures provide controlled approval records.

Endpoints:

```text
GET  /api/esignatures
GET  /api/esignatures/entity/{entityName}/{entityId}
POST /api/esignatures
```

Example:

```json
{
  "entityName": "Batch",
  "entityId": "17",
  "actionType": "APPROVE",
  "reason": "QA approval"
}
```

Security validations include:

* Authentication required
* Entity name required
* Entity ID required
* Action type required
* Authenticated user association

---

# 12. Audit Trail

The system maintains audit records for controlled activities.

Endpoints:

```text
GET /api/audit-trails
GET /api/audit-trails/entity/{entityName}/{entityId}
```

Example:

```text
Entity       : Batch
Entity ID    : 17
Action       : STATUS_CHANGE
Field        : batchStatus
Old Value    : CREATED
New Value    : RELEASED
User         : admin
```

Audit example:

```json
{
  "actionType": "STATUS_CHANGE",
  "entityName": "Batch",
  "entityId": "17",
  "fieldName": "batchStatus",
  "oldValue": "CREATED",
  "newValue": "RELEASED"
}
```

---

# 🧪 QA Automation Strategy

The project is designed around multiple levels of testing.

## Smoke Testing

Critical APIs:

```text
Authentication
Product
Batch
Production
AQL
Deviation
CAPA
QA Review
Reconciliation
Electronic Signature
Audit Trail
```

---

## Functional Testing

Examples:

```text
Create Product
Update Product
Create Batch
Change Batch Status
Create Production Run
Create Production Entry
Create AQL Inspection
Create Deviation
Create CAPA
Create QA Review
Create Reconciliation
Create Electronic Signature
```

---

## Negative Testing

The automation framework validates invalid requests such as:

```text
Missing authentication
Invalid JWT
Missing required fields
Duplicate product code
Duplicate batch number
Invalid batch status
Invalid production run
Negative production quantity
Good + Reject > Produced
Production exceeding batch lot size
Invalid entity information
```

---

# 🔎 API Validation Examples

## Unauthorized request

Expected:

```text
HTTP 401
```

Example response:

```json
{
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Authentication required. Please log in."
}
```

---

## Invalid production run

Expected:

```text
HTTP 400
```

Response:

```json
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Production run not found"
}
```

---

## Invalid production quantity

Expected:

```text
HTTP 400
```

Response:

```json
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Good + Reject cannot exceed Produced"
}
```

---

# 🧪 QA Test Coverage

Current validation work has covered:

| Module             | Positive | Negative | Security | Traceability |
| ------------------ | -------: | -------: | -------: | -----------: |
| Authentication     |        ✅ |        ✅ |        ✅ |            — |
| Products           |        ✅ |        ✅ |        ✅ |            — |
| Master Data        |        ✅ |        — |        ✅ |            — |
| Batches            |        ✅ |        ✅ |        ✅ |            ✅ |
| Production Runs    |        ✅ |        ✅ |        ✅ |            ✅ |
| Production Entries |        ✅ |        ✅ |        ✅ |            ✅ |
| AQL                |        ✅ |        — |        ✅ |            — |
| Deviations         |        ✅ |        — |        ✅ |            ✅ |
| CAPA               |        ✅ |        — |        ✅ |            ✅ |
| QA Review          |        ✅ |        — |        ✅ |            ✅ |
| Reconciliation     |        ✅ |        ✅ |        ✅ |            ✅ |
| E-Signature        |        ✅ |        ✅ |        ✅ |            ✅ |
| Audit Trail        |        ✅ |        — |        ✅ |            ✅ |

---

# 🔬 Verified Production Traceability

A complete production traceability scenario has been validated:

```text
Product
PMP-001
Demo Product 10 mg
       │
       ▼
Batch
QA-BATCH-002
       │
       ▼
Packaging Line
LINE-01
       │
       ▼
Equipment
EQ-001
Cartoner 01
       │
       ▼
Production Run
#14
       │
       ▼
Production Entry
#14
       │
       ├── Produced = 1000
       ├── Good     = 980
       └── Reject   = 20
```

---

# 🗄️ Database

The application uses **MySQL**.

Example database:

```text
pharmapack_qms
```

Major entities include:

```text
users
roles
products
materials
equipment
packaging_lines
batches
production_runs
production_entries
aql_plans
aql_inspections
deviations
capa
qa_reviews
reconciliations
electronic_signatures
audit_trails
```

---

# ⚙️ Configuration

Application configuration is maintained through environment variables.

Example:

```properties
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/pharmapack_qms}
spring.datasource.username=${DB_USERNAME:pharmapack_app}
spring.datasource.password=${DB_PASSWORD}
```

### Important

Do **not** commit:

```text
database passwords
JWT secrets
API keys
private credentials
production credentials
```

Use environment variables or local configuration files excluded by `.gitignore`.

---

# 🚀 Running the Backend

## Prerequisites

Install:

* Java 21
* Maven
* MySQL 8+
* Git
* Eclipse / IntelliJ IDEA / VS Code

Verify:

```bash
java -version
mvn -version
mysql --version
```

---

## Clone Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd RbcTcsWorld_PharmaPackQMS_v2
```

---

## Configure Database

Create the database:

```sql
CREATE DATABASE pharmapack_qms;
```

Create/configure the application database user according to your local environment.

Set:

```bash
export DB_URL="jdbc:mysql://localhost:3306/pharmapack_qms?useSSL=false&serverTimezone=America/New_York&allowPublicKeyRetrieval=true"

export DB_USERNAME="pharmapack_app"

export DB_PASSWORD="YOUR_LOCAL_PASSWORD"
```

Never commit the actual password to GitHub.

---

# 🔨 Build

From the project root:

```bash
mvn clean package -DskipTests
```

Successful build:

```text
BUILD SUCCESS
```

---

# ▶️ Run Application

```bash
mvn spring-boot:run
```

Backend:

```text
http://localhost:8080
```

Health check:

```text
http://localhost:8080/actuator/health
```

---

# 🔑 Login

API:

```text
POST /api/auth/login
```

Example:

```json
{
  "username": "admin",
  "password": "YOUR_LOCAL_PASSWORD"
}
```

The response contains a JWT.

Use:

```http
Authorization: Bearer <TOKEN>
```

for protected APIs.

---

# 🧪 Example API Test

Login:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"YOUR_PASSWORD"}' \
  | python3 -c 'import sys,json; print(json.load(sys.stdin)["token"])')
```

Verify authentication:

```bash
curl -i http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

Expected:

```text
HTTP/1.1 200
```

---

# 🧪 Production API Example

Create production run:

```bash
curl -i -X POST http://localhost:8080/api/production/runs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "batchId": 17,
    "lineId": 1,
    "equipmentId": 1
  }'
```

Create production entry:

```bash
curl -i -X POST http://localhost:8080/api/production/entries \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "runId": 14,
    "produced": 1000,
    "good": 980,
    "reject": 20,
    "operatorId": 1,
    "remarks": "QA production entry test"
  }'
```

Retrieve entries:

```bash
curl -i http://localhost:8080/api/production/runs/14/entries \
  -H "Authorization: Bearer $TOKEN"
```

---

# 🧹 Test Data & Environment Isolation

Because QMS automation creates persistent records, repeated test execution can create additional:

```text
Batches
Production Runs
Production Entries
Deviations
CAPA records
QA Reviews
AQL Inspections
E-Signatures
Audit Trails
```

Future automation improvements should include:

* Dedicated test database
* Test profile
* Database cleanup
* Transaction rollback
* Deterministic test data
* Test fixtures
* Unique test identifiers
* API teardown
* CI-specific database

Recommended architecture:

```text
Development DB
        │
        ├── Local manual testing
        │
        ▼
Test DB
        │
        ├── API automation
        ├── Cucumber
        └── Regression
        │
        ▼
CI/CD Test Environment
```

---

# 📊 Test Automation Architecture

Recommended automation structure:

```text
automation/
│
├── src/test/java/
│   └── com/pharmapack/qms/
│       ├── runners/
│       ├── stepdefinitions/
│       ├── pages/
│       ├── api/
│       ├── utils/
│       ├── hooks/
│       └── assertions/
│
└── src/test/resources/
    ├── features/
    ├── testdata/
    └── config/
```

---

# 🥒 Cucumber BDD

Example feature:

```gherkin
Feature: Production Entry Validation

  Scenario: Create valid production entry
    Given the user is authenticated
    And production run 14 exists
    When the user creates a production entry
    Then the production entry should be created successfully

  Scenario: Reject production entry with invalid quantities
    Given the user is authenticated
    When good quantity plus reject quantity exceeds produced quantity
    Then the API should return HTTP 400
```

---

# 🖥️ Selenium UI Automation

The UI automation framework is designed using the:

```text
Page Object Model
```

Example:

```text
LoginPage
DashboardPage
ProductPage
BatchPage
ProductionPage
AQLPage
DeviationPage
CAPAPage
QAReviewPage
ReconciliationPage
ESignaturePage
AuditTrailPage
```

---

# 📈 Test Strategy

The QA strategy follows:

```text
Unit Testing
     ↓
API Testing
     ↓
Integration Testing
     ↓
UI Testing
     ↓
Smoke Testing
     ↓
Regression Testing
     ↓
Cross-Browser Testing
     ↓
CI/CD
```

---

# 🔄 CI/CD Roadmap

Jenkins pipeline:

```text
GitHub Push
     ↓
Jenkins
     ↓
Checkout
     ↓
Compile
     ↓
Unit Tests
     ↓
API Tests
     ↓
Cucumber Tests
     ↓
Selenium Tests
     ↓
Regression Suite
     ↓
Reports
     ↓
Build Result
```

Planned pipeline stages:

```text
Checkout
Build
Unit Test
API Smoke
API Regression
UI Smoke
UI Regression
Report
Archive
```

---

# 📋 QA Test Suites

## Smoke Suite

```text
Authentication
Product GET
Batch GET
Production GET
AQL GET
Deviation GET
CAPA GET
QA Review GET
Reconciliation GET
E-Signature GET
Audit Trail GET
```

## Regression Suite

```text
Authentication
Product CRUD
Batch CRUD
Batch Status
Production Run
Production Entry
AQL Inspection
Deviation
CAPA
QA Review
Reconciliation
Electronic Signature
Audit Trail
Security
Negative Tests
Traceability
```

---

# 🛡️ Security Testing

Security scenarios include:

* Missing JWT
* Invalid JWT
* Expired JWT
* Protected endpoint access
* Unauthorized production entry
* Authentication endpoint validation
* Role-based access control
* Password hashing
* Stateless authentication
* CORS configuration

Example:

```text
No Authorization Header
        ↓
HTTP 401
        ↓
Authentication required
```

---

# 📝 Validation & Error Handling

The application uses centralized error handling.

Example:

```json
{
  "timestamp": "2026-09-27T01:44:58Z",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Production run not found"
}
```

Validation categories include:

```text
400 BAD_REQUEST
401 UNAUTHORIZED
403 FORBIDDEN
404 RESOURCE_NOT_FOUND
500 INTERNAL_SERVER_ERROR
```

---

# 📚 Pharmaceutical QA Concepts Demonstrated

This project is intended to demonstrate practical understanding of:

* Batch traceability
* Packaging line traceability
* Equipment traceability
* Production quantity control
* Good/reject quantity management
* AQL inspection
* Deviation management
* CAPA
* QA review
* Reconciliation
* Electronic signatures
* Audit trails
* Controlled status transitions
* User authentication
* Data integrity
* Negative testing
* Regression testing
* API validation
* Automated traceability testing

---

# 🎓 QA Automation Engineer Skills Demonstrated

This project demonstrates experience with:

### Automation

* Selenium WebDriver
* Cucumber BDD
* JUnit
* Page Object Model
* API automation
* Data-driven testing
* Smoke testing
* Regression testing
* Negative testing
* Cross-browser testing

### Java

* Java 21
* OOP
* Records
* Streams
* Collections
* BigDecimal
* Exception handling
* REST API development

### Backend

* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* REST APIs
* Validation
* Global exception handling

### Database

* MySQL
* SQL
* Relational data modeling
* Foreign-key relationships
* Test data management
* Traceability queries

### DevOps

* Git
* GitHub
* Maven
* Jenkins
* CI/CD

---

# 🔮 Future Enhancements

Planned enhancements include:

## Frontend

* React
* TypeScript
* Dashboard
* Batch management UI
* Production dashboard
* QA dashboard
* CAPA dashboard
* Audit dashboard

## Reporting

* Extent Reports
* Allure Reports
* Cucumber Reports
* HTML reports
* PDF reports

## Automation

* Complete API automation
* Complete Selenium automation
* Cross-browser testing
* Parallel execution
* Data-driven framework
* Database validation
* Contract testing

## CI/CD

* Jenkins pipeline
* GitHub integration
* Automated smoke suite
* Automated regression suite
* Dockerized test environment

## QMS Enhancements

* Material dispensing
* Packaging BOM
* Line clearance
* Batch record management
* In-process checks
* Vision-system inspection
* Serialization
* Barcode validation
* Label verification
* Equipment calibration tracking
* Training records
* Change control
* Document control
* OOS management
* Risk management
* User access management

---

# 📌 Project Status

### Current Status

```text
Backend API                  ████████████████████  Complete
Authentication/JWT          ████████████████████  Complete
Product Management           ████████████████████  Complete
Batch Management             ████████████████████  Complete
Production Management        ████████████████████  Complete
Production Traceability      ████████████████████  Complete
AQL                          ████████████████████  Implemented
Deviation                    ████████████████████  Implemented
CAPA                         ████████████████████  Implemented
QA Review                    ████████████████████  Implemented
Reconciliation               ████████████████████  Implemented
Electronic Signature         ████████████████████  Implemented
Audit Trail                  ████████████████████  Implemented
API QA Testing               ████████████████████  Active
Selenium/Cucumber            ███████████████░░░░░  In Progress
React Frontend               ██████████░░░░░░░░░░  Planned/In Progress
Jenkins CI/CD                ███████░░░░░░░░░░░░░  Planned
Advanced QMS Modules         █████░░░░░░░░░░░░░░░  Planned
```

---

# 👨‍💻 Author

## Ranajit Baran Chowdhury

**QA Automation Engineer | Pharmaceutical Packaging | Java | Selenium | Cucumber | Spring Boot**

Areas of interest:

* Pharmaceutical Packaging QA
* Quality Management Systems
* Test Automation
* API Automation
* Selenium
* Cucumber BDD
* Java
* Spring Boot
* SQL
* CI/CD
* Jenkins
* Pharmaceutical Serialization
* Manufacturing Quality Systems

---

# 📫 Portfolio

Add your professional links here:

```text
Portfolio:
https://www.RbcTcsWorld.com/

LinkedIn:
https://www.linkedin.com/in/rbchy
```

---

# ⚠️ Disclaimer

This project is an **educational and portfolio demonstration system**.

It is not intended to be used as a production pharmaceutical QMS without appropriate:

* Computer System Validation (CSV)
* GxP assessment
* 21 CFR Part 11 assessment
* Data integrity assessment
* Security assessment
* User requirements specification
* Functional requirements specification
* Risk assessment
* Validation protocols
* IQ/OQ/PQ
* SOPs
* Change control
* Audit controls
* Regulatory review

---

# ⭐ GitHub Repository Goals

This repository demonstrates the combination of:

```text
Pharmaceutical Packaging Knowledge
              +
Software Engineering
              +
QA Automation
              +
API Testing
              +
UI Automation
              +
Database Testing
              +
CI/CD
              +
Quality Management
```

The long-term goal is to build an enterprise-style **Pharmaceutical Packaging QMS and QA Automation platform** that demonstrates practical skills relevant to modern pharmaceutical manufacturing and software quality engineering.

This version updates the supplied V2.1 project into a MySQL-backed Spring Boot + React + Cucumber foundation for a pharmaceutical packaging QMS portfolio/training project.

### Main improvements
- Java 21 / Spring Boot 3.5.5
- MySQL integration with `pharmapack_app`
- Product + Batch service layers and DTO-safe Batch responses
- Production, AQL, Reconciliation, QA Review, Deviation and CAPA APIs
- Material, Equipment and Packaging Line master APIs
- React Product and Batch forms
- Cucumber/REST Assured API smoke coverage
- Database scripts and runbook

### Important
The project is designed for portfolio/training use. A real GMP-regulated deployment requires formal validation, access control, audit/e-signature controls, change control, backup/recovery, security review and qualification.
