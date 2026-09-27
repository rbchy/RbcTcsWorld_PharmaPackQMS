Feature: Serialization and Aggregation
 Scenario: Commission unit-level serial numbers for a batch
  Given the QMS API is running
  When I POST "/api/serialization/commission" with body:
    """
    {"batchId":1,"quantity":3,"aggregationLevel":"UNIT"}
    """
  Then the HTTP status is 201
  And the response body is JSON
  And the response field "[0].aggregationLevel" equals "UNIT"
  And the response field "[0].status" equals "COMMISSIONED"

 Scenario: Commission a CASE-level serial and aggregate units under it
  Given the QMS API is running
  When I POST "/api/serialization/commission" with body:
    """
    {"batchId":1,"quantity":1,"aggregationLevel":"CASE"}
    """
  Then the HTTP status is 201
  And I capture the response field "[0].serialNumber" as "caseSerial"
  When I POST "/api/serialization/commission" with body:
    """
    {"batchId":1,"quantity":2,"aggregationLevel":"UNIT"}
    """
  Then the HTTP status is 201
  And I capture the response field "[0].serialNumber" as "unitSerial1"
  And I capture the response field "[1].serialNumber" as "unitSerial2"
  When I POST "/api/serialization/aggregate" with body:
    """
    {"parentSerialNumber":"{{caseSerial}}","childSerialNumbers":["{{unitSerial1}}","{{unitSerial2}}"]}
    """
  Then the HTTP status is 200
  And the response field "aggregationLevel" equals "CASE"
  And the response field "serialNumber" equals "{{caseSerial}}"

 Scenario: Aggregating a higher-level unit under a lower-level one is rejected
  Given the QMS API is running
  When I POST "/api/serialization/commission" with body:
    """
    {"batchId":1,"quantity":1,"aggregationLevel":"PALLET"}
    """
  Then the HTTP status is 201
  And I capture the response field "[0].serialNumber" as "palletSerial"
  When I POST "/api/serialization/commission" with body:
    """
    {"batchId":1,"quantity":1,"aggregationLevel":"CASE"}
    """
  Then the HTTP status is 201
  And I capture the response field "[0].serialNumber" as "caseSerial2"
  When I POST "/api/serialization/aggregate" with body:
    """
    {"parentSerialNumber":"{{caseSerial2}}","childSerialNumbers":["{{palletSerial}}"]}
    """
  Then the HTTP status is 400

 Scenario: List serialized units for a batch
  Given the QMS API is running
  When I GET "/api/serialization/batch/1"
  Then the HTTP status is 200
  And the response body is JSON
