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

### Change the priority
API- http://localhost:8080/task-mgmt/update-priority
METHOD - PATCH
--data {
   "task_id": 5,
   "new_priority": "LOW"
}
curl --location --request PATCH 'http://localhost:8080/task-mgmt/update-priority' \
--header 'Content-Type: application/json' \
--data '{
   "task_id": 5,
   "new_priority": "LOW"
}'

### Get list of task with ceratin priority
Get list of tasks with certain priority
This fetches all tasks with priority HIGH
API- http://localhost:8080/task-mgmt/priority/{PRIORITY}
curl --location 'http://localhost:8080/task-mgmt/priority/HIGH'


### Post comment and maintain and track history of tasks 
Post a comment to a task
This adds a comment to task ID 5 by commenter ID 101
curl --location --request POST 'http://localhost:8080/task-mgmt/tasks/5/comments' \
--header 'Content-Type: application/json' \
--data '{
   "commenter_id": 101,
   "comment_text": "Please prioritize this task for today."
}'

API for adding comments - http://localhost:8080/task-mgmt/tasks/{id}/comments
data {
  "commenter_id": 101,
  "comment_text": "Please prioritize this task for today."
}

### Get detailed task history
curl --location 'http://localhost:8080/task-mgmt/tasks/5/details'
API for detailed task history -  http://localhost:8080/task-mgmt/tasks/5{id}details


// 
