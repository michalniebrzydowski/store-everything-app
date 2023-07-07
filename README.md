# StoreEverything

StoreEverything is a comprehensive web application developed with Java and the Spring framework. It provides users with an easy-to-use interface where they can store, organize, and share a wide variety of information. This includes, but is not limited to, web pages, shopping lists, meeting invitations, and personal notes. Through its diverse functionality, StoreEverything serves as a unified platform for efficient and convenient information management.

## User Experience and Functionality
StoreEverything offers users a high level of interactivity and control over the information they save. Once registered, users can gather, organize, and categorize information to suit their specific needs. The ability to label and sort entries by various criteria, such as by date, category, or alphabetically, allows for a tailored user experience and facilitates easier navigation.

Furthermore, the platform includes a unique sorting feature that enables users to sort their entries based on the most popular categories used in their notes. This feature enhances the user's ability to access and manage their most frequently used information quickly and efficiently.

An integral part of StoreEverything's functionality is the ability to share information with others. This sharing can be specific to a user or more general via a generated link, fostering collaboration and knowledge exchange among users. Whether it's web pages, shopping lists, meeting invites, or notes, StoreEverything provides a streamlined platform for saving and sharing worthwhile information.

## User Roles
The application recognizes different user roles with varying access levels: Admin, Full User, Limited User, and Guest. Full Users can fully utilize the system's functionality, while Limited Users can only view shared notes. Admins, apart from using the system like Full Users, also have the authority to manage user accounts. Guests, on the other hand, can only access the home, login, registration pages and shared via link notes.

## Design
StoreEverything prioritizes user experience with a focus on intuitive and accessible interface design. The application primarily employs Bootstrap version 5.3.0 for its front-end layout and interactions. This version of Bootstrap has extensive support for WCAG 2.0 (Web Content Accessibility Guidelines), ensuring the application is accessible for users with different abilities.

In addition to Bootstrap's inherent WCAG 2.0 support, we have made further adjustments and enhancements to comply with certain aspects of WCAG 2.1. These improvements allow us to provide an even better and more inclusive user experience, expanding accessibility to a wider range of users with varying needs.

Moreover, the application leverages Thymeleaf as a server-side Java template engine for web applications. Thymeleaf helps in creating dynamically generated HTML output using static files and Java code, thereby providing a seamless and interactive user experience.

# Getting Started

StoreEverything is designed to be flexible with the choice of database. You can choose to use either a local or an in-memory database depending on your specific needs. By default, the application is configured to use PostgreSQL and H2 databases.

To set up your database:

1. Update the `application.properties` file located in the resources folder with your database details. You should provide the database URL, username, and password.

2. The application utilizes Liquibase to manage database migrations. Upon starting the application, Liquibase will automatically handle the necessary tables and relationships based on the change log files present in the resources folder.

Here are example of how to configure for PostgreSQL:

```application.properties
spring.datasource.url=your_database_url
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=none
```

If you prefer to use a different database, you will need to add the appropriate JDBC driver to the Maven `pom.xml` file and adjust your `application.properties` file accordingly.

Remember to ensure your chosen database instance is running before you start the application.

Remember to add the respective driver as a Maven dependency if you switch databases.

## Used Technologies

StoreEverything primarily leverages the following technologies: Java 17, Spring Boot 3.1.0, Spring Security, Spring Data JPA with PostgreSQL, Thymeleaf, Liquibase, MapStruct, and Lombok.

## Reporting Issues and Suggestions
We value your feedback. If you encounter any issues or have suggestions for improvements, please report them via the "Issues" section of this GitHub repository. Ensure to provide detailed descriptions to help us understand the issue or suggestion better.

Thank you for your contribution.
