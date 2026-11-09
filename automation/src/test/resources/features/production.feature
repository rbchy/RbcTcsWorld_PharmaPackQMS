Feature: Production
 Scenario: GET production runs
  Given the QMS API is running
  When I GET "/api/production/runs"
  Then the HTTP status is 200
  And the response body is JSON

 Scenario: POST a production run and a production entry against it
  Given the QMS API is running
  When I POST "/api/production/runs" with body:
    """
    {"batchId":1,"lineId":1}
    """
  Then the HTTP status is 201
  And the response field "status" equals "IN_PROGRESS"
  And the response field "batchNumber" is present
  And the response field "lineCode" is present
  And I capture the response field "id" as "runId"
  When I POST "/api/production/entries" with body:
    """
    {"runId":{{runId}},"produced":100,"good":95,"reject":5,"operatorId":1,"remarks":"Automated production entry test"}
    """
  Then the HTTP status is 201
  And the response body is JSON
  And the response field "batchNumber" is present
  When I GET "/api/production/runs/{{runId}}/entries"
  Then the HTTP status is 200
  And the response body is JSON

 Scenario: Production entries cannot exceed the batch lot size
  Given the QMS API is running
  When I POST "/api/batches" with body:
    """
    {"batchNumber":"BATCH-LOT-{{ts}}","productId":1,"lotSize":500,"manufacturingDate":"2026-01-01","expiryDate":"2027-01-01"}
    """
  Then the HTTP status is 201
  And I capture the response field "id" as "lotBatchId"
  When I POST "/api/production/runs" with body:
    """
    {"batchId":{{lotBatchId}},"lineId":1}
    """
  Then the HTTP status is 201
  And I capture the response field "id" as "lotRunId"
  When I POST "/api/production/entries" with body:
    """
    {"runId":{{lotRunId}},"produced":400,"good":390,"reject":10,"operatorId":1,"remarks":"first shift"}
    """
  Then the HTTP status is 201
  When I POST "/api/production/entries" with body:
    """
    {"runId":{{lotRunId}},"produced":200,"good":200,"reject":0,"operatorId":1,"remarks":"would make 600 of a 500 lot"}
    """
  Then the HTTP status is 400
  When I GET "/api/production/runs/{{lotRunId}}/entries"
  Then the HTTP status is 200
  And the response is a list of at least 1 item
  And the response field "size()" equals "1"

 Scenario: Good + reject cannot be more than produced
  Given the QMS API is running
  When I POST "/api/production/runs" with body:
    """
    {"batchId":1,"lineId":1}
    """
  Then the HTTP status is 201
  And I capture the response field "id" as "badRunId"
  When I POST "/api/production/entries" with body:
    """
    {"runId":{{badRunId}},"produced":100,"good":95,"reject":10,"operatorId":1,"remarks":"105 out of 100"}
    """
  Then the HTTP status is 400
