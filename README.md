# Java-REST-POST-email-Service

A Java based REST server that provides a POST /email API to send emails.

# Requirements

Need JDK7 or higher
Need Apache Maven (Java package and build management system)

_Install Maven_

(For Mac) brew install maven

# Setting up package

Open file src/main/java/usr/keerthy/email/SendGridEmailProvider.java and update the SendGrid username and password there.

Open file src/main/java/usr/keerthy/email/MailGunEmailProvider.java and update the MailGun custom domain and api-key there.

# And before running the tests ...

Change the 'to' email address in the integration tests from 'sriramkeerthy@gmail.com' to your email address so that tests send email to your address and not to me.

# Running the tests

_mvn clean test_

# Running the rest service

To run using default email provider 'SendGrid'

_mvn exec:java_ (starts service in port 8080; port changeable in src/main/java/user/keerthy/Main.java)

To run using specific email provider

_mvn exec:java -DemailProvider=SendGrid_

_mvn exec:java -DemailProvider=MailGun_

# Sample command to send email once the server is up and running

_cat > emaildata.json_

```json
{
    "to": "sriramkeerthy@gmail.com",
    "to_name": "Sriram Keerthy",
    "from": "noreply@uber.com",
    "from_name": "Uber",
    "subject": "Test: A Message from Uber",
    "body": "<h1>Your Bill</h1><p>$10</p>"
}
```

_curl -X POST http://localhost:8080/email -d @emaildata.json -H "Content-Type: application/json" -v_

# You can change the email provider without restarting the service

_curl -X POST http://localhost:8080/email/provider/MailGun -v_

# Why use Jersey, Apache HttpComponents, Jackson etc.

Jersey is the de-facto standard in Java community to develop RESTful services in an agile environment. It handles most of the boiler-plate functionality very well and gives developers a clean intuitive API/framework to work with.

Apache HttpComponents (formerly Apache Http Commons) is the most widely used HttpClient in the Java community. The new HttpComponents support NIO out of the box, thereby providing better performing client along with a clean API.

Jackson is the most popular Java Json library and has multiple features in a easy to use manner. In this project, I have just used json to java object mapper functionality.

Junit and EasyMock together give a robust (unit) testing platform for Java applications. EasyMock is the cleaner option for mocking, since we cannot mock static calls and so restricts us to follow good class design.\

# What is missing to get this into Production

I have made my own implementation of Factory pattern and Singleton pattern in the email provider classes. In production, I will replace this with either Spring IoC framework or Google Guice IoC framework.

Secure credentials access is needed. Right now, the creds have to hard-coded into the java-file(s) to run the app successfully. Each company has its own secure credentials access infrastructure which needs to be plugged in here to get this to production.

Logging and metrics-gathering needs to be added in the places that I have marked as 'TODO' in the code. Without proper metrics we cannot identify if our email provider is down and if we should switch to an alternate email provider.

The current project is setup as a Java application with an embedded http server running as the rest service. In production environment, java web-apps are usually compressed to .war file and then deployed as apps into a enterprise java servlet container. With a few configuration changes, this Jersey web-app can be compiled into a war and be made deployable into a servlet container.

# What other features are in my wish-list

Introduce a priority based random choosing of email provider so that we can use all available providers and distribute load in a more planned manner.

Need some refactoring of the EmailProviderFactory, so that it automatically scans all available EmailProvider implementations and includes in its API.
