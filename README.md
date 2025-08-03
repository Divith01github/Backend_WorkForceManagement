# Workforce Management - Starter Project


This is a Spring Boot application for the Backend.


## How to Run


1.  Ensure you have Java 17 and Gradle installed.
2.  Open the project in your favorite IDE (IntelliJ, VSCode, etc.).
3.  Run the main class `com.railse.hiring.workforcemgmt.Application`.
4.  The application will start on `http://localhost:8080`.


## API Endpoints


Here are some example `cURL` commands to interact with the API.


### Get a single task
```bash
curl --location 'http://localhost:8080/task-mgmt/1'
```


### Create a new task
```bash
curl --location 'http://localhost:8080/task-mgmt/create' \
--header 'Content-Type: application/json' \
--data '{
   "requests": [
       {
           "reference_id": 105,
           "reference_type": "ORDER",
           "task": "CREATE_INVOICE",
           "assignee_id": 1,
           "priority": "HIGH",
           "task_deadline_time": 1728192000000
       }
   ]
}'
```


### Update a task's status
```bash
curl --location 'http://localhost:8080/task-mgmt/update' \
--header 'Content-Type: application/json' \
--data '{
   "requests": [
       {
           "task_id": 1,
           "task_status": "STARTED",
           "description": "Work has been started on this invoice."
       }
   ]
}'
```


### Assign tasks by reference 

```bash
curl --location 'http://localhost:8080/task-mgmt/assign-by-ref' \
--header 'Content-Type: application/json' \
--data '{
   "reference_id": 201,
   "reference_type": "ENTITY",
   "assignee_id": 5
}'
```


### Fetch tasks by date 
This fetches tasks for assignees 1 and 2.
```bash
curl --location 'http://localhost:8080/task-mgmt/fetch-by-date/v2' \
--header 'Content-Type: application/json' \
--data '{
   "start_date": 1672531200000,
   "end_date": 1735689599000,
   "assignee_ids": [1, 2]
}'
```
*/

# Task Management API

This API allows managing tasks including updating priority, filtering by priority, commenting, and viewing task history.

---

### 🔄 Change Task Priority

Update the priority of a task.

PATCH http://localhost:8080/task-mgmt/update-priority

curl --location --request PATCH 'http://localhost:8080/task-mgmt/update-priority' \
--header 'Content-Type: application/json' \
--data '{
   "task_id": 5,
   "new_priority": "LOW"
}'

---

### 📋 Get Tasks by Priority

Fetch all tasks with a specific priority.

GET http://localhost:8080/task-mgmt/priority/{PRIORITY}

Example for priority = HIGH:

curl --location 'http://localhost:8080/task-mgmt/priority/HIGH'

---

### 💬 Post Comment to a Task

Add a comment to a task.

POST http://localhost:8080/task-mgmt/tasks/{id}/comments

Example for task ID = 5:

curl --location --request POST 'http://localhost:8080/task-mgmt/tasks/5/comments' \
--header 'Content-Type: application/json' \
--data '{
   "commenter_id": 101,
   "comment_text": "Please prioritize this task for today."
}'

---

### 📜 Get Detailed Task History

Fetch complete details of a task, including comments and change history.

GET http://localhost:8080/task-mgmt/tasks/{id}/details

Example for task ID = 5:

curl --location 'http://localhost:8080/task-mgmt/tasks/5/details'
iled task history -  http://localhost:8080/task-mgmt/tasks/5{id}details

