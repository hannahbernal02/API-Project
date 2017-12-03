# API-Project Brief Overview

This project is a part of my Software Construction course. As a REST API, it uses the Spring framework to get, add, update, or delete
any vehicle from and to the vehicle inventory, which is stored as a local file in the program. It can be accessed through tools such as
the Advanced REST Client with its URL given from the Google App Engine.

The client-side code includes having scheduled tasks that are executed in various times, as specified by Cron expressions. They will automatically run when the program starts.

The server-side code includes the REST API methods that are defined as controllers to modify the local file using HTTP requests.
