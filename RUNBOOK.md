# Phase 2 Runbook

## 1. MySQL
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed.sql
mysql -u root -p < database/create_app_user.sql
mysql -u root -p < database/phase2-migration.sql
```
Verify:
```sql
USE pharmapack_qms;
SELECT id,product_code,product_name,status FROM products;
SELECT id,batch_number,product_id,batch_status FROM batches;
```

## 2. Backend
Set credentials if different from defaults:
```bash
export DB_USERNAME=pharmapack_app
export DB_PASSWORD='PharmaPack@123'
```
Run from project root:
```bash
mvn clean test
mvn spring-boot:run
```

## 3. API checks
```bash
curl -i http://localhost:8080/api/products
curl -i http://localhost:8080/api/batches
curl -i http://localhost:8080/api/aql/plans
```

## 4. Frontend
```bash
cd frontend
npm install
npm run build
npm run dev
```
Open `http://localhost:5173`.

## 5. Automation
In another terminal after backend is running:
```bash
mvn -f automation/pom.xml test
```

## Expected data flow
Product → Material → Batch → Production → AQL → Reconciliation → QA Review → Deviation/CAPA.
